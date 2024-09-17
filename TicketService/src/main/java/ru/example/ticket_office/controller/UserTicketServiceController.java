package ru.example.ticket_office.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.example.ticket_office.dto.request.AllTicketsRequestEntityDto;
import ru.example.ticket_office.dto.request.TicketDtoForEditAndDisplay;
import ru.example.ticket_office.dto.response.ResponseDto;
import ru.example.ticket_office.service.TicketService;
import ru.example.ticket_office.util.ErrorsHandler;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Tag(name = "Контроллер сервиса продажи билетов", description = "Возможность получить информацию о билетах и приобрести их")
public class UserTicketServiceController {

    private final TicketService ticketService;

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/tickets")
    @Operation(
            summary = "Просмотр билетов",
            description = "Позволяет просмотреть все доступные для покупки билеты"
    )
    public ResponseEntity<ResponseDto> getAvailableTickets(@RequestBody @Valid @Parameter(description = "Данные о билетах для сортировки")
                                                           AllTicketsRequestEntityDto requestEntity,
                                                           Errors errors,
                                                           @RequestParam(required = false, name = "page", defaultValue = "0") @Min(0) @Valid @Parameter(description = "Страница вывода")
                                                           int page,
                                                           @RequestParam(required = false, name = "size", defaultValue = "1") @Min(0) @Valid @Parameter(description = "Размер страницы")
                                                           int size) {
        if (errors.hasErrors()) {
            return ErrorsHandler.getResponseInCaseOfFieldsFillError(errors);
        } else {
            Page<TicketDtoForEditAndDisplay> tickets = ticketService.getAvailableTickets(requestEntity, page, size);
            return ResponseEntity.ok().body(new ResponseDto("Доступные билеты: " + tickets));
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/tickets/mine")
    @Operation(
            summary = "Билеты пользователя",
            description = "Позволяет просмотреть все билеты пользователя"
    )
    public ResponseEntity<ResponseDto> getClientsTicket(Principal principal) {
        List<TicketDtoForEditAndDisplay> tickets = ticketService.getClientsTickets(principal.getName());
        return ResponseEntity.ok().body(new ResponseDto("Мои билеты: " + tickets.toString()));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PutMapping("/tickets/buy/{ticketId}")
    @Operation(
            summary = "Покупка билета",
            description = "Производит покупку билета"
    )
    public ResponseEntity<ResponseDto> buyTicket(@PathVariable(required = true, name = "ticketId") @Parameter(description = "Идентификатор билета")
                                                 Long ticketId,
                                                 Principal principal) {
        TicketDtoForEditAndDisplay ticket = ticketService.sellTicketAndSendKafkaMessage(principal.getName(), ticketId);
        return ticket != null ? ResponseEntity.ok().body(new ResponseDto("Куплен новый билет"))
                : ResponseEntity.badRequest().body(new ResponseDto("Выбран неверный билет"));
    }

}
