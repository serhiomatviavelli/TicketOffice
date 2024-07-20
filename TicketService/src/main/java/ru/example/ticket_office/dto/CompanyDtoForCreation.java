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
@Schema(description = "Сущность компании для добавления в БД")
public class CompanyDtoForCreation {

    @NotNull
    @Schema(description = "Название компании")
    private String title;

    @NotNull
    @Schema(description = "Телефон компании")
    private String phone;

}
