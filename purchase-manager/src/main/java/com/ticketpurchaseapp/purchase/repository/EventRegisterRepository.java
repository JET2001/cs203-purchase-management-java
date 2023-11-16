package com.ticketpurchaseapp.purchase.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ticketpurchaseapp.purchase.dto.Queue;
import com.ticketpurchaseapp.purchase.dto.Show;

@Mapper
public interface EventRegisterRepository {
    Boolean checkGroupStatus(@Param("group_id") String groupId, @Param("event_id") String eventId);
    Boolean isGroupLeader(@Param("group_id") String groupId, @Param("user_id") String userId);
    String getRegistrationGroupId(@Param("user_id") String userId, @Param("event_id") String eventId);
    List<Queue> retrieveAllQueuesForSpecificEvent(@Param("event_id") String eventId);
    List<Show> retrieveAllShowsForSpecificEvent(@Param("event_id") String eventId);
    List<Queue> retrieveAllQueuesForSpecificGroup(@Param("group_id") String groupId);

    String getEventName(@Param("event_id") String eventId);
    Integer getGroupSize(@Param("group_id") String groupId);
}
