package com.grafyn.assignment.service;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.grafyn.assignment.model.DBConfig;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.io.File;
import java.io.IOException;

@Service
public class ConfigService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private DBConfig config;

    @PostConstruct
    public void loadConfig() {
        // Default path — can be customized via system property or env var
        String configPath = System.getProperty("crawler.config.path",
                "src/main/resources/crawler-config.json");

        try {
            config = objectMapper.readValue(new File(configPath), DBConfig.class);
            System.out.println("✅ Loaded configuration from: " + configPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file: " + configPath, e);
        }
    }

    public DBConfig getConfig() {
        if (config == null) loadConfig();
        return config;
    }
}

