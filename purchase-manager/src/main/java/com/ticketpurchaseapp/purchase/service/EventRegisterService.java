package com.ticketpurchaseapp.purchase.service;

import java.util.List;

import com.ticketpurchaseapp.purchase.dto.RegistrationInfo;
import com.ticketpurchaseapp.purchase.dto.SeatCategoryInfo;
import com.ticketpurchaseapp.purchase.dto.SeatCategorySelection;
import com.ticketpurchaseapp.purchase.dto.Show;

public interface EventRegisterService {
    public Boolean checkGroupRegistrationStatus(String groupId, String eventId);
    public RegistrationInfo getRegistrationGroupInfo(String userId, String eventId);
    public Boolean isValidShowForEvent(String eventId, String showId);
    public List<Show> getAllShowsForSpecificEvent(String eventId);
    public String getEventName(String eventId);
    public Integer getGroupSize(String groupId);
}
