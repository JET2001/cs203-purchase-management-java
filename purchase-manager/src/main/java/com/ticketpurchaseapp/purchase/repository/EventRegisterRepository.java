package com.ticketpurchaseapp.purchase.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ticketpurchaseapp.purchase.dto.Registration;
import com.ticketpurchaseapp.purchase.dto.User;

@Mapper
public interface EventRegisterRepository {
    int registerGroup(Registration form);

    int registerUser(@Param("user") User user, @Param("group_id") String groupId, @Param("event_id") String eventId,
            @Param("confirmation") int confirmation);

    Boolean checkGroupStatus(@Param("group_id") String groupId, @Param("event_id") String eventId);
    int updateUserStatus(@Param("group_id") String groupId, @Param("event_id") String eventId, @Param("user_id") String userId);
    Boolean checkUserStatus(@Param("group_id") String groupId, @Param("event_id") String eventId, @Param("user_id") String userId);
    String getRegistrationGroupId(@Param("user_id") String userId, @Param("event_id") String eventId);

    int userGroupForEventCount(@Param("user_id") String userId, @Param("event_id") String eventId);
    String getRegistrationGroupLeader(@Param("group_id") String groupId);
    Boolean isGroupLeader(@Param("group_id") String groupId, @Param("user_id") String userId);
}
