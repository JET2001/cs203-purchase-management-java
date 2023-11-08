package com.ticketpurchaseapp.purchase.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ticketpurchaseapp.purchase.common.exception.InvalidArgsException;
import com.ticketpurchaseapp.purchase.dto.User;
import com.ticketpurchaseapp.purchase.repository.EventRegisterRepository;
import com.ticketpurchaseapp.purchase.repository.UserRepository;
import com.ticketpurchaseapp.purchase.service.QueueService;
import com.ticketpurchaseapp.purchase.util.Utility;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QueueServiceImpl implements QueueService {

    private final EventRegisterRepository eventRegisterRepository;
    private final UserRepository userRepository;

    @Autowired
    public QueueServiceImpl(EventRegisterRepository eventRegisterRepository, UserRepository userRepository) {
        this.eventRegisterRepository = eventRegisterRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Integer getQueueNumber(String email, String eventId, String queueId) {
        Utility utility = new Utility();
        if (!utility.emailWhitelist(email) || !utility.isInputSafe(eventId) || !utility.isInputSafe(queueId) ) {
            throw new InvalidArgsException("invalid login credentials - failed preliminary check");
        }
        User user = userRepository.findUserByEmail(email);
        String groupId = eventRegisterRepository.getRegistrationGroupId(user.getId(), eventId);
        String groupLeaderEmail = eventRegisterRepository.getRegistrationGroupLeader(groupId);
        if (!user.getId().equals(groupLeaderEmail)) {
            throw new RuntimeException("User not group leader");
        }
        

        Integer sQueueNumber = eventRegisterRepository.getQueueNumber(queueId, groupId);
        
        Integer queueFactor = eventRegisterRepository.getQueueFactor(queueId);
        
        Integer queueNumber = sQueueNumber - queueFactor;
        
        return queueNumber;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateQueueFactor(String queueId) {
        Utility utility = new Utility();
        if (!utility.isInputSafe(queueId) ) {
            throw new InvalidArgsException("invalid login credentials - failed preliminary check");
        }
        eventRegisterRepository.updateQueueFactor(queueId);
        return true;
    }
}
