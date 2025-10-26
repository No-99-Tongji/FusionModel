package com.no99.fusionmodel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "聊天响应")
public class ChatResponse {

    @Schema(description = "响应选项列表")
    private List<Choice> choices;

    public ChatResponse() {
    }

    public ChatResponse(List<Choice> choices) {
        this.choices = choices;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    @Schema(description = "响应选项")
    public static class Choice {
        @Schema(description = "消息内容")
        private ChatMessage message;

        public Choice() {
        }

        public Choice(ChatMessage message) {
            this.message = message;
        }

        public ChatMessage getMessage() {
            return message;
        }

        public void setMessage(ChatMessage message) {
            this.message = message;
        }
    }
}
