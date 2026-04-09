package com.example.interview.entity;
import lombok.Data;

@Data
public class InterviewQuestion {
    private Long id;
    private String questionId;
    private String role;
    private String difficulty;
    private String content;
}
