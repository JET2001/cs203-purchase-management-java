package com.ticketpurchaseapp.purchase.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ticketpurchaseapp.purchase.dto.Event;
import com.ticketpurchaseapp.purchase.dto.Queue;
import com.ticketpurchaseapp.purchase.dto.Show;

@Mapper
public interface EventRepository {
    List<Event> retrieveAllEvents();
    Event retrieveEvent(@Param("event_id") String eventId);
    List<Show> retrieveAllShowsForSpecificEvent(@Param("event_id") String eventId);
    List<Queue> retrieveAllQueuesForSpecificEvent(@Param("event_id") String eventId);
    List<String> retrieveAllCountriesForSpecificEvent(@Param("event_id") String eventId);

    List<Event> retrieveHighlightedEventInfo();
    List<Queue> retrieveAllQueuesForSpecificGroup(@Param("group_id") String groupId);
}
