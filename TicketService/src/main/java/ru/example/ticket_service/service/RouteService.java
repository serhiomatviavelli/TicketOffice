package ru.example.ticket_service.service;

import jooq.db.tables.records.RouteRecord;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.ticket_service.dto.request.RouteDtoForCreation;
import ru.example.ticket_service.dto.request.RouteDtoForEdit;
import ru.example.ticket_service.repository.RouteRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RouteService {

    private final RouteRepository repository;

    public RouteRecord deleteRouteById(Long id) {
        Optional<RouteRecord> route = repository.deleteRouteById(id);
        return route.orElse(null);
    }

    public void addRoute(RouteDtoForCreation dto) {
        repository.addRoute(dto);
    }

    public RouteRecord updateRoute(RouteDtoForEdit dto) {
        Optional<RouteRecord> route = repository.updateRoute(dto);
        return route.orElse(null);
    }

}
