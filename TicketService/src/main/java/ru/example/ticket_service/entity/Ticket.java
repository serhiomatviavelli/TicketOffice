package ru.example.ticket_service.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Ticket {

    private UUID id;
    private Long route;
    private LocalDateTime datetime;
    private Short seat;
    private Double price;
    private Long client;

}
