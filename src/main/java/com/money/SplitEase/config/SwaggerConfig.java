package com.money.SplitEase.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI splitKroOpenAPI() {
        var info = new Info()
                .title("SplitKro API")
                .description("API for SplitKro expense sharing application")
                .version("v0.0.1");

        var securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic");

        var components = new Components()
                .addSecuritySchemes("basicAuth", securityScheme);

        var server = new Server()
                .url("http://localhost:8080")
                .description("Local dev server");

        return new OpenAPI()
                .info(info)
                .components(components)
                .servers(List.of(server));
    }
}
