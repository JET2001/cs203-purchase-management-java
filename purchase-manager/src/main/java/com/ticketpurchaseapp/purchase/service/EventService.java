package com.ticketpurchaseapp.purchase.service;

import java.util.List;

import com.ticketpurchaseapp.purchase.dto.Event;
import com.ticketpurchaseapp.purchase.dto.Show;

public interface EventService {
    public List<Event> getAllEvents(boolean onlyHighlighted);
    public Event getEvent(String eventId);
    public List<Show> getAllShowsForSpecificEvent(String eventId);
}
