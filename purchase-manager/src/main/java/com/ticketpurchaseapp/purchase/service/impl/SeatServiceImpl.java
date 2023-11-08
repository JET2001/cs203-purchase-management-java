package com.ticketpurchaseapp.purchase.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;

import com.ticketpurchaseapp.purchase.common.exception.EventRegisterException;
import com.ticketpurchaseapp.purchase.common.exception.InvalidArgsException;
import com.ticketpurchaseapp.purchase.common.exception.PurchaseException;
import com.ticketpurchaseapp.purchase.common.exception.UserException;
import com.ticketpurchaseapp.purchase.dto.RegistrationInfo;
import com.ticketpurchaseapp.purchase.dto.Seat;
import com.ticketpurchaseapp.purchase.dto.SeatCategoryInfo;
import com.ticketpurchaseapp.purchase.dto.SeatCategorySelection;
import com.ticketpurchaseapp.purchase.dto.UserInfo;
import com.ticketpurchaseapp.purchase.repository.EventRegisterRepository;
import com.ticketpurchaseapp.purchase.repository.SeatRepository;
import com.ticketpurchaseapp.purchase.service.EventRegisterService;
import com.ticketpurchaseapp.purchase.service.SeatService;
import com.ticketpurchaseapp.purchase.util.SeatAlgorithm;
import com.ticketpurchaseapp.purchase.util.Utility;

import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.events.Event;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final EventRegisterRepository eventRegisterRepository;
    private final EventRegisterServiceImpl eventRegisterService;

    @Autowired
    public SeatServiceImpl(SeatRepository seatRepository, EventRegisterRepository eventRegisterRepository,
            EventRegisterServiceImpl eventRegisterService) {
        this.seatRepository = seatRepository;
        this.eventRegisterRepository = eventRegisterRepository;
        this.eventRegisterService = eventRegisterService;
    }

    @Override
    public List<SeatCategoryInfo> getSeatCategoryInfos(String eventId, String showId) {
        Utility utility = new Utility();
        if (!utility.isInputSafe(eventId) || !utility.isInputSafe(showId)) {
            throw new InvalidArgsException("Invalid Request");
        }
        List<SeatCategoryInfo> seatCategoryInfos = seatRepository.getSeatCategoryInfos(eventId, showId);
        if (seatCategoryInfos == null || seatCategoryInfos.size() == 0) {
            throw new EventRegisterException("Invalid Event ID / Show ID");
        }
        return seatCategoryInfos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveSeatCategorySelectionForGroup(SeatCategorySelection form) {

        String userId = form.getUserId();
        String groupId = form.getGroupId();
        String eventId = form.getEventId();
        String showId = form.getShowId();

        Utility utility = new Utility();
        if (!utility.isInputSafe(userId) || !utility.isInputSafe(groupId) || !utility.isInputSafe(eventId)
                || !utility.isInputSafe(showId)) {
            throw new InvalidArgsException("Invalid Request");
        }

        if (!eventRegisterService.checkGroupRegistrationStatus(groupId, eventId)) {
            throw new EventRegisterException("Group is not registered for that event");
        }

        RegistrationInfo groupRegistrationInfo = eventRegisterService.getRegistrationGroupInfo(userId, eventId);
        if (groupRegistrationInfo == null) {
            throw new UserException("Invalid User ID / Event ID");
        }

        if (!eventRegisterRepository.isGroupLeader(groupId, userId)) {
            throw new UserException("User is not the group leader for that group");
        }

        if (!eventRegisterService.isValidShowForEvent(eventId, showId)) {
            throw new EventRegisterException("Invalid Show ID / Event ID");
        }

        List<UserInfo> userInfoList = groupRegistrationInfo.getUserInfoList();
        int groupSize = userInfoList.size();

        List<Seat> seatsInCategory = seatRepository.getSeatsInCategoryForSpecificShow(eventId, showId,
                form.getCategoryId());
        if (seatsInCategory == null || seatsInCategory.size() == 0) {
            throw new PurchaseException("There are no seats available in that category for that show");
        }

        SeatAlgorithm seatAlgorithm = new SeatAlgorithm();

        List<Seat> seatAllocation = seatAlgorithm.getSeatAllocation(seatsInCategory, groupSize);
        if (seatAllocation == null) {
            throw new PurchaseException("There are not enough seats in that category for that group size");
        }

        for (int i = 0; i < groupSize; i++) {
            int seatIdAllocation = seatAllocation.get(i).getSeatId();
            String userIdAllocation = userInfoList.get(i).getId();

            if (seatRepository.userHasSeatForSpecificShow(eventId, showId, userIdAllocation)) {
                throw new PurchaseException("User is already allocated a seat");
            }
            seatRepository.saveSeatCategorySelection(eventId, showId, form.getCategoryId(), seatIdAllocation,
                    userIdAllocation);
        }

        return true;
    }
}
