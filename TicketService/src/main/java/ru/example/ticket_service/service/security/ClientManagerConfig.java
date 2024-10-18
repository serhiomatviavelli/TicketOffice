package ru.example.ticket_service.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.example.ticket_service.repository.ClientRepository;

@Service
@RequiredArgsConstructor
public class ClientManagerConfig implements UserDetailsService {

    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return clientRepository
                .findByLogin(login)
                .map(ClientConfig::new)
                .orElseThrow(() -> new UsernameNotFoundException("Login: " + login + " does not exist"));
    }

}
