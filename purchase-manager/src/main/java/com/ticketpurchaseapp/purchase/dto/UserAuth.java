package com.ticketpurchaseapp.purchase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserAuth {
    private String userId;
    private String password;
    private boolean allowLogin;
}
