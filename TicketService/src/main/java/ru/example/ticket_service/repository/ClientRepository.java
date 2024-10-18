package ru.example.ticket_service.repository;

import jooq.db.Tables;
import jooq.db.tables.records.ClientRecord;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.example.ticket_service.dto.request.ClientForRegistrationDto;
import ru.example.ticket_service.entity.Client;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class ClientRepository {

    private final DSLContext context;

    @Transactional
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

    public ClientRecord getClientByLogin(String login) {
        return context.selectFrom(Tables.CLIENT).where(Tables.CLIENT.LOGIN.eq(login)).fetchAny();
    }

    public Optional<Client> findById(Long clientId) {
        return context.selectFrom(Tables.CLIENT).where(Tables.CLIENT.ID.eq(clientId))
                .fetchOptionalInto(Client.class);
    }
}
