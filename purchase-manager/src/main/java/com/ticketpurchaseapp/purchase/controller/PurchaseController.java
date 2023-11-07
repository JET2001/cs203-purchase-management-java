package com.ticketpurchaseapp.purchase.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ticketpurchaseapp.purchase.common.exception.EventRegisterException;
import com.ticketpurchaseapp.purchase.common.exception.InvalidArgsException;
import com.ticketpurchaseapp.purchase.common.exception.PurchaseException;
import com.ticketpurchaseapp.purchase.common.exception.UserException;
import com.ticketpurchaseapp.purchase.dto.Event;
import com.ticketpurchaseapp.purchase.dto.SeatCategoryInfo;
import com.ticketpurchaseapp.purchase.dto.SeatCategorySelection;
import com.ticketpurchaseapp.purchase.dto.Status;
import com.ticketpurchaseapp.purchase.dto.User;
import com.ticketpurchaseapp.purchase.service.EventRegisterService;
import com.ticketpurchaseapp.purchase.service.SeatService;
import com.ticketpurchaseapp.purchase.service.UserService;
import com.ticketpurchaseapp.purchase.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin(allowedHeaders = { "Authorization" }, exposedHeaders = { "Access-Control-Allow-Origin",
        "Access-Control-Allow-Credentials" })
@RequestMapping("/purchase")
public class PurchaseController {

    private final SeatService seatService;
    private final EventRegisterService eventRegisterService;

    @Autowired
    public PurchaseController(SeatService seatService, EventRegisterService eventRegisterService) {
        this.seatService = seatService;
        this.eventRegisterService = eventRegisterService;
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<?> getEventName(@PathVariable String eventId) {
        try {
            String eventName = eventRegisterService.getEventName(eventId);
            return new ResponseEntity<>(eventName, HttpStatus.OK);
        } catch (EventRegisterException e) {
            return ResponseEntity.status(404).body("Event Registration Error: " + e.getMessage());
        } catch (InvalidArgsException e) {
            return ResponseEntity.status(422).body("Invalid Request Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/{eventId}/{showId}/categories")
    public ResponseEntity<?> getSeatCategoryInfosForSpecificShow(@PathVariable String eventId,
            @PathVariable String showId) {
        try {
            List<SeatCategoryInfo> seatCategoryInfos = seatService.getSeatCategoryInfos(eventId, showId);
            return new ResponseEntity<>(seatCategoryInfos, HttpStatus.OK);
        } catch (EventRegisterException e) {
            return ResponseEntity.status(404).body("Event Registration Error: " + e.getMessage());
        } catch (PurchaseException e) {
            return ResponseEntity.status(404).body("Purchase Error: " + e.getMessage());
        } catch (InvalidArgsException e) {
            return ResponseEntity.status(422).body("Invalid Request Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server Error: " + e.getMessage());
        }
    }

    @PostMapping("/seat-category-selection")
    public ResponseEntity<?> saveSeatCategorySelection(@RequestBody SeatCategorySelection form) {
        try {
            Boolean addUsersStatus = seatService.saveSeatCategorySelectionForGroup(form);
            Status responseStatus = new Status();
            responseStatus.setStatus(addUsersStatus);
            return new ResponseEntity<>(responseStatus, HttpStatus.OK);
        } catch (PurchaseException e) {
            log.error("Purchase error:", e);
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (EventRegisterException e) {
            log.error("Event Registration error: ", e);
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (UserException e) {
            log.error("User Exception: ", e);
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (InvalidArgsException e) {
            return ResponseEntity.status(422).body("Invalid Request Error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Seat Category Selection error: ", e);
            return ResponseEntity.status(500).body("Server Error: " + e.getMessage());
        }
    }
}
