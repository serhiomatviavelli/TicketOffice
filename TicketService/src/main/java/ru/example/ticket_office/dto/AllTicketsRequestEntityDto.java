package ru.example.ticket_office.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Параметры билета для поиска")
public class AllTicketsRequestEntityDto {

    @Nullable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(description = "Дата/Время от...")
    private LocalDateTime dateFrom;

    @Nullable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(description = "Дата/Время по...")
    private LocalDateTime dateTo;

    @Nullable
    @Schema(description = "Пункт отправления")
    private String departure;

    @Nullable
    @Schema(description = "Пункт назначения")
    public String destination;

    @Nullable
    @Schema(description = "Компания - перевозчик")
    private String company;

}
