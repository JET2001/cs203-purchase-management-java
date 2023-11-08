package com.ticketpurchaseapp.purchase.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;

import com.ticketpurchaseapp.purchase.common.exception.InvalidArgsException;
import com.ticketpurchaseapp.purchase.common.exception.UserException;
import com.ticketpurchaseapp.purchase.dto.User;
import com.ticketpurchaseapp.purchase.service.QueueService;
import com.ticketpurchaseapp.purchase.service.UserService;
import com.ticketpurchaseapp.purchase.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin(allowedHeaders = { "Authorization", "Content-Type" }, exposedHeaders = { "Access-Control-Allow-Origin",
        "Access-Control-Allow-Credentials", "Access-Control-Allow-Headers" })
@RequestMapping("/queues")
public class QueueController {
    
    private final QueueService queueService;

    @Autowired
    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }
    
    @PostMapping("/queue-number")
    public ResponseEntity<?> getQueueNumber(@RequestBody HashMap<String, String> queueData){
        try {
            Map<String, Integer> queueNumberMap = new HashMap<>();
            Integer queueNumber = queueService.getQueueNumber(queueData.get("email"), queueData.get("eventId"), queueData.get("queueId"));
            queueNumberMap.put("queueNumber", queueNumber);
            return ResponseEntity.ok().body(queueNumberMap);
        } catch (InvalidArgsException e){
            return ResponseEntity.unprocessableEntity().body("Invalid Request: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }

    @PostMapping("/queue-factor")
    public ResponseEntity<?> updateQueueFactor(@RequestBody HashMap<String, String> queueData){
        try {
            boolean status = queueService.updateQueueFactor(queueData.get("queueId"));
            return ResponseEntity.ok().body(status);
        } catch (InvalidArgsException e){
            return ResponseEntity.unprocessableEntity().body("Invalid Request: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }
}
