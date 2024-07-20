package ru.example.ticket_office.entity;

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
