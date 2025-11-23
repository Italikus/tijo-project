package com.example.tijo.backend.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TiJO - Car & Owner Management API")
                        .version("1.0.0")
                        .description("""
                                REST API do zarządzania pojazdami i ich właścicielami.
                                
                                Aplikacja umożliwia:
                                - Zarządzanie pojazdami (CRUD operations)
                                - Zarządzanie właścicielami (CRUD operations)
                                - Przypisywanie pojazdów do właścicieli
                                - Walidację danych wejściowych (PESEL, VIN)
                                
                                Technologie: Spring Boot 3.5.7, Java 21, H2 Database
                                """)
                        .contact(new Contact()
                                .name("Bartłomiej Podlewski")
                                .email("bartlomiej.podlewski@example.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development server")
                ));
    }
}
