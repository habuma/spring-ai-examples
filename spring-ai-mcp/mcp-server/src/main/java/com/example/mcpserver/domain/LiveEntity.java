package com.example.mcpserver.domain;

public record LiveEntity(
        String id,
        String name,
        LiveData[] liveData) {
}
