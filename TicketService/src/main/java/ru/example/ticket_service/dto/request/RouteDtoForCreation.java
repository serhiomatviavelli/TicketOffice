package ru.example.ticket_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность маршрута для добавления в БД")
public class RouteDtoForCreation {

    @NotNull
    @Size(min = 3)
    @Schema(description = "Пункт назначения")
    private String destination;

    @NotNull
    @Size(min = 3)
    @Schema(description = "Пункт отправления")
    private String departure;

    @NotNull
    @Size(min = 3)
    @Schema(description = "Идентификатор компании")
    private Long company;

    @NotNull
    @Schema(description = "Длительность в минутах")
    private Integer duration;

}
