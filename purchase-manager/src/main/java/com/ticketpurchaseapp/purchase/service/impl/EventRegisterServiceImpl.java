package com.ticketpurchaseapp.purchase.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ticketpurchaseapp.purchase.common.exception.EventRegisterException;
import com.ticketpurchaseapp.purchase.common.exception.InvalidArgsException;
import com.ticketpurchaseapp.purchase.common.exception.UserException;
import com.ticketpurchaseapp.purchase.dto.AddMember;
import com.ticketpurchaseapp.purchase.dto.Registration;
import com.ticketpurchaseapp.purchase.dto.User;
import com.ticketpurchaseapp.purchase.dto.UserInfo;
import com.ticketpurchaseapp.purchase.dto.RegistrationInfo;
import com.ticketpurchaseapp.purchase.dto.Queue;
import com.ticketpurchaseapp.purchase.repository.EventRegisterRepository;
import com.ticketpurchaseapp.purchase.repository.EventRepository;
import com.ticketpurchaseapp.purchase.repository.UserRepository;
import com.ticketpurchaseapp.purchase.service.EventRegisterService;
import com.ticketpurchaseapp.purchase.service.enums.ValStatus;
import com.ticketpurchaseapp.purchase.util.Utility;

import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventRegisterServiceImpl implements EventRegisterService {

    private final EventRegisterRepository eventRegisterRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public EventRegisterServiceImpl(UserRepository userRepository, EventRepository eventRepository,
            EventRegisterRepository eventRegisterRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.eventRegisterRepository = eventRegisterRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Registration registerGroup(Registration form) throws UserException {
        log.info("Begin registerGroup Service method ");
        List<User> userList = form.getUserGroup();
        List<User> responseUserList = new ArrayList<>();

        responseUserList = this.verifyUser(userList);
        form.setUserGroup(responseUserList);

        Utility util = new Utility();
        String groupId = util.generateRandomUUID();
        log.info("Generate Group ID: " + groupId);
        form.setId(groupId);

        User groupLeader = userRepository.findUserByEmail(form.getGroupLeaderEmail());
        form.setGroupLeaderId(groupLeader.getId());
        try {
            log.info(form.getGroupLeaderId());
            eventRegisterRepository.registerGroup(form);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                // Handle the specific duplicate key scenario
                throw new UserException("User is already a group leader of another Group of the same event!");
            } else {
                throw new UserException("Group Insertion Error");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new UserException("Group registration error!");
        }
        log.info("Group Registration Success");

        try {
            this.registerUsers(responseUserList, groupId, form.getEventId(), form.getGroupLeaderEmail());
            log.info("Group Users Registration Success");
        } catch (Exception e) {
            throw new UserException(e.getMessage());
        }
        return form; // Registration is successful

    }

    @Override
    public Boolean registerUsers(List<User> userList, String groupId, String eventId, String groupLeaderEmail) {
        Utility utility = new Utility();
        if (!utility.emailWhitelist(groupLeaderEmail) || !utility.isInputSafe(eventId) || !utility.isInputSafe(groupId)) {
            throw new InvalidArgsException("Invalid request");
        }
        try {
            for (User user : userList) {
                boolean isLeaderBool = user.getEmail().equals(groupLeaderEmail);
                int isLeader = isLeaderBool ? 1 : 0;
                eventRegisterRepository.registerUser(user, groupId, eventId, isLeader);
            }
            return true; // User registration is successful
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                // Handle the specific duplicate key scenario
                throw new UserException("User is already in another Group of the same event!");
            } else {
                throw new UserException("Group Users Registration Insertion Error");
            }
        } catch (Exception e) {
            throw new UserException("Group Users Registration Error");
        }
    }

    @Override
    public Boolean checkGroupRegistrationStatus(String groupId, String eventId) {
        Utility utility = new Utility();
        if (!utility.isInputSafe(eventId) || !utility.isInputSafe(groupId)) {
            throw new InvalidArgsException("Invalid request");
        }
        try {
            Boolean status = eventRegisterRepository.checkGroupStatus(groupId, eventId);

            if (status == null) {
                throw new EventRegisterException("Group ID/Event ID does not exist");
            }
            return status;
        } catch (Exception e) {
            // Handle any unexpected exceptions here
            e.printStackTrace(); // Log the exception for debugging
            throw new EventRegisterException("Unable to retrieve Group Data");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateEventGroupUserConfirmation(String userId, String eventId, String groupId) {
        Utility utility = new Utility();
        if (!utility.isInputSafe(userId) || !utility.isInputSafe(eventId) || !utility.isInputSafe(groupId)) {
            throw new InvalidArgsException("Invalid request");
        }
        try {
            eventRegisterRepository.updateUserStatus(groupId, eventId, userId);
            Boolean status = eventRegisterRepository.checkUserStatus(groupId, eventId, userId);
            if (status == null) {
                throw new EventRegisterException("User ID/Group ID/Event ID does not match");
            }
            return status;
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            throw new EventRegisterException("Unable to update user confirmation Data");
        }
    }

    @Override
    public RegistrationInfo getRegistrationGroupInfo(String userId, String eventId) {
        Utility utility = new Utility();
        if (!utility.isInputSafe(eventId) || !utility.isInputSafe(userId)) {
            throw new InvalidArgsException("Invalid request");
        }
        try {
            String groupId = eventRegisterRepository.getRegistrationGroupId(userId, eventId);
            if (groupId == null) {
                throw new UserException("User ID / Event ID does not exist");
            }

            List<UserInfo> userInfoList = userRepository.getAllUserInfo(groupId);

            Boolean hasAllUsersConfirmed = eventRegisterRepository.checkGroupStatus(groupId, eventId);

            List<Queue> queueList = eventRepository.retrieveAllQueuesForSpecificGroup(groupId);

            return new RegistrationInfo(groupId, userInfoList, hasAllUsersConfirmed, queueList);
        } catch (UserException e) {
            throw new UserException(e.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new RuntimeException("Unable to get User's registration group information");
        }
    }

    @Override
    public List<ValStatus> validateGroup(List<String> emailList, List<String> mobileList, String eventId) {
        if (emailList == null || mobileList == null || emailList.size() != mobileList.size()) {
            throw new InvalidArgsException("email list or mobile list provided is invalid.");
        }
        List<ValStatus> output = new ArrayList<>();
        Iterator<String> emailIter = emailList.iterator(), mobileIter = mobileList.iterator();
        while (emailIter.hasNext() && mobileIter.hasNext()) {
            output.add(determineUserElligibility(emailIter.next(), mobileIter.next(), eventId));
        }
        return output;
    }

    private ValStatus determineUserElligibility(String email, String mobile, String eventId){
        Utility utility = new Utility();
        if (!utility.emailWhitelist(email) || !utility.isInputSafe(mobile) || !utility.isInputSafe(eventId)) {
            throw new InvalidArgsException("Invalid request");
        }
        User user = userRepository.findUserByEmailAndMobile(email, mobile);
        if (user == null) return ValStatus.USER_DOES_NOT_EXIST;

        if (!user.isVerified()) return ValStatus.USER_NOT_VERIFIED;

        // Searches the event
        int groupCount = eventRegisterRepository.userGroupForEventCount(user.getId(), eventId);

        if(groupCount > 0) return ValStatus.USER_IN_OTHER_GROUP;
        
        return ValStatus.VALID_MEMBER;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean addUsersToGroup(AddMember form) {
        if (!eventRegisterRepository.isGroupLeader(form.getGroupId(), form.getUserId())) {
            throw new EventRegisterException("User ID is not a group leader!");
        }
        List<User> verifiedUsers = verifyUser(form.getUserGroup());
        registerUsers(verifiedUsers, form.getGroupId(), form.getEventId(), form.getUserId());
        return true;
    }

    public List<User> verifyUser(List<User> userList) {
        List<User> verifiedUserList = new ArrayList<>();
        for (User user : userList) {
            try {
                User verifiedUser = userRepository.findUserByEmailAndMobile(user.getEmail(), user.getMobile());
                if (verifiedUser == null) {
                    throw new UserException("Email(s) in group are not registered as users");
                }

                if (verifiedUser != null && !verifiedUser.isVerified()) {
                    throw new UserException("User(s) in group are not verified"); // User is not authenticated
                }
                verifiedUserList.add(verifiedUser);
            } catch (UserException e) {
                throw new UserException(e.getMessage());
            } catch (Exception e) {
                throw new UserException("User Error!");
            }
        }
        return verifiedUserList;
    }
}
