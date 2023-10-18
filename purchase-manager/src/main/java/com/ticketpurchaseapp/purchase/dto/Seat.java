package com.ticketpurchaseapp.purchase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Seat {
    private String locationId;
    private String venueId;
    private int seatId;
    private int rowId;
    private int colId;
}
