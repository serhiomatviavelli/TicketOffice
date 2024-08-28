package ru.example.ticket_office.util;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import ru.example.ticket_office.dto.response.ResponseDto;

import java.util.List;

public class ErrorsHandler {

    public static ResponseEntity<ResponseDto> getResponseInCaseOfFieldsFillError(Errors errors) {
        List<FieldError> errorsList = errors.getFieldErrors();
        String errorsAsString = getErrors(errorsList);
        return ResponseEntity.badRequest().body(new ResponseDto("Имеются ошибки заполнения в следующих полях: " + errorsAsString));
    }

    private static String getErrors(List<FieldError> errors) {
        StringBuilder sb = new StringBuilder();
        errors.forEach(error -> sb.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(", "));
        sb.deleteCharAt(sb.length() - 2);
        return sb.toString().trim();
    }

}
