package com.no99.fusionmodel.service;

import com.no99.fusionmodel.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AIModelService {

    @Value("${aigateway.baseurl}")
    private String baseUrl;

    private final WebClient webClient;

    public AIModelService() {
        this.webClient = WebClient.builder().build();
    }

    public ModelResult getBestResponse(List<ChatMessage> messages) {
        try {
            // 并行调用doubao和qwen模型
            CompletableFuture<String> doubaoFuture = callModel("doubao", messages);
            CompletableFuture<String> qwenFuture = callModel("qwen", messages);

            // 等待两个模型的响应
            String doubaoResponse = doubaoFuture.get();
            String qwenResponse = qwenFuture.get();

            // 使用deepseek评分
            double doubaoScore = scoreResponse(doubaoResponse, messages);
            double qwenScore = scoreResponse(qwenResponse, messages);

            // 返回评分最高的结果
            if (doubaoScore >= qwenScore) {
                return new ModelResult("doubao", doubaoResponse, doubaoScore);
            } else {
                return new ModelResult("qwen", qwenResponse, qwenScore);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing AI model requests", e);
        }
    }

    private CompletableFuture<String> callModel(String modelName, List<ChatMessage> messages) {
        return CompletableFuture.supplyAsync(() -> {
            ChatRequest request = new ChatRequest(modelName, messages);

            try {
                ChatResponse response = webClient.post()
                        .uri(baseUrl)
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(ChatResponse.class)
                        .block();

                if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                    return response.getChoices().get(0).getMessage().getContent();
                }
                return "No response from " + modelName;
            } catch (Exception e) {
                throw new RuntimeException("Error calling " + modelName + " model", e);
            }
        });
    }

    private double scoreResponse(String response, List<ChatMessage> originalMessages) {
        try {
            // 构造评分提示
            String scorePrompt = """
                请对以下回答进行评分（0-10分），只需要返回数字分数。
                原始问题：%s
                回答：%s
                评分标准：准确性、有用性、清晰度。只返回分数，不要其他内容。
                """.formatted(getLastUserMessage(originalMessages), response);

            ChatMessage scoreMessage = new ChatMessage("user", scorePrompt);
            ChatRequest scoreRequest = new ChatRequest("deepseek", List.of(scoreMessage));

            ChatResponse scoreResponse = webClient.post()
                    .uri(baseUrl)
                    .bodyValue(scoreRequest)
                    .retrieve()
                    .bodyToMono(ChatResponse.class)
                    .block();

            if (scoreResponse != null && scoreResponse.getChoices() != null && !scoreResponse.getChoices().isEmpty()) {
                String scoreText = scoreResponse.getChoices().get(0).getMessage().getContent().trim();
                try {
                    return Double.parseDouble(scoreText);
                } catch (NumberFormatException e) {
                    // 如果无法解析分数，尝试提取数字
                    String[] parts = scoreText.split("\\s+");
                    for (String part : parts) {
                        try {
                            double score = Double.parseDouble(part);
                            if (score >= 0 && score <= 10) {
                                return score;
                            }
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }
            return 5.0; // 默认分数
        } catch (Exception e) {
            return 5.0; // 出错时返回默认分数
        }
    }

    private String getLastUserMessage(List<ChatMessage> messages) {
        for (int i = messages.size() - 1; i >= 0; i--) {
            ChatMessage message = messages.get(i);
            if ("user".equals(message.getRole())) {
                return message.getContent();
            }
        }
        return "No user message found";
    }
}
