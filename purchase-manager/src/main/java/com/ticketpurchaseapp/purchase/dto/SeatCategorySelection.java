package com.ticketpurchaseapp.purchase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SeatCategorySelection {
    private String groupId;
    private String eventId;
    private String showId;
    private String userId;
    private int categoryId;
}
