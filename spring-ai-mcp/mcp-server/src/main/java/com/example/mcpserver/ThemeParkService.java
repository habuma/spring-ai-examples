package com.example.mcpserver;

public interface ThemeParkService {

    String getParks();

    String getEntitySchedule(String entityId);

    String getEntityLive(String entityId);

    String getEntityChildren(String entityId);

}
