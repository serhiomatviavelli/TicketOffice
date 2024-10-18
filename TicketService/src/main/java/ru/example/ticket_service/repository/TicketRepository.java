package ru.example.ticket_service.repository;

import jooq.db.Tables;
import jooq.db.tables.records.TicketRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.example.ticket_service.dto.request.TicketDtoForCreation;
import ru.example.ticket_service.dto.request.TicketDtoForEditAndDisplay;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.jooq.impl.DSL.asterisk;

@Repository
@RequiredArgsConstructor
public class TicketRepository {

    private final DSLContext context;

    public List<TicketDtoForEditAndDisplay> getAvailableTickets(LocalDateTime from, LocalDateTime to,
                                                                String departure, String destination, String company) {
        return context.select(Tables.TICKET.ID, Tables.TICKET.ROUTE, Tables.TICKET.DATETIME,
                        Tables.TICKET.SEAT, Tables.TICKET.PRICE)
                .from(Tables.TICKET
                        .leftJoin(Tables.ROUTE).on(Tables.TICKET.ROUTE.eq(Tables.ROUTE.ID))
                        .leftJoin(Tables.COMPANY).on(Tables.ROUTE.COMPANY.eq(Tables.COMPANY.ID)))
                .where(getTicketsCondition(from, to, departure, destination, company))
                .fetchInto(TicketDtoForEditAndDisplay.class);
    }

    public List<TicketRecord> getClientsTickets(String login) {
        return context.select(asterisk()).from(Tables.TICKET)
                .join(Tables.CLIENT).on(Tables.TICKET.CLIENT.eq(Tables.CLIENT.ID))
                .where(Tables.CLIENT.LOGIN.eq(login))
                .fetchInto(TicketRecord.class);
    }

    @Transactional
    public TicketRecord buyTicket(String login, Long ticketId) {
        Long clientId = Objects.requireNonNull(context.select(Tables.CLIENT.ID).from(Tables.CLIENT)
                .where(Tables.CLIENT.LOGIN.eq(login)).fetchAny()).value1();
        return context.update(Tables.TICKET)
                .set(Tables.TICKET.CLIENT, clientId)
                .where(Tables.TICKET.ID.eq(ticketId))
                .and(Tables.TICKET.CLIENT.isNull())
                .and(Tables.TICKET.DATETIME.greaterThan(LocalDateTime.now()))
                .returning().fetchOne();
    }

    public Condition getTicketsCondition(LocalDateTime from, LocalDateTime to,
                                         String departure, String destination, String company) {
        Condition condition = Tables.TICKET.CLIENT.isNull();

        if (from != null) {
            condition = condition.and(Tables.TICKET.DATETIME.greaterThan(from));
        }
        if (to != null) {
            condition = condition.and(Tables.TICKET.DATETIME.lessOrEqual(to));
        }
        if (departure != null) {
            condition = condition.and(Tables.ROUTE.DEPARTURE.eq(departure));
        }
        if (destination != null) {
            condition = condition.and(Tables.ROUTE.DESTINATION.eq(destination));
        }
        if (company != null) {
            condition = condition.and(Tables.COMPANY.TITLE.eq(company));
        }

        return condition;
    }

    public Optional<TicketRecord> deleteTicketById(Long id) {
        return context.deleteFrom(Tables.TICKET).where(Tables.TICKET.ID.eq(id))
                .returning().fetchOptional();
    }

    public Optional<TicketRecord> addTicket(TicketDtoForCreation ticket) {
        return context.insertInto(Tables.TICKET)
                .set(context.newRecord(Tables.TICKET, ticket))
                .returning()
                .fetchOptional();
    }

    public Optional<TicketRecord> updateTicket(TicketDtoForEditAndDisplay dto) {
        return context.update(Tables.TICKET)
                .set(Tables.TICKET.ROUTE, dto.getRoute())
                .set(Tables.TICKET.DATETIME, dto.getDatetime())
                .set(Tables.TICKET.SEAT, dto.getSeat())
                .set(Tables.TICKET.PRICE, dto.getPrice())
                .where(Tables.TICKET.ID.eq(dto.getId()))
                .returning().fetchOptional();
    }

}
