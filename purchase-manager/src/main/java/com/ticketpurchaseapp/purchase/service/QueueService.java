package com.ticketpurchaseapp.purchase.service;

public interface QueueService {
    public Integer getQueueNumber(String email, String eventId, String queueId);

    public boolean updateQueueFactor(String queueId);
}
