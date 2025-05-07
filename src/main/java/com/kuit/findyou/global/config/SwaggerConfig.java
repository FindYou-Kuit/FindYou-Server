package com.kuit.findyou.global.config;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .servers(List.of(new Server().url("https://findyou.store")
                        .description("운영 서버")))
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Findyou Swagger")
                .description("Findyou의 REST API")
                .version("1.0.0");
    }
}
