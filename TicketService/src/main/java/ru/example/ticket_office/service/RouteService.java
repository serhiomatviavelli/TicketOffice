package ru.example.ticket_office.service;

import jooq.db.tables.records.RouteRecord;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.ticket_office.dto.RouteDtoForCreation;
import ru.example.ticket_office.dto.RouteDtoForEdit;
import ru.example.ticket_office.repository.RouteRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RouteService {

    private final RouteRepository repository;

    public RouteRecord deleteRouteById(Long id) {
        Optional<RouteRecord> route = repository.deleteRouteById(id);
        return route.isEmpty() ? null
                : route.get();
    }

    public void addRoute(RouteDtoForCreation dto) {
        repository.addRoute(dto);
    }

    public RouteRecord updateRoute(RouteDtoForEdit dto) {
        Optional<RouteRecord> route = repository.updateRoute(dto);
        return route.isEmpty() ? null
                : route.get();
    }

}
