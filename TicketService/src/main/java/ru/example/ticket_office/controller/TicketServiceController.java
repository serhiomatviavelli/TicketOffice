package ru.example.ticket_office.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jooq.db.tables.records.CompanyRecord;
import jooq.db.tables.records.RouteRecord;
import jooq.db.tables.records.TicketRecord;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.example.ticket_office.dto.*;
import ru.example.ticket_office.dto.response.ResponseDto;
import ru.example.ticket_office.service.CompanyService;
import ru.example.ticket_office.service.RouteService;
import ru.example.ticket_office.service.TicketService;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name="Контроллер сервиса продажи билетов", description="Возможность получить информацию о билетах и приобрести их")
public class TicketServiceController {

    private TicketService ticketService;
    private CompanyService companyService;
    private RouteService routeService;

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/tickets")
    @Operation(
            summary = "Просмотр билетов",
            description = "Позволяет просмотреть все доступные для покупки билеты"
    )
    public ResponseEntity<?> getAvailableTickets(@RequestBody @Valid @Parameter(description = "Данные о билетах для сортировки")
                                                             AllTicketsRequestEntityDto requestEntity,
                                                 @RequestParam(required = false, name="page", defaultValue = "0") @Min(0) @Valid @Parameter(description = "Страница вывода")
                                                         int page,
                                                 @RequestParam(required = false, name="size", defaultValue = "1") @Min(0) @Valid @Parameter(description = "Размер страницы")
                                                             int size,
                                                 Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseDto("Поля заполнены с ошибками."));
        } else {
            Page<TicketDtoForEditAndDisplay> tickets = ticketService.getAvailableTickets(requestEntity, page, size);
            return ResponseEntity.ok().body(tickets);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/tickets/mine")
    @Operation(
            summary = "Билеты пользователя",
            description = "Позволяет просмотреть все билеты пользователя"
    )
    public ResponseEntity<?> getClientsTicket(Principal principal) {
        List<TicketDtoForEditAndDisplay> tickets = ticketService.getClientsTickets(principal.getName());
        return ResponseEntity.ok().body(tickets);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PutMapping("/tickets/buy/{ticketId}")
    @Operation(
            summary = "Покупка билета",
            description = "Производит покупку билета"
    )
    public ResponseEntity<?> buyTicket(@PathVariable(required = true, name="ticketId") @Parameter(description = "Идентификатор билета")
                                          Long ticketId,
                                       Principal principal) {
        TicketDtoForEditAndDisplay ticket = ticketService.buyNewTicket(principal.getName(), ticketId);
        return ticket != null ? ResponseEntity.ok().body(ticket)
                : ResponseEntity.badRequest().body(new ResponseDto("Выбран неверный билет."));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("admin/tickets/create")
    @Operation(
            summary = "Создание билета",
            description = "Позволяет пользователю с ролью администратора создать билет"
    )
    public ResponseEntity<?> createTicket(@RequestBody @Valid @Parameter(description = "Данные о билете для создания")
                                                      TicketDtoForCreation ticket, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseDto("Поля заполнены с ошибками."));
        } else {
            ticketService.addTicket(ticket);
            return ResponseEntity.ok().body(new ResponseDto("Билет успешно добавлен"));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("admin/tickets/edit")
    @Operation(
            summary = "Изменение билета",
            description = "Позволяет пользователю с ролью администратора изменить билет"
    )
    public ResponseEntity<?> editTicket(@RequestBody @Valid @Parameter(description = "Данные о билете для изменения")
                                                    TicketDtoForEditAndDisplay dto, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseDto("Поля заполнены с ошибками."));
        } else {
            TicketRecord ticket = ticketService.updateTicket(dto);
            return ticket == null ? ResponseEntity.badRequest().body(new ResponseDto("Данный билет не найден"))
                    : ResponseEntity.ok().body(new ResponseDto("Билет с id " + dto.getId() + " успешно изменен"));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("admin/tickets/delete/{id}")
    @Operation(
            summary = "Удаление билета",
            description = "Позволяет пользователю с ролью администратора удалить билет"
    )
    public ResponseEntity<?> deleteTicket(@PathVariable(required = true, name="id") @Parameter(description = "Идентификатор билета")
                                                      Long id) {
        TicketRecord ticket = ticketService.deleteTicketById(id);
        return ticket == null ? ResponseEntity.badRequest().body(new ResponseDto("Билет не найден"))
                : ResponseEntity.ok().body(new ResponseDto("Билет успешно удален"));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("admin/routes/create")
    @Operation(
            summary = "Создание маршрута",
            description = "Позволяет пользователю с ролью администратора создать маршрут"
    )
    public ResponseEntity<?> createRoute(@RequestBody @Valid @Parameter(description = "Данные о маршруте для создания")
                                                     RouteDtoForCreation route, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseDto("Поля заполнены с ошибками."));
        } else {
            routeService.addRoute(route);
            return ResponseEntity.ok().body(new ResponseDto("Маршрут успешно добавлен"));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("admin/routes/edit")
    @Operation(
            summary = "Изменение маршрута",
            description = "Позволяет пользователю с ролью администратора изменить маршрут"
    )
    public ResponseEntity<?> editRoute(@RequestBody @Valid @Parameter(description = "Данные о маршруте для изменения")
                                                   RouteDtoForEdit dto, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseDto("Поля заполнены с ошибками."));
        } else {
            RouteRecord route = routeService.updateRoute(dto);
            return route == null ? ResponseEntity.badRequest().body(new ResponseDto("Данный маршрут не найден"))
                    : ResponseEntity.ok().body(new ResponseDto("Маршрут с id " + dto.getId() + " успешно изменен"));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("admin/routes/delete/{id}")
    @Operation(
            summary = "Удаление маршрута",
            description = "Позволяет пользователю с ролью администратора удалить маршрут"
    )
    public ResponseEntity<?> deleteRoute(@PathVariable(required = true, name="id") @Parameter(description = "Идентификатор маршрута")
                                                     Long id) {
        RouteRecord route = routeService.deleteRouteById(id);
        return route == null ? ResponseEntity.badRequest().body(new ResponseDto("Маршрут не найден"))
                : ResponseEntity.ok().body(new ResponseDto("Маршрут успешно удален"));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("admin/companies/create")
    @Operation(
            summary = "Создание компании",
            description = "Позволяет пользователю с ролью администратора создать компанию"
    )
    public ResponseEntity<?> createCompany(@RequestBody @Valid @Parameter(description = "Данные о компании для создания")
                                                       CompanyDtoForCreation company, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseDto("Поля заполнены с ошибками."));
        }
        CompanyRecord record = companyService.addCompany(company);
        if (record != null) {
            return ResponseEntity.ok().body(new ResponseDto("Компания успешно добавлена"));
        } else {
            return ResponseEntity.badRequest().body(new ResponseDto("Поля заполнены с ошибками."));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("admin/companies/edit")
    @Operation(
            summary = "Изменение компании",
            description = "Позволяет пользователю с ролью администратора изменить компанию"
    )
    public ResponseEntity<?> editCompany(@RequestBody @Valid @Parameter(description = "Данные о компании для изменения") CompanyDtoForEdit dto, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseDto("Поля заполнены с ошибками."));
        } else {
            CompanyRecord company = companyService.updateCompany(dto);
            return company == null ? ResponseEntity.badRequest().body(new ResponseDto("Поля заполнены с ошибками"))
                    : ResponseEntity.ok().body(new ResponseDto("Компания с id " + dto.getId() + " успешно изменена"));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("admin/companies/delete/{id}")
    @Operation(
            summary = "Удаление компании",
            description = "Позволяет пользователю с ролью администратора удалить компанию"
    )
    public ResponseEntity<?> deleteCompany(@PathVariable(required = true, name="id") @Parameter(description = "Идентификатор компании")
                                                       Long id) {
        CompanyRecord company = companyService.deleteCompanyById(id);
        return company == null ? ResponseEntity.badRequest().body(new ResponseDto("Компания не найдена"))
                : ResponseEntity.ok().body(new ResponseDto("Компания успешно удалена"));
    }

}
