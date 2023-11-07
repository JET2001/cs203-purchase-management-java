package com.ticketpurchaseapp.purchase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SeatCategoryInfo {
    private int categoryId;
    private double seatPrice;
    private int seatsLeft;
    private int seatsTotal;
}
