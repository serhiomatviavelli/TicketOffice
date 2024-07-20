package ru.example.ticket_office.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Сущность клиента для регистрации")
public class ClientForRegistrationDto {

    @NotNull
    @Schema(description = "Логин")
    private String login;

    @NotNull
    @Schema(description = "Пароль")
    private String password;

    @NotNull
    @Schema(description = "ФИО клиента")
    private String fullname;

    @Nullable
    @Schema(description = "Роли пользователя")
    private String roles;

}
