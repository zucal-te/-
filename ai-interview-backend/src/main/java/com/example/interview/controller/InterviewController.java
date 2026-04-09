package com.example.interview.controller;

import com.example.interview.entity.*;
import com.example.interview.mapper.InterviewMapper;
import com.example.interview.service.AiService;
import com.example.interview.util.Result;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/interview")
public class InterviewController {

    private final AiService aiService;
    private final InterviewMapper interviewMapper;
    private final ObjectMapper objectMapper = new ObjectMapper(); // 用于解析JSON

    public InterviewController(AiService aiService, InterviewMapper interviewMapper) {
        this.aiService = aiService;
        this.interviewMapper = interviewMapper;
    }

    // 1. 初始化面试：生成场次并抽题
    @PostMapping("/start")
    public Result<Map<String, Object>> startInterview(@RequestParam String userId,
                                                      @RequestParam String role,
                                                      @RequestParam String difficulty) {
        // 生成场次ID
        String interviewId = "INT-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        
        InterviewSession session = new InterviewSession();
        session.setInterviewId(interviewId);
        session.setUserId(userId);
        session.setRole(role);
        session.setDifficulty(difficulty);
        interviewMapper.insertSession(session);

        // 随机抽取5道题 (请确保 interview_question 表里有数据，否则会返回空列表)
        List<InterviewQuestion> questions = interviewMapper.getRandomQuestions(role, difficulty, 5);

        Map<String, Object> data = new HashMap<>();
        data.put("interviewId", interviewId);
        data.put("questions", questions);
        return Result.success(data);
    }

    // 2. 聊天与打分
    @PostMapping("/chat")
    public Result<String> chat(@RequestParam String prompt,
                               @RequestParam String interviewId,
                               @RequestParam String userId,
                               @RequestParam String questionId) {
        try {
            // 让大模型返回严格的 JSON
            String aiSystemPrompt = "你是一个专业面试官。用户回答了问题。请你给出点评和0-100的打分。" +
                    "必须只返回合法的JSON字符串，格式如下：{\"feedback\": \"你的文字点评\", \"score\": 85}\n\n用户回答：" + prompt;
            
            String aiResultStr = aiService.chat(aiSystemPrompt);
            
            // 解析大模型返回的 JSON
            // 提示：大模型有时候会用 ```json 包裹，可以用正则或字符串处理清理一下
            if (aiResultStr.startsWith("```json")) {
                aiResultStr = aiResultStr.replace("```json", "").replace("```", "").trim();
            }
            JsonNode node = objectMapper.readTree(aiResultStr);
            String feedback = node.has("feedback") ? node.get("feedback").asText() : "AI未给出点评";
            int score = node.has("score") ? node.get("score").asInt() : 60;

            // 存入明细表
            ChatMessage msg = new ChatMessage();
            msg.setInterviewId(interviewId);
            msg.setUserId(userId);
            msg.setQuestionId(questionId);
            msg.setUserAnswer(prompt);
            msg.setAiFeedback(feedback);
            msg.setScore(score);
            interviewMapper.addChatMessage(msg);

            return Result.success(feedback);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("对话接口处理异常：" + e.getMessage());
        }
    }

    // 3. 结束交卷与生成报告
    @PostMapping("/submit_and_report")
    public Result<InterviewReport> submitAndReport(@RequestParam String interviewId,
                                                   @RequestParam String userId) {
        try {
            // 结束会话
            interviewMapper.finishSession(interviewId);

            // 获取该场面试的所有历史聊天
            List<ChatMessage> historyList = interviewMapper.getChatMessageByInterviewId(interviewId);
            if(historyList.isEmpty()){
                return Result.fail("未查到聊天记录，无法生成报告");
            }

            // 拼接给AI的提示词
            StringBuilder sb = new StringBuilder();
            sb.append("这是一场完整的面试记录，请评估表现，并给出四大维度的打分（总分100）。\n");
            for (int i = 0; i < historyList.size(); i++) {
                ChatMessage m = historyList.get(i);
                sb.append("第").append(i+1).append("题得分:").append(m.getScore()).append("\n");
                sb.append("用户回答:").append(m.getUserAnswer()).append("\n");
            }
            sb.append("\n请严格按照以下JSON格式返回（不可包含其他多余字符）：\n");
            sb.append("{\"totalScore\": 85, \"conclusion\": \"整体不错\", \"dimensions\": {\"tech\":80, \"logic\":90, \"expression\":85, \"stability\":80}, \"strengths\": \"基础扎实\", \"weaknesses\": \"深度不够\", \"suggestion\": \"多看源码\"}");

            // 调用 AI
            String reportJsonStr = aiService.chat(sb.toString());
            
            if (reportJsonStr.startsWith("```json")) {
                reportJsonStr = reportJsonStr.replace("```json", "").replace("```", "").trim();
            }
            
            JsonNode reportNode = objectMapper.readTree(reportJsonStr);

            // 构造报告实体存入数据库
            InterviewReport report = new InterviewReport();
            report.setInterviewId(interviewId);
            report.setUserId(userId);
            report.setTotalScore(reportNode.get("totalScore").asInt());
            report.setConclusion(reportNode.get("conclusion").asText());
            report.setDimensions(reportNode.get("dimensions").toString()); // 维度存JSON串
            report.setStrengths(reportNode.get("strengths").asText());
            report.setWeaknesses(reportNode.get("weaknesses").asText());
            report.setSuggestion(reportNode.get("suggestion").asText());

            interviewMapper.insertReport(report);

            // 直接返回报告对象供前端渲染雷达图
            return Result.success(report);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("生成报告异常：" + e.getMessage());
        }
    }
}
