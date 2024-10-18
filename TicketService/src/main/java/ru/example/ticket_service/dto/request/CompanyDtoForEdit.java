package ru.example.ticket_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Size(min = 3)
    @Schema(description = "Название")
    private String title;

    @NotNull
    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")
    @Schema(description = "Телефон")
    private String phone;

}
