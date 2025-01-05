package com.example.mcpserver.domain;

public record EntityQueue(
        EntityQueueEntry STANDBY,
        EntityQueueEntry RETURN_TIME,
        EntityQueueEntry SINGLE_RIDER) {}
