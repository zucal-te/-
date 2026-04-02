package com.example.interview.controller;

import com.example.interview.entity.ChatMessage;
import com.example.interview.mapper.InterviewMapper;
import com.example.interview.service.AiService;
import com.example.interview.util.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interview")
public class InterviewController {

    private final AiService aiService;
    private final InterviewMapper interviewMapper;

    public InterviewController(AiService aiService, InterviewMapper interviewMapper) {
        this.aiService = aiService;
        this.interviewMapper = interviewMapper;
    }

    // AI面试问答（核心接口，适配chat_message新表结构）
    @PostMapping("/chat")
    public Result<String> chat(@RequestParam String prompt, 
                               @RequestParam String interviewId,
                               @RequestParam String userId) {
        try {
            // 调用AI服务获取回答（测试可用，后续可替换为真实逻辑）
            String aiFeedback = aiService.chat(prompt);
            // 保存对话记录到chat_message表
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setInterviewId(interviewId);
            chatMessage.setUserId(userId);
            chatMessage.setUserAnswer(prompt); // 此处暂用prompt作为用户回答，可根据实际需求修改
            chatMessage.setAiFeedback(aiFeedback);
            chatMessage.setScore(80); // 测试分数，可根据AI评价动态赋值
            interviewMapper.addChatMessage(chatMessage);
            return Result.success(aiFeedback);
        } catch (Exception e) {
            // 异常捕获，返回明确错误信息
            return Result.fail("接口调用失败：" + e.getMessage());
        }
    }

    // 查询所有对话记录
    @GetMapping("/chat/all")
    public Result<List<ChatMessage>> getAllChatMessage() {
        try {
            List<ChatMessage> list = interviewMapper.getAllChatMessage();
            return Result.success(list);
        } catch (Exception e) {
            return Result.fail("查询失败：" + e.getMessage());
        }
    }

    // 根据ID查询单条对话记录
    @GetMapping("/chat/{id}")
    public Result<ChatMessage> getChatMessageById(@PathVariable Long id) {
        try {
            ChatMessage chatMessage = interviewMapper.getChatMessageById(id);
            return Result.success(chatMessage);
        } catch (Exception e) {
            return Result.fail("查询失败：" + e.getMessage());
        }
    }

    // 根据interview_id查询该面试的所有对话记录
    @GetMapping("/chat/interview/{interviewId}")
    public Result<List<ChatMessage>> getChatMessageByInterviewId(@PathVariable String interviewId) {
        try {
            List<ChatMessage> list = interviewMapper.getChatMessageByInterviewId(interviewId);
            return Result.success(list);
        } catch (Exception e) {
            return Result.fail("查询失败：" + e.getMessage());
        }
    }
}
