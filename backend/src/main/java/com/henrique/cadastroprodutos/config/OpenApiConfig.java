package com.henrique.cadastroprodutos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("Cadastro de Produtos API")
                .version("1.0.0")
                .description("API REST para cadastro de produtos físicos e digitais. "
                        + "Evolução do sistema desktop original (Java Swing) para uma arquitetura web."));
    }
}
