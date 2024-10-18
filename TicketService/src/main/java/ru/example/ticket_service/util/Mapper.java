package ru.example.ticket_service.util;

import jooq.db.tables.records.TicketRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.example.ticket_service.dto.request.TicketDtoForEditAndDisplay;

@Component
@RequiredArgsConstructor
public class Mapper {

    public TicketDtoForEditAndDisplay ticketRecordToDto(TicketRecord record) {
        return record == null ? null
                : TicketDtoForEditAndDisplay.builder()
                .id(record.getId())
                .route(record.getRoute())
                .datetime(record.getDatetime())
                .seat(record.getSeat())
                .price(record.getPrice())
                .build();
    }

}
