package com.example.mcpserver.domain;

public record EntityQueueEntry(
        int waitTime,
        String state,
        String returnStart,
        String returnEnd) {}
