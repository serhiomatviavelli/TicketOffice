package ru.example.ticket_office.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> template;

    public void sendMessage(String message) {
        template.send("purchasedTickets", message);
        log.info("Message sent: {}", message);
    }

}
