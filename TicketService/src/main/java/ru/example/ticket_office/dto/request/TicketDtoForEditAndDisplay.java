package ru.example.ticket_office.dto.request;

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
@ToString
@Schema(description = "Сущность билета для изменения")
public class TicketDtoForEditAndDisplay {

    @NotNull
    @Schema(description = "Идентификатор")
    private Long id;

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
