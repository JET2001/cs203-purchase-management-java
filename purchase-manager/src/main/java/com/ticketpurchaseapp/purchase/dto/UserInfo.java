package com.ticketpurchaseapp.purchase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInfo {
    private String id;
    private String mobile;
    private String email;
    private boolean isConfirmed;
    private boolean isGroupLeader;
}
