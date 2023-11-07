package com.ticketpurchaseapp.purchase.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Queue {
    private String queueId;
    private String startDateTime;
    private String endDatetime;
    private String showId;
    private String eventId;

}
