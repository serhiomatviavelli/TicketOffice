package ru.example.ticket_service.dto;

import lombok.Getter;

@Getter
public enum TokenType {

    BEARER("Bearer");

    TokenType(String alias) {
        this.alias = alias;
    }

    private final String alias;

}
