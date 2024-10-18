package ru.example.kafka_consumer.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.example.kafka_consumer.service.TicketService;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final TicketService ticketService;

    @KafkaListener(topics = "purchasedTickets", id = "group")
    public void consume(String message) {
        log.info("Message received: {}", message);
        ticketService.addTicket(message);
        log.info("New object added to the table");
    }

}
