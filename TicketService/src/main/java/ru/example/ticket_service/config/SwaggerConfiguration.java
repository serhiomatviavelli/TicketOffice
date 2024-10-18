package ru.example.ticket_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Ticket Office",
                description = "Сервис продажи билетов", version = "1.0.0",
                contact = @Contact(
                        name = "Сергей Матвеев",
                        email = "serhiomatviavelli@gmail.com"
                )
        )
)
@Configuration
public class SwaggerConfiguration {
}
