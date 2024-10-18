package ru.example.kafka_consumer.repository;

import jooq.db.Tables;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.example.kafka_consumer.entity.Ticket;

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
