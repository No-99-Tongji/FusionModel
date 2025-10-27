package com.no99.fusionmodel.service;

import com.no99.fusionmodel.config.ModelConfig;
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

    private final ModelConfig modelConfig;
    private final WebClient webClient;

    public AIModelService(ModelConfig modelConfig) {
        this.modelConfig = modelConfig;
        this.webClient = WebClient.builder().build();
    }

    public ModelResult getBestResponse(List<ChatMessage> messages) {
        try {
            // 并行调用配置的模型
            List<CompletableFuture<ModelResponse>> modelFutures = modelConfig.getGeneration().stream()
                    .map(modelName -> callModelWithName(modelName, messages))
                    .toList();

            // 等待所有模型的响应
            List<ModelResponse> responses = modelFutures.stream()
                    .map(CompletableFuture::join)
                    .toList();

            // 使用配置的评分模型对所有响应进行评分
            ModelResult bestResult = null;
            double bestScore = -1;

            for (ModelResponse response : responses) {
                double score = scoreResponse(response.content, messages);
                if (score > bestScore) {
                    bestScore = score;
                    bestResult = new ModelResult(response.modelName, response.content, score);
                }
            }

            return bestResult != null ? bestResult :
                new ModelResult("default", "No valid response received", 0.0);
        } catch (Exception e) {
            throw new RuntimeException("Error processing AI model requests", e);
        }
    }

    private CompletableFuture<ModelResponse> callModelWithName(String modelName, List<ChatMessage> messages) {
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
                    String content = response.getChoices().get(0).getMessage().getContent();
                    return new ModelResponse(modelName, content);
                }
                return new ModelResponse(modelName, "No response from " + modelName);
            } catch (Exception e) {
                return new ModelResponse(modelName, "Error calling " + modelName + " model: " + e.getMessage());
            }
        });
    }

    private static class ModelResponse {
        final String modelName;
        final String content;

        ModelResponse(String modelName, String content) {
            this.modelName = modelName;
            this.content = content;
        }
    }

    private double scoreResponse(String response, List<ChatMessage> messages) {
        try {
            String context = messages.stream()
                    .map(msg -> msg.getRole() + ": " + msg.getContent())
                    .reduce("", (a, b) -> a + "\n" + b);

            String scorePrompt = String.format(
                    "请为以下回答的质量打分，分数在0-10之间，只返回数字。\n\n" +
                    "对话上下文:\n%s\n\n" +
                    "回答内容:\n%s\n\n" +
                    "评分标准：准确性、相关性、完整性、清晰度。只返回分数数字。",
                    context, response
            );

            ChatMessage scoreMessage = new ChatMessage("user", scorePrompt);
            ChatRequest scoreRequest = new ChatRequest(modelConfig.getScorer(), List.of(scoreMessage));

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
                    // 如果评分模型返回的不是数字，尝试提取数字
                    String[] parts = scoreText.split("\\s+");
                    for (String part : parts) {
                        try {
                            return Double.parseDouble(part);
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    return 5.0; // 默认分数
                }
            }
            return 5.0; // 默认分数
        } catch (Exception e) {
            return 5.0; // 出错时返回默认分数
        }
    }
}
