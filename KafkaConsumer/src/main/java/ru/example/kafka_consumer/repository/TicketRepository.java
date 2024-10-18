package ru.example.ticket_office.repository;

import jooq.db.Tables;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.example.ticket_office.entity.Ticket;

@Repository
@RequiredArgsConstructor
public class TicketRepository {

    private final DSLContext context;

    public void addTicket(Ticket ticket) {
        context.insertInto(Tables.TICKET)
                .set(context.newRecord(Tables.TICKET, ticket))
                .returning()
                .fetchOptional();
    }

}
