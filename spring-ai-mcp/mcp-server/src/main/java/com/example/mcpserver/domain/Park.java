package com.example.mcpserver.domain;

public record Park(
        String entityId,
        String parkName,
        String parentDestinationEntityId,
        String parentDestinationName) {}
