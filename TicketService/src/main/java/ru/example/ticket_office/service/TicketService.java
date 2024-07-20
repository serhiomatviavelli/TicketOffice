package ru.example.ticket_office.service;

import jooq.db.tables.records.TicketRecord;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.example.ticket_office.dto.AllTicketsRequestEntityDto;
import ru.example.ticket_office.dto.TicketDtoForCreation;
import ru.example.ticket_office.dto.TicketDtoForEditAndDisplay;
import ru.example.ticket_office.repository.TicketRepository;
import ru.example.ticket_office.util.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final Mapper mapper;

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
        return ticket.isEmpty() ? null
                : ticket.get();
    }

    public void addTicket(TicketDtoForCreation dto) {
        ticketRepository.addTicket(dto);
    }

    public TicketRecord updateTicket(TicketDtoForEditAndDisplay dto) {
        Optional<TicketRecord> ticket = ticketRepository.updateTicket(dto);
        return ticket.isEmpty() ? null
                : ticket.get();
    }

}
