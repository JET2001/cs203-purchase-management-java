package com.ticketpurchaseapp.purchase.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Event {
    private String id;
    private String name;
    private int maxQueueable;
    private boolean isHighlighted;
    private String description;
    private String posterImagePath;
    private List<String> countries; 
}
