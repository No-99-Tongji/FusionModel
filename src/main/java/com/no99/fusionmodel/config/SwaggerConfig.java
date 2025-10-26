package com.no99.fusionmodel.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port:8081}")
    private String serverPort;

    @Value("${swagger.server.url:}")
    private String swaggerServerUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        Server server;
        if (swaggerServerUrl != null && !swaggerServerUrl.isEmpty()) {
            // 如果配置了swagger.server.url，使用配置的URL
            server = new Server().url(swaggerServerUrl).description("当前环境");
        } else {
            // 否则使用localhost
            server = new Server().url("http://localhost:" + serverPort).description("本地开发环境");
        }

        return new OpenAPI()
                .info(new Info()
                        .title("FusionModel API")
                        .description("AI模型融合微服务，并行调用多个AI模型并智能评分返回最佳回答")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("FusionModel Team")
                                .email("support@fusionmodel.com")))
                .servers(List.of(server));
    }
}
