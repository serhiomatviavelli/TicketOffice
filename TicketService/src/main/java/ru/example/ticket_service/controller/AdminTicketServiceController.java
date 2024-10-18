package ru.example.ticket_office.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.example.ticket_office.dto.request.*;
import ru.example.ticket_office.dto.response.ResponseDto;
import ru.example.ticket_office.service.CompanyService;
import ru.example.ticket_office.service.RouteService;
import ru.example.ticket_office.service.TicketService;
import ru.example.ticket_office.util.ErrorsHandler;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Контроллер для администратора", description = "Возможность добавлять, изменять и удалять данные из БД")
public class AdminTicketServiceController {

    private final TicketService ticketService;
    private final CompanyService companyService;
    private final RouteService routeService;

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping("tickets")
    @Operation(
            summary = "Создание билета",
            description = "Позволяет пользователю с ролью администратора создать билет"
    )
    public ResponseEntity<ResponseDto> createTicket(@RequestBody @Valid @Parameter(description = "Данные о билете для создания")
                                                    TicketDtoForCreation ticket, Errors errors) {
        if (errors.hasErrors()) {
            return ErrorsHandler.getResponseInCaseOfFieldsFillError(errors);
        } else {
            ticketService.addTicket(ticket);
            return ResponseEntity.ok().body(new ResponseDto("Билет успешно добавлен"));
        }
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PutMapping("tickets")
    @Operation(
            summary = "Изменение билета",
            description = "Позволяет пользователю с ролью администратора изменить билет"
    )
    public ResponseEntity<ResponseDto> editTicket(@RequestBody @Valid @Parameter(description = "Данные о билете для изменения")
                                                  TicketDtoForEditAndDisplay dto, Errors errors) {
        if (errors.hasErrors()) {
            return ErrorsHandler.getResponseInCaseOfFieldsFillError(errors);
        } else {
            jooq.db.tables.records.TicketRecord ticket = ticketService.updateTicket(dto);
            return ticket == null ? ResponseEntity.badRequest().body(new ResponseDto("Данный билет не найден"))
                    : ResponseEntity.ok().body(new ResponseDto("Билет с id " + dto.getId() + " успешно изменен"));
        }
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('SCOPE_DELETE')")
    @DeleteMapping("tickets/{id}")
    @Operation(
            summary = "Удаление билета",
            description = "Позволяет пользователю с ролью администратора удалить билет"
    )
    public ResponseEntity<ResponseDto> deleteTicket(@PathVariable(required = true, name = "id") @Parameter(description = "Идентификатор билета")
                                                    Long id) {
        jooq.db.tables.records.TicketRecord ticket = ticketService.deleteTicketById(id);
        return ticket == null ? ResponseEntity.badRequest().body(new ResponseDto("Билет не найден"))
                : ResponseEntity.ok().body(new ResponseDto("Билет успешно удален"));
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping("routes")
    @Operation(
            summary = "Создание маршрута",
            description = "Позволяет пользователю с ролью администратора создать маршрут"
    )
    public ResponseEntity<ResponseDto> createRoute(@RequestBody @Valid @Parameter(description = "Данные о маршруте для создания")
                                                   RouteDtoForCreation route, Errors errors) {
        if (errors.hasErrors()) {
            return ErrorsHandler.getResponseInCaseOfFieldsFillError(errors);
        } else {
            routeService.addRoute(route);
            return ResponseEntity.ok().body(new ResponseDto("Маршрут успешно добавлен"));
        }
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PutMapping("routes")
    @Operation(
            summary = "Изменение маршрута",
            description = "Позволяет пользователю с ролью администратора изменить маршрут"
    )
    public ResponseEntity<ResponseDto> editRoute(@RequestBody @Valid @Parameter(description = "Данные о маршруте для изменения")
                                                 RouteDtoForEdit dto, Errors errors) {
        if (errors.hasErrors()) {
            return ErrorsHandler.getResponseInCaseOfFieldsFillError(errors);
        } else {
            jooq.db.tables.records.RouteRecord route = routeService.updateRoute(dto);
            return route == null ? ResponseEntity.badRequest().body(new ResponseDto("Данный маршрут не найден"))
                    : ResponseEntity.ok().body(new ResponseDto("Маршрут с id " + dto.getId() + " успешно изменен"));
        }
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('SCOPE_DELETE')")
    @DeleteMapping("routes/{id}")
    @Operation(
            summary = "Удаление маршрута",
            description = "Позволяет пользователю с ролью администратора удалить маршрут"
    )
    public ResponseEntity<ResponseDto> deleteRoute(@PathVariable(required = true, name = "id") @Parameter(description = "Идентификатор маршрута")
                                                   Long id) {
        jooq.db.tables.records.RouteRecord route = routeService.deleteRouteById(id);
        return route == null ? ResponseEntity.badRequest().body(new ResponseDto("Маршрут не найден"))
                : ResponseEntity.ok().body(new ResponseDto("Маршрут успешно удален"));
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping("companies")
    @Operation(
            summary = "Создание компании",
            description = "Позволяет пользователю с ролью администратора создать компанию"
    )
    public ResponseEntity<ResponseDto> createCompany(@RequestBody @Valid @Parameter(description = "Данные о компании для создания")
                                                     CompanyDtoForCreation company, Errors errors) {
        if (errors.hasErrors()) {
            return ErrorsHandler.getResponseInCaseOfFieldsFillError(errors);
        } else {
            jooq.db.tables.records.CompanyRecord record = companyService.addCompany(company);
            return ResponseEntity.ok().body(new ResponseDto("Компания " + record.getTitle() + " успешно добавлена"));
        }
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PutMapping("companies")
    @Operation(
            summary = "Изменение компании",
            description = "Позволяет пользователю с ролью администратора изменить компанию"
    )
    public ResponseEntity<ResponseDto> editCompany(@RequestBody @Valid @Parameter(description = "Данные о компании для изменения") CompanyDtoForEdit dto, Errors errors) {
        if (errors.hasErrors()) {
            return ErrorsHandler.getResponseInCaseOfFieldsFillError(errors);
        } else {
            jooq.db.tables.records.CompanyRecord company = companyService.updateCompany(dto);
            return company == null ? ResponseEntity.badRequest().body(new ResponseDto("Компания не найдена")) :
                    ResponseEntity.ok().body(new ResponseDto("Компания с id " + dto.getId() + " успешно изменена"));
        }
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('SCOPE_DELETE')")
    @DeleteMapping("companies/{id}")
    @Operation(
            summary = "Удаление компании",
            description = "Позволяет пользователю с ролью администратора удалить компанию"
    )
    public ResponseEntity<ResponseDto> deleteCompany(@PathVariable(required = true, name = "id") @Parameter(description = "Идентификатор компании")
                                                     Long id) {
        jooq.db.tables.records.CompanyRecord company = companyService.deleteCompanyById(id);
        return company == null ? ResponseEntity.badRequest().body(new ResponseDto("Компания не найдена"))
                : ResponseEntity.ok().body(new ResponseDto("Компания успешно удалена"));
    }


}
