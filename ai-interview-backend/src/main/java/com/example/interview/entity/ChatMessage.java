package com.example.interview.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMessage {
    // 主键ID（与chat_message表id字段一致，自增，bigint类型）
    private Long id;
    // 面试唯一ID（与chat_message表interview_id字段一致）
    private String interviewId;
    // 用户唯一ID（与chat_message表user_id字段一致）
    private String userId;
    // 题目ID（与chat_message表question_id字段一致）
    private String questionId;
    // 用户文字回答（与chat_message表user_answer字段一致）
    private String userAnswer;
    // AI评价（与chat_message表ai_feedback字段一致）
    private String aiFeedback;
    // 分数（与chat_message表score字段一致）
    private Integer score;
    // 创建时间（与chat_message表create_time字段一致）
    private LocalDateTime createTime;
}
