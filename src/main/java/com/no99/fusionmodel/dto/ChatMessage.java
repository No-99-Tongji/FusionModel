package com.no99.fusionmodel.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "聊天消息")
public class ChatMessage {
    @Schema(description = "消息角色", example = "user", allowableValues = {"user", "assistant", "system"})
    private String role;

    @Schema(description = "消息内容", example = "什么是机器学习？")
    private String content;

    public ChatMessage() {}

    public ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
