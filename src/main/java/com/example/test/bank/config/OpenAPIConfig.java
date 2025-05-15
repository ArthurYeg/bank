package com.example.test.bank.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bank Card Management API")
                        .version("1.0.0")
                        .description("API for managing bank cards, including creating, viewing, and transferring funds.")
                        .contact(new Contact()
                                .name("Support Team")
                                .url("http://example.com/support")
                                .email("support@example.com")));
    }
}
