package ru.example.ticket_office.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность маршрута для изменения")
public class RouteDtoForEdit {

    @NotNull
    @Schema(description = "Идентификатор")
    private Long id;

    @NotNull
    @Schema(description = "Пункт назначения")
    private String destination;

    @NotNull
    @Schema(description = "Пункт отправления")
    private String departure;

    @NotNull
    @Schema(description = "Идентификатор компании")
    private Long company;

    @NotNull
    @Schema(description = "Длительность в минутах")
    private Integer duration;

}