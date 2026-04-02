package com.example.interview.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ai.model")
@Data
public class AiConfig {
    private String apiKey;
    private String apiUrl;
}
