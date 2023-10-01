package com.ticketpurchaseapp.purchase.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    private String id;
    private String mobile;
    private String email;
    private String password;
    private String authenticatorId;
    private boolean verified;

}
