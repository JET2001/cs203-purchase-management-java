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
public class AddMember {
    private String eventId;
    private String groupId;
    private String userId;
    private List<User> userGroup;
}
