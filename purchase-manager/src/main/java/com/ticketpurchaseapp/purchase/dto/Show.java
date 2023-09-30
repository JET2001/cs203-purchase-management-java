package com.ticketpurchaseapp.purchase.dto;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Show {
    private String id;
    private Event event;
    private LocalDateTime dateTime;
    private String locationId;
    private String locationName;
    private String venueId;
    private List<Queue> queues;
}
