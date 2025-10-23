package com.no99.fusionmodel.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AI模型回答结果")
public class ModelResult {
    @Schema(description = "获胜的模型名称", example = "doubao", allowableValues = {"doubao", "qwen", "error"})
    private String model;

    @Schema(description = "AI模型的回答内容", example = "机器学习是人工智能的一个分支，它让计算机能够在没有明确编程的情况下学习和改进...")
    private String content;

    @Schema(description = "deepseek模型给出的评分", example = "8.5", minimum = "0", maximum = "10")
    private double score;

    public ModelResult() {}

    public ModelResult(String model, String content, double score) {
        this.model = model;
        this.content = content;
        this.score = score;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
