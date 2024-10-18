package ru.example.ticket_office.service;

import jooq.db.tables.records.ClientRecord;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.example.ticket_office.dto.request.ClientForRegistrationDto;
import ru.example.ticket_office.repository.ClientRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientService {

    private final ClientRepository repository;
    private final PasswordEncoder encoder;

    public Long registerNewClient(ClientForRegistrationDto client) {
        client.setPassword(encoder.encode(client.getPassword()));
        Optional<ClientRecord> clientRecord = repository.addClient(client);
        return clientRecord.map(ClientRecord::getId).orElse(null);
    }

}
