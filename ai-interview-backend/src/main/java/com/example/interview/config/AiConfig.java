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


//java -jar target/interview-0.0.1-SNAPSHOT.jar --spring.main.web-application-type=servlet http://localhost:8080/interview/chat/all http://localhost:8080/interview/chat/all