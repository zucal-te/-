package com.example.interview.service.impl;

import com.example.interview.service.AiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AiServiceImpl implements AiService {

    @Value("${ai.model.api-key}")
    private String apiKey;

    @Value("${ai.model.api-url}")
    private String apiUrl;

    @Override
    public String chat(String prompt) {
        // 测试可用，后续可替换为真实AI接口调用逻辑
        // 新增异常捕获，避免AI调用失败导致接口报错
        try {
            return "AI评价：" + prompt + "（回答符合要求，逻辑清晰，建议进一步优化细节）";
        } catch (Exception e) {
            return "AI调用失败：" + e.getMessage();
        }
    }
}