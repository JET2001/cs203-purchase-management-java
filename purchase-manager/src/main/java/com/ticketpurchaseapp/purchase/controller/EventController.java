package com.ticketpurchaseapp.purchase.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticketpurchaseapp.purchase.common.exception.EventException;
import com.ticketpurchaseapp.purchase.common.exception.InvalidArgsException;
import com.ticketpurchaseapp.purchase.dto.Event;
import com.ticketpurchaseapp.purchase.dto.Show;
import com.ticketpurchaseapp.purchase.service.EventService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllEvents() {
        try {
            List<Event> allEvents = eventService.getAllEvents(false);
            return new ResponseEntity<>(allEvents, HttpStatus.OK);
        }
        catch (Exception e) {
            log.error("Events Retrieval Error: ", e);
            return ResponseEntity.status(500).body("Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/highlighted")
    public ResponseEntity<?> getEventHighlights() {
        try {
            List<Event> highlightedEvents = eventService.getAllEvents(true);
            return ResponseEntity.ok().body(highlightedEvents);
        } catch (Exception e){
            log.error("Highlighted events retrieval error: ", e); 
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getSpecificEvent(@PathVariable String eventId) {
        try {
            Event event = eventService.getEvent(eventId);
            return new ResponseEntity<>(event, HttpStatus.OK);
        }
        catch (EventException e) {
            return ResponseEntity.status(404).body("Event Error: " + e.getMessage());
        }
        catch (InvalidArgsException e) {
            return ResponseEntity.status(422).body("Invalid Request Error: " + e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Server Error: " + e.getMessage());
        } 
    }

    @GetMapping("/{eventId}/shows")
    public ResponseEntity<?> getShowsForSpecificEvent(@PathVariable String eventId) {
        try {
            List<Show> allShowsForSpecificEvent = eventService.getAllShowsForSpecificEvent(eventId);
            return new ResponseEntity<>(allShowsForSpecificEvent, HttpStatus.OK);
        }
        catch (EventException e) {
            return ResponseEntity.status(404).body("Event Error: " + e.getMessage());
        }
        catch (InvalidArgsException e) {
            return ResponseEntity.status(422).body("Invalid Request Error: " + e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Server Error: " + e.getMessage());
        }
    }
    
}




