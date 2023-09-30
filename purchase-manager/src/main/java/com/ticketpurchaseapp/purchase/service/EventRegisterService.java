package com.ticketpurchaseapp.purchase.service;

import java.util.List;
import java.util.Map;

import com.ticketpurchaseapp.purchase.dto.AddMember;
import com.ticketpurchaseapp.purchase.dto.Registration;
import com.ticketpurchaseapp.purchase.dto.RegistrationInfo;
import com.ticketpurchaseapp.purchase.dto.User;
import com.ticketpurchaseapp.purchase.service.enums.ValStatus;
import com.ticketpurchaseapp.purchase.dto.UserInfo;

public interface EventRegisterService {
    public Registration registerGroup(Registration form);

    public Boolean registerUsers(List<User> userList, String groupId, String eventId, String groupLeaderEmail);

    public Boolean checkGroupRegistrationStatus(String groupId, String eventId);

    public Boolean updateEventGroupUserConfirmation(String userId, String eventId, String groupId);
    public List<ValStatus> validateGroup (List<String> emailList, List<String> mobileList, String eventId);
    public RegistrationInfo getRegistrationGroupInfo(String userId, String eventId);
    public Boolean addUsersToGroup(AddMember form);
}
