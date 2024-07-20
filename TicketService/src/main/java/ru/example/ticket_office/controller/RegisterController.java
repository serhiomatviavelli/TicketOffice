package ru.example.ticket_office.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.ticket_office.dto.ClientForRegistrationDto;
import ru.example.ticket_office.dto.response.ResponseDto;
import ru.example.ticket_office.service.ClientService;

@AllArgsConstructor
@RestController
@RequestMapping("/register")
@Tag(name="Контроллер регистрации новых клиентов", description="Возможность зарегистрироваться в системе")

public class RegisterController {

    private final ClientService clientService;

    @PostMapping
    @Operation(
            summary = "Регистрация пользователя",
            description = "Позволяет зарегистрировать пользователя"
    )
    public ResponseEntity<?> addNewClient(@RequestBody @Valid @Parameter(description = "Данные клиента для регистрации")
                                                  ClientForRegistrationDto client, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseDto("Не все поля были заполнены."));
        } else {
            Long newClientId = clientService.registerNewClient(client);
            return newClientId != null ? ResponseEntity.ok().body(newClientId)
                    : ResponseEntity.badRequest().body(new ResponseDto("Пользователь с данным логином уже зарегистрирован в системе."));
        }
    }

}
