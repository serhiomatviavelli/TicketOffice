package ru.example.ticket_service.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Route {

    private Long id;
    private String destination;
    private String departure;
    private Long company;
    private Integer duration;

}
