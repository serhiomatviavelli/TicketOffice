package ru.example.ticket_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @Min(10)
    @Max(10000)
    @Schema(description = "Цена")
    private BigDecimal price;

}
