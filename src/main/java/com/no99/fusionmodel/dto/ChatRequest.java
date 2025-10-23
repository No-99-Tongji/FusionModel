package com.no99.fusionmodel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "聊天请求")
public class ChatRequest {
    @Schema(description = "AI模型名称", example = "deepseek", allowableValues = {"deepseek", "qwen", "doubao"})
    private String model;

    @Schema(description = "消息列表", required = true)
    private List<ChatMessage> messages;

    @Schema(description = "温度参数，控制回答的随机性", example = "0.7", minimum = "0", maximum = "2")
    private double temperature = 0.7;

    @Schema(description = "是否流式返回", example = "false")
    private boolean stream = false;

    @Schema(description = "最大token数", example = "1000", minimum = "1", maximum = "4096")
    private int max_tokens = 1000;

    public ChatRequest() {}

    public ChatRequest(String model, List<ChatMessage> messages) {
        this.model = model;
        this.messages = messages;
    }

    // Getters and setters
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }

    public int getMax_tokens() {
        return max_tokens;
    }

    public void setMax_tokens(int max_tokens) {
        this.max_tokens = max_tokens;
    }
}
