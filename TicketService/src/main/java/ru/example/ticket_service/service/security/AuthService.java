package ru.example.ticket_service.service.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.example.ticket_service.config.security.jwtConfig.JwtTokenGenerator;
import ru.example.ticket_service.dto.TokenType;
import ru.example.ticket_service.dto.response.AuthResponseDto;
import ru.example.ticket_service.entity.Client;
import ru.example.ticket_service.repository.ClientRepository;
import ru.example.ticket_service.repository.RefreshTokenRepository;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final ClientRepository clientRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthResponseDto getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response) {
        try {
            Client client = clientRepository.findByLogin(authentication.getName())
                    .orElseThrow(() -> {
                        log.error("[AuthService:userSignInAuth] User :{} not found", authentication.getName());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND ");
                    });


            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            saveRefreshToken(client, refreshToken);

            creatRefreshTokenCookie(response, refreshToken);

            log.info("[AuthService:userSignInAuth] Access token for user:{}, has been generated", client.getLogin());
            return AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(15 * 60)
                    .userName(client.getLogin())
                    .tokenType(TokenType.BEARER)
                    .build();


        } catch (Exception e) {
            log.error("[AuthService:userSignInAuth]Exception while authenticating the user due to :{}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please Try Again");
        }
    }

    private void saveRefreshToken(Client client, String refreshToken) {
        refreshTokenRepository.saveToken(client.getId(), refreshToken);
    }

    private Cookie creatRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60); // in seconds
        response.addCookie(refreshTokenCookie);
        return refreshTokenCookie;
    }

    public Object getAccessTokenUsingRefreshToken(String authorizationHeader) {
        if (!authorizationHeader.startsWith(TokenType.BEARER.getAlias())) {
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please verify your token type");
        }

        final String refreshToken = authorizationHeader.substring(7);

        var refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .filter(tokens -> !tokens.getRevoked())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Refresh token revoked"));

        Long clientId = refreshTokenEntity.getClient();
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException(String.format("Couldn't find user with id %d", clientId)));

        Authentication authentication = createAuthenticationObject(client);

        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(5 * 60)
                .userName(client.getLogin())
                .tokenType(TokenType.BEARER)
                .build();
    }

    private static Authentication createAuthenticationObject(Client client) {
        String username = client.getLogin();
        String password = client.getPassword();
        String roles = client.getRoles();

        String[] roleArray = roles.split(",");
        GrantedAuthority[] authorities = Arrays.stream(roleArray)
                .map(role -> (GrantedAuthority) role::trim)
                .toArray(GrantedAuthority[]::new);

        return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(authorities));
    }
}