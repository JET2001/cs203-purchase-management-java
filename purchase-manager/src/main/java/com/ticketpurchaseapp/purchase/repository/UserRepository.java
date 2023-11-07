package com.ticketpurchaseapp.purchase.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ticketpurchaseapp.purchase.dto.User;
import com.ticketpurchaseapp.purchase.dto.UserAuth;
import com.ticketpurchaseapp.purchase.dto.UserInfo;

@Mapper
public interface UserRepository {

    User findUserByEmailAndMobile(@Param("email") String email, @Param("mobile") String mobile);

    User findUserByEmail(@Param("email") String email);

    UserAuth retrieveUserForAuth(@Param("email") String email, @Param("mobile") String mobile);

    List<UserInfo> getAllUserInfo(@Param("group_id") String groupId);

    int recordLoginFailed(@Param("ip_address") String ipAddress, @Param("datetime_recorded") LocalDateTime datetime_recorded);

    boolean isLoginLocked(@Param("ip_address") String ipAddress, @Param("datetime_recorded") LocalDateTime datetime_recorded);

    int removeExpiredLoginRecords(@Param("datetime_recorded") LocalDateTime datetime_recorded);
}