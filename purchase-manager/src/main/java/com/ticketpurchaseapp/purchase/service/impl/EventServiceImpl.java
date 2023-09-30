package com.ticketpurchaseapp.purchase.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketpurchaseapp.purchase.common.exception.EventException;
import com.ticketpurchaseapp.purchase.common.exception.InvalidArgsException;
import com.ticketpurchaseapp.purchase.dto.Event;
import com.ticketpurchaseapp.purchase.dto.Queue;
import com.ticketpurchaseapp.purchase.dto.Show;
import com.ticketpurchaseapp.purchase.repository.EventRepository;
import com.ticketpurchaseapp.purchase.service.EventService;
import com.ticketpurchaseapp.purchase.util.Utility;


@Service
public class EventServiceImpl implements EventService{
    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    
    @Override
    public List<Event> getAllEvents(boolean onlyHighlighted) {
        List<Event> eventsList = null;
        if(onlyHighlighted){

            eventsList = eventRepository.retrieveHighlightedEventInfo();

            // Update the eventsList with countries.
            for (Event event: eventsList){
                List<String> countries = eventRepository.retrieveAllCountriesForSpecificEvent(event.getId());
                event.setCountries(countries);
            }
        } else {
            eventsList = eventRepository.retrieveAllEvents();
        }
        return eventsList;
    }

    @Override
    public Event getEvent(String eventId){
        Utility utility = new Utility();
        if (!utility.isInputSafe(eventId)) {
            throw new InvalidArgsException("Invalid Request");
        }
        Event event = eventRepository.retrieveEvent(eventId);
        if (event == null) {
            throw new EventException("Event does not exist");
        }
        return event;
    }

    @Override
    public List<Show> getAllShowsForSpecificEvent(String eventId) {
        Utility utility = new Utility();
        if (!utility.isInputSafe(eventId)) {
            throw new InvalidArgsException("Invalid Request");
        }
        List<Show> allShowsForEvent = eventRepository.retrieveAllShowsForSpecificEvent(eventId);
        List<Queue> allQueuesForEvent = eventRepository.retrieveAllQueuesForSpecificEvent(eventId);
        
        Map<String, List<Queue>> groupedByShowId = allQueuesForEvent.stream()
            .collect(Collectors.groupingBy(Queue::getShowId));
        
        for (Show show : allShowsForEvent) {
            show.setQueues(groupedByShowId.get(show.getId()));
        }

        return allShowsForEvent;
    }

    
}
