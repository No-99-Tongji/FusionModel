package com.no99.fusionmodel.controller;

import com.no99.fusionmodel.dto.ChatRequest;
import com.no99.fusionmodel.dto.ModelResult;
import com.no99.fusionmodel.service.AIModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@Tag(name = "AI模型融合", description = "并行调用多个AI模型并智能评分返回最佳回答的API")
public class ChatController {

    @Autowired
    private AIModelService aiModelService;

    @PostMapping("/best-response")
    @Operation(
            summary = "获取最佳AI回答",
            description = "并行调用doubao和qwen两个AI模型，使用deepseek评分后返回最佳回答"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功获取最佳回答",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ModelResult.class),
                            examples = @ExampleObject(
                                    name = "成功响应示例",
                                    value = """
                                            {
                                              "model": "doubao",
                                              "content": "人工智能（AI）是一门研究如何让计算机模拟人类智能行为的科学...",
                                              "score": 8.5
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "服务器内部错误",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ModelResult.class),
                            examples = @ExampleObject(
                                    name = "错误响应示例",
                                    value = """
                                            {
                                              "model": "error",
                                              "content": "Error processing request: 具体错误信息",
                                              "score": 0.0
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<ModelResult> getBestResponse(
            @Parameter(
                    description = "聊天请求，包含用户消息列表",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ChatRequest.class),
                            examples = @ExampleObject(
                                    name = "请求示例",
                                    value = """
                                            {
                                              "messages": [
                                                {
                                                  "role": "user",
                                                  "content": "什么是机器学习？请详细解释"
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            )
            @RequestBody ChatRequest request) {
        try {
            ModelResult result = aiModelService.getBestResponse(request.getMessages());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ModelResult("error", "Error processing request: " + e.getMessage(), 0.0));
        }
    }

    @GetMapping("/health")
    @Operation(
            summary = "健康检查",
            description = "检查服务是否正常运行"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "服务运行正常",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    name = "健康检查响应",
                                    value = "Service is running on port 8081"
                            )
                    )
            )
    })
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Service is running on port 8081");
    }
}
