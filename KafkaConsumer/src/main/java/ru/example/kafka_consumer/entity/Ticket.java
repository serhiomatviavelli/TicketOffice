package ru.example.ticket_office.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    private Long id;
    private String route;
    private LocalDateTime datetime;
    private Short seat;
    private Double price;
    private String client;

}
