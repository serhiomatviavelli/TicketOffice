package ru.example.ticket_office.service;

import jooq.db.tables.records.ClientRecord;
import jooq.db.tables.records.RouteRecord;
import jooq.db.tables.records.TicketRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.example.ticket_office.dto.request.AllTicketsRequestEntityDto;
import ru.example.ticket_office.dto.request.TicketDtoForCreation;
import ru.example.ticket_office.dto.request.TicketDtoForEditAndDisplay;
import ru.example.ticket_office.kafka.KafkaProducer;
import ru.example.ticket_office.repository.ClientRepository;
import ru.example.ticket_office.repository.RouteRepository;
import ru.example.ticket_office.repository.TicketRepository;
import ru.example.ticket_office.util.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final RouteRepository routeRepository;
    private final ClientRepository clientRepository;
    private final KafkaProducer kafkaProducer;
    private final Mapper mapper;

    private static final String JSON_OBJECT_FOR_RECEIVE = "{\"id\": \"%s\", \"route\": \"%s\", \"datetime\": \"%s\", \"seat\": \"%s\", \"price\": \"%s\", \"client\": \"%s\"}";

    public Page<TicketDtoForEditAndDisplay> getAvailableTickets(AllTicketsRequestEntityDto requestEntity, int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        List<TicketDtoForEditAndDisplay> tickets = getDtoList(requestEntity);

        int listSize = tickets.size();
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), listSize);

        try {
            List<TicketDtoForEditAndDisplay> pageContent = tickets.subList(start, end);
            return new PageImpl<>(pageContent, pageRequest, listSize);
        } catch (IllegalArgumentException e) {
            return new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 1), 0);
        }
    }

    public List<TicketDtoForEditAndDisplay> getClientsTickets(String login) {
        List<TicketRecord> records = ticketRepository.getClientsTickets(login);
        return records.stream().map(mapper::ticketRecordToDto).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public TicketDtoForEditAndDisplay buyNewTicket(String login, Long ticketId) {
        TicketRecord record = ticketRepository.buyTicket(login, ticketId);
        return mapper.ticketRecordToDto(record);
    }

    public List<TicketDtoForEditAndDisplay> getDtoList(AllTicketsRequestEntityDto requestEntity) {
        return new ArrayList<>(ticketRepository.getAvailableTickets(requestEntity.getDateFrom(), requestEntity.getDateTo(),
                requestEntity.getDeparture(), requestEntity.getDestination(),
                requestEntity.getCompany()));
    }

    public TicketRecord deleteTicketById(Long id) {
        Optional<TicketRecord> ticket = ticketRepository.deleteTicketById(id);
        return ticket.orElse(null);
    }

    public void addTicket(TicketDtoForCreation dto) {
        ticketRepository.addTicket(dto);
    }

    public TicketRecord updateTicket(TicketDtoForEditAndDisplay dto) {
        Optional<TicketRecord> ticket = ticketRepository.updateTicket(dto);
        return ticket.orElse(null);
    }

    public TicketDtoForEditAndDisplay sellTicketAndSendKafkaMessage(String login, Long ticketId) {
        TicketDtoForEditAndDisplay ticket = buyNewTicket(login, ticketId);
        if (ticket != null) {
            RouteRecord routeRecord = routeRepository.getRouteById(ticket.getRoute());
            ClientRecord client = clientRepository.getClientByLogin(login);
            String message = String.format(JSON_OBJECT_FOR_RECEIVE,
                    ticket.getId(),
                    routeRecord.getDeparture() + " -> " + routeRecord.getDestination(),
                    ticket.getDatetime(),
                    ticket.getSeat(),
                    ticket.getPrice(),
                    client.getFullname());
            log.info("Ticket with id: {} was purchased by client: {}", ticket.getId(), client.getFullname());
            kafkaProducer.sendMessage(message);
            return ticket;
        }
        return null;
    }

}
