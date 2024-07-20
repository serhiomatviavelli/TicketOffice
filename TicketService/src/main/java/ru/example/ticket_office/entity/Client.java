package ru.example.ticket_office.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    private Long id;
    private String login;
    private String password;
    private String fullname;
    private String roles;

}
