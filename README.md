# FusionModel 微服务

这是一个AI模型融合微服务，它可以：
1. 并行调用doubao和qwen两个AI模型
2. 使用deepseek模型对两个回答进行评分
3. 返回评分最高的回答

## 配置

服务运行在8081端口，AI网关配置在application.yml中：

```yaml
server:
  port: 8081

aigateway:
  baseurl: http://localhost:8080/chat/completions
```

## API文档

### Swagger UI
启动服务后，访问以下地址查看交互式API文档：
- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **OpenAPI规范**: http://localhost:8081/v3/api-docs

Swagger UI提供了完整的API文档，包括：
- 详细的接口说明
- 请求参数示例
- 响应格式说明
- 在线测试功能

## API接口

### 获取最佳回答
`POST /api/chat/best-response`

请求体格式：
```json
{
  "messages": [
    {
      "role": "user",
      "content": "你好，请介绍一下人工智能的发展历史"
    }
  ]
}
```

响应格式：
```json
{
  "model": "doubao",
  "content": "AI模型的回答内容...",
  "score": 8.5
}
```

### 健康检查
`GET /api/chat/health`

## 启动服务

```bash
mvn spring-boot:run
```

或者：

```bash
java -jar target/FusionModel-0.0.1-SNAPSHOT.jar
```

## 测试示例

使用curl测试：

```bash
curl -X POST http://localhost:8081/api/chat/best-response \
  -H "Content-Type: application/json" \
  -d '{
    "messages": [
      {
        "role": "user",
        "content": "什么是机器学习？"
      }
    ]
  }'
```
