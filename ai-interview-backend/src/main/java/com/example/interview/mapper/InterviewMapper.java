package com.example.interview.mapper;

import com.example.interview.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface InterviewMapper {
    // 新增对话记录（适配chat_message表新结构）
    int addChatMessage(ChatMessage chatMessage);
    // 查询所有对话记录
    List<ChatMessage> getAllChatMessage();
    // 根据ID查询单条对话记录
    ChatMessage getChatMessageById(Long id);
    // 根据interview_id查询该面试的所有对话记录
    List<ChatMessage> getChatMessageByInterviewId(String interviewId);
}
