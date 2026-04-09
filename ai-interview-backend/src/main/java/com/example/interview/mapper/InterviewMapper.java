package com.example.interview.mapper;
import com.example.interview.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface InterviewMapper {
    // 1. 会话操作
    int insertSession(InterviewSession session);
    int finishSession(@Param("interviewId") String interviewId);

    // 2. 题库操作：根据岗位和难度随机抽取题目
    List<InterviewQuestion> getRandomQuestions(@Param("role") String role, @Param("difficulty") String difficulty, @Param("limit") int limit);

    // 3. 对话明细操作
    int addChatMessage(ChatMessage chatMessage);
    List<ChatMessage> getChatMessageByInterviewId(@Param("interviewId") String interviewId);

    // 4. 报告操作
    int insertReport(InterviewReport report);
}

