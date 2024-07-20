package ru.example.ticket_office.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность компании-перевозчика для редактирования")
public class CompanyDtoForEdit {

    @NotNull
    @Schema(description = "Идентификатор")
    private Long id;

    @NotNull
    @Schema(description = "Название")
    private String title;

    @NotNull
    @Schema(description = "Телефон")
    private String phone;

}
