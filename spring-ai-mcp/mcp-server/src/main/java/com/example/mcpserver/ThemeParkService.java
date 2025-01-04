package com.example.mcpserver;

public interface ThemeParkService {

    String getDestinations();

    String getEntitySchedule(String entityId);

    String getEntityLive(String entityId);

}
