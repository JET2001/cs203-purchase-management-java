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
public class RegistrationInfo {
    private String groupId;
    private List<UserInfo> userInfoList;
    private Boolean hasAllUsersConfirmed;
    private List<Queue> queueList;
}