package com.no99.fusionmodel.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FusionModel API")
                        .description("AI模型融合微服务，并行调用多个AI模型并智能评分返回最佳回答")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("FusionModel Team")
                                .email("support@fusionmodel.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("本地开发环境")
                ));
    }
}
