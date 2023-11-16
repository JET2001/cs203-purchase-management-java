package com.ticketpurchaseapp.purchase.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ticketpurchaseapp.purchase.common.exception.EventRegisterException;
import com.ticketpurchaseapp.purchase.common.exception.InvalidArgsException;
import com.ticketpurchaseapp.purchase.common.exception.UserException;
import com.ticketpurchaseapp.purchase.dto.Queue;
import com.ticketpurchaseapp.purchase.dto.RegistrationInfo;
import com.ticketpurchaseapp.purchase.dto.Show;
import com.ticketpurchaseapp.purchase.dto.UserInfo;
import com.ticketpurchaseapp.purchase.repository.EventRegisterRepository;
import com.ticketpurchaseapp.purchase.repository.UserRepository;
import com.ticketpurchaseapp.purchase.service.EventRegisterService;
import com.ticketpurchaseapp.purchase.util.Utility;

import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventRegisterServiceImpl implements EventRegisterService {

    private final EventRegisterRepository eventRegisterRepository;
    private final UserRepository userRepository;

    public EventRegisterServiceImpl(EventRegisterRepository eventRegisterRepository, UserRepository userRepository) {
        this.eventRegisterRepository = eventRegisterRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Integer getGroupSize(String groupId) {
        Utility utility = new Utility();
        if (!utility.isInputSafe(groupId)) {
            throw new InvalidArgsException("Invalid Request");
        }
        Integer groupSize = eventRegisterRepository.getGroupSize(groupId);
        if (groupSize == 0) {
            throw new EventRegisterException("Invalid Group ID");
        }
        return groupSize;
    }

    @Override
    public String getEventName(String eventId) {
        Utility utility = new Utility();
        if (!utility.isInputSafe(eventId)) {
            throw new InvalidArgsException("Invalid Request");
        }
        String eventName = eventRegisterRepository.getEventName(eventId);
        if (eventName == null) {
            throw new EventRegisterException("Invalid Event ID");
        }
        return eventName;
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
                throw new EventRegisterException("Group ID / Event ID does not exist");
            }
            return status;
        } catch (EventRegisterException e) {
            throw new EventRegisterException(e.getMessage());
        } catch (Exception e) {
            // Handle any unexpected exceptions here
            e.printStackTrace(); // Log the exception for debugging
            throw new EventRegisterException("Unable to retrieve Group Data");
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

            List<Queue> queueList = eventRegisterRepository.retrieveAllQueuesForSpecificGroup(groupId);

            return new RegistrationInfo(groupId, userInfoList, hasAllUsersConfirmed, queueList);
        } catch (UserException e) {
            throw new UserException(e.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new RuntimeException("Unable to get User's registration group information");
        }
    }

    @Override
    public List<Show> getAllShowsForSpecificEvent(String eventId) {
        Utility utility = new Utility();
        if (!utility.isInputSafe(eventId)) {
            throw new InvalidArgsException("Invalid Request");
        }
        List<Show> allShowsForEvent = eventRegisterRepository.retrieveAllShowsForSpecificEvent(eventId);
        List<Queue> allQueuesForEvent = eventRegisterRepository.retrieveAllQueuesForSpecificEvent(eventId);

        Map<String, List<Queue>> groupedByShowId = allQueuesForEvent.stream()
                .collect(Collectors.groupingBy(Queue::getShowId));

        for (Show show : allShowsForEvent) {
            show.setQueues(groupedByShowId.get(show.getId()));
        }

        return allShowsForEvent;
    }

    @Override
    public Boolean isValidShowForEvent(String eventId, String showId) {
        List<Show> showList = getAllShowsForSpecificEvent(eventId);
        if (showList == null || showList.size() == 0) {
            return false;
        }
        for (Show show : showList) {
            if (show.getId().equals(showId)) {
                return true;
            }
        }
        return false;
    }
}
