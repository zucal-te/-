package com.example.interview.entity;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InterviewReport {
    private Long id;
    private String interviewId;
    private String userId;
    private Integer totalScore;
    private String conclusion;
    private String dimensions; // JSON字符串
    private String strengths;
    private String weaknesses;
    private String suggestion;
    private LocalDateTime createTime;
}

