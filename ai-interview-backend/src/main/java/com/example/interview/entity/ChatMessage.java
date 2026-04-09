package com.example.interview.entity;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMessage {
    private Long id;
    private String interviewId;
    private String userId;
    private String questionId;
    private String userAnswer;
    private String aiFeedback;
    private Integer score;
    private LocalDateTime createTime;
}

