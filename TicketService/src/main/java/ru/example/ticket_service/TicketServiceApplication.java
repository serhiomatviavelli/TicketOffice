package ru.example.ticket_office;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import ru.example.ticket_office.config.RSAKeyRecord;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(RSAKeyRecord.class)
public class TicketServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketServiceApplication.class, args);
    }
}
