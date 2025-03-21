package com.example.franchise_challenge.infrastructure.input.documentation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de franquicias")
                        .version("1.0")
                        .description("Documentación de los endpoints de la api de franquicias")
                        .contact(new Contact()
                                .name("Sayira Elittzi Quirama Arrubla")
                                .email("sayira.quirama@pragma.com.co")
                        )
                );
    }
}
