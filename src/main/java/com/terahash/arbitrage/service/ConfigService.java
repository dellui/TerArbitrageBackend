package com.terahash.arbitrage.service;

public interface ConfigService {
    void load();
    String getProperty(String key);
    String getProperty(String key, String group);
    String getProperty(String key, String group, String defaultValue);
    public Integer getPropertyAsInteger(String key, String group);
    public Double getPropertyAsDouble(String key, String group);
    public String getTempFileName(String baseFileName);
}
