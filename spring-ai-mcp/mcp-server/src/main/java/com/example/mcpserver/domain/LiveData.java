package com.example.mcpserver.domain;

public record LiveData(
        String id,
        String name,
        String entityType,
        String status,
        ShowTime[] showtimes,
        EntityQueue queue
) {
}
