package ru.example.ticket_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.example.ticket_service.dto.request.ClientForRegistrationDto;
import ru.example.ticket_service.dto.response.AuthResponseDto;
import ru.example.ticket_service.dto.response.ResponseDto;
import ru.example.ticket_service.service.ClientService;
import ru.example.ticket_service.service.security.AuthService;
import ru.example.ticket_service.util.ErrorsHandler;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Tag(name = "Контроллер регистрации и авторизации", description = "Возможность зарегистрироваться и авторизоваться в системе")
public class AuthController {

    private final AuthService authService;
    private final ClientService clientService;

    @PostMapping("/register")
    @Operation(
            summary = "Регистрация пользователя",
            description = "Позволяет зарегистрировать пользователя"
    )
    public ResponseEntity<ResponseDto> addNewClient(@RequestBody @Valid @Parameter(description = "Данные клиента для регистрации")
                                                    ClientForRegistrationDto client, Errors errors) {
        if (errors.hasErrors()) {
            return ErrorsHandler.getResponseInCaseOfFieldsFillError(errors);
        } else {
            Long newClientId = clientService.registerNewClient(client);
            return newClientId != null ? ResponseEntity.ok().body(new ResponseDto("Клиент с id " + newClientId + " успешно зарегистрирован"))
                    : ResponseEntity.badRequest().body(new ResponseDto("Пользователь с данным логином уже зарегистрирован в системе."));
        }
    }

    @PostMapping("/sign-in")
    @Operation(
            summary = "Вход в систему",
            description = "Позволяет зарегистрированному пользователю войти в систему"
    )
    public ResponseEntity<AuthResponseDto> authenticateUser(Authentication authentication, HttpServletResponse response) {
        return ResponseEntity.ok(authService.getJwtTokensAfterAuthentication(authentication, response));
    }

    @PostMapping("/refresh-token")
    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
    @Operation(
            summary = "Получение токена доступа",
            description = "Позволяет получить токен доступа по рефреш-токену"
    )
    public ResponseEntity<?> getAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return ResponseEntity.ok(authService.getAccessTokenUsingRefreshToken(authorizationHeader));
    }

}
