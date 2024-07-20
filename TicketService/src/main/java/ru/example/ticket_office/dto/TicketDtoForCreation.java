package ru.example.ticket_office.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Сущность билета для добавления в БД")
public class TicketDtoForCreation {

    @NotNull
    @Schema(description = "Идентификатор маршрута")
    private Long route;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(description = "Время отправления")
    private LocalDateTime datetime;

    @NotNull
    @Schema(description = "Место")
    private Short seat;

    @NotNull
    @Schema(description = "Цена")
    private BigDecimal price;

}
