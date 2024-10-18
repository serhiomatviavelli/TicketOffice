package ru.example.ticket_service.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Company {

    private Long id;
    private String title;
    private String phone;

}
