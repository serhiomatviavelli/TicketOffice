package ru.example.ticket_office.repository;

import jooq.db.Tables;
import jooq.db.tables.records.ClientRecord;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.example.ticket_office.dto.ClientForRegistrationDto;
import ru.example.ticket_office.entity.Client;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class ClientRepository {

    private final DSLContext context;

    public Optional<ClientRecord> addClient(ClientForRegistrationDto client) {
        if (findByLogin(client.getLogin()).isPresent()) {
            return Optional.empty();
        } else {
            return context.insertInto(Tables.CLIENT)
                    .set(context.newRecord(Tables.CLIENT, client))
                    .set(Tables.CLIENT.PASSWORD, client.getPassword())
                    .set(Tables.CLIENT.ROLES, "ROLE_USER")
                    .returning()
                    .fetchOptional();
        }
    }

    public Optional<Client> findByLogin(String login) {
        return context.selectFrom(Tables.CLIENT)
                .where(Tables.CLIENT.LOGIN.eq(login))
                .fetchOptionalInto(Client.class);
    }

}
