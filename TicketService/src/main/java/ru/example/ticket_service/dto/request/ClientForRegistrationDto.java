package ru.example.ticket_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Сущность клиента для регистрации")
public class ClientForRegistrationDto {

    @NotNull
    @Size(min = 3)
    @Schema(description = "Логин")
    private String login;

    @NotNull
    @Size(min = 4)
    @Schema(description = "Пароль")
    private String password;

    @NotNull
    @Pattern(regexp = "^[a-zA-Zа-яёА-ЯЁ]+\\s[a-zA-Zа-яёА-ЯЁ]+(\\s[a-zA-Zа-яёА-ЯЁ]+)?$")
    @Schema(description = "ФИО клиента")
    private String fullname;

    @Nullable
    @Schema(description = "Роли пользователя")
    private String roles;

}
