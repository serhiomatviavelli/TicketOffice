package ru.example.ticket_office.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.example.ticket_office.entity.Ticket;
import ru.example.ticket_office.repository.TicketRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository repository;

    public void addTicket(String ticketAsJson) {
        Ticket ticket = getTicketFromString(ticketAsJson);
        repository.addTicket(ticket);
    }

    private Ticket getTicketFromString(String json) {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        try {
            return mapper.readValue(json, Ticket.class);
        } catch (JsonProcessingException e) {
            log.error("Error during mapping json to ticket object");
            throw new RuntimeException(e);
        }
    }

}
