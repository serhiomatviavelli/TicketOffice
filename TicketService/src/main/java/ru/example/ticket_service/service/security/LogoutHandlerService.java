package ru.example.ticket_service.service.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import ru.example.ticket_service.dto.TokenType;
import ru.example.ticket_service.repository.RefreshTokenRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogoutHandlerService implements LogoutHandler {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!authHeader.startsWith(TokenType.BEARER.getAlias())) {
            return;
        }

        final String refreshToken = authHeader.substring(7);

        refreshTokenRepository.findByToken(refreshToken)
                .map(token -> {
                    refreshTokenRepository.setRevoked(token);
                    return token;
                });

    }
}
