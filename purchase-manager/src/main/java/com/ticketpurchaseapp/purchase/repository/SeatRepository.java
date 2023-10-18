package com.ticketpurchaseapp.purchase.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ticketpurchaseapp.purchase.dto.SeatCategoryInfo;
import com.ticketpurchaseapp.purchase.dto.Seat;

@Mapper
public interface SeatRepository {
    List<SeatCategoryInfo> getSeatCategoryInfos(@Param("event_id") String eventId, @Param("show_id") String show_id);
    List<Seat> getSeatsInCategoryForSpecificShow(@Param("event_id") String eventId, @Param("show_id") String showId, @Param("category_id") int categoryId);
    int saveSeatCategorySelection(@Param("event_id") String eventId, @Param("show_id") String showId, @Param("category_id") int categoryId, @Param("seat_id") int seatId, @Param("user_id") String userId);
    boolean userHasSeatForSpecificShow(@Param("event_id") String eventId, @Param("show_id") String showId, @Param("user_id") String userId);
}
