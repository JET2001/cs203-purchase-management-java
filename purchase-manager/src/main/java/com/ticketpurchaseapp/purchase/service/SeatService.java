package com.ticketpurchaseapp.purchase.service;

import java.util.List;

import com.ticketpurchaseapp.purchase.dto.SeatCategoryInfo;
import com.ticketpurchaseapp.purchase.dto.SeatCategorySelection;

public interface SeatService {
    public Boolean saveSeatCategorySelectionForGroup(SeatCategorySelection form);
    public List<SeatCategoryInfo> getSeatCategoryInfos(String eventId, String showId);
}
