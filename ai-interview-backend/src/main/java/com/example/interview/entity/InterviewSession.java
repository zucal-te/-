package com.example.interview.entity;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InterviewSession {
    private Long id;
    private String interviewId;
    private String userId;
    private String role;
    private String difficulty;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

