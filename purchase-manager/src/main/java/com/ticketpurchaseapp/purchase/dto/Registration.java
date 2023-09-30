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
public class Registration {
    private String id;
    private String eventId;
    private String groupLeaderId;
    private String groupLeaderEmail;
    private int groupSize;
    private List<User> userGroup;

}
