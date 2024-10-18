package ru.example.ticket_service.repository;

import jooq.db.Tables;
import jooq.db.tables.records.CompanyRecord;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.example.ticket_service.dto.request.CompanyDtoForCreation;
import ru.example.ticket_service.dto.request.CompanyDtoForEdit;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class CompanyRepository {

    private final DSLContext context;

    public Optional<CompanyRecord> deleteCompanyById(Long id) {
        return context.deleteFrom(Tables.COMPANY).where(Tables.COMPANY.ID.eq(id))
                .returning().fetchOptional();
    }

    public Optional<CompanyRecord> addCompany(CompanyDtoForCreation company) {
        return context.insertInto(Tables.COMPANY)
                .set(context.newRecord(Tables.COMPANY, company))
                .returning()
                .fetchOptional();
    }

    public Optional<CompanyRecord> updateCompany(CompanyDtoForEdit company) {
        return context.update(Tables.COMPANY)
                .set(Tables.COMPANY.TITLE, company.getTitle())
                .set(Tables.COMPANY.PHONE, company.getPhone())
                .where(Tables.COMPANY.ID.eq(company.getId()))
                .returning().fetchOptional();
    }

}
