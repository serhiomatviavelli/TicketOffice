package ru.example.ticket_service.repository;

import jooq.db.Tables;
import jooq.db.tables.records.RouteRecord;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.example.ticket_service.dto.request.RouteDtoForCreation;
import ru.example.ticket_service.dto.request.RouteDtoForEdit;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class RouteRepository {

    private final DSLContext context;

    public Optional<RouteRecord> deleteRouteById(Long id) {
        return context.deleteFrom(Tables.ROUTE).where(Tables.ROUTE.ID.eq(id))
                .returning().fetchOptional();
    }

    public void addRoute(RouteDtoForCreation route) {
        context.insertInto(Tables.ROUTE)
                .set(context.newRecord(Tables.ROUTE, route))
                .returning()
                .fetchOptional();
    }

    public Optional<RouteRecord> updateRoute(RouteDtoForEdit route) {
        return context.update(Tables.ROUTE)
                .set(Tables.ROUTE.DESTINATION, route.getDestination())
                .set(Tables.ROUTE.DEPARTURE, route.getDeparture())
                .set(Tables.ROUTE.COMPANY, route.getCompany())
                .set(Tables.ROUTE.DURATION, route.getDuration())
                .where(Tables.ROUTE.ID.eq(route.getId()))
                .returning().fetchOptional();
    }

    public RouteRecord getRouteById(Long id) {
        return context.selectFrom(Tables.ROUTE).where(Tables.ROUTE.ID.eq(id)).fetchAny();
    }

}
