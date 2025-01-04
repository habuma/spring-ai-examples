package com.example.mcpserver.domain;

public record Destination(
        String id,
        String name,
        DestinationPark[] parks) {}
