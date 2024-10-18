package ru.example.ticket_service.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto {

    private final String description;

    public ResponseDto(String description) {
        this.description = description;
    }
}
