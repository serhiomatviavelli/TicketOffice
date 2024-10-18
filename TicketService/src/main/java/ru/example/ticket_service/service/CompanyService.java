package ru.example.ticket_service.service;

import jooq.db.tables.records.CompanyRecord;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.ticket_service.dto.request.CompanyDtoForCreation;
import ru.example.ticket_service.dto.request.CompanyDtoForEdit;
import ru.example.ticket_service.repository.CompanyRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository repository;

    public CompanyRecord deleteCompanyById(Long id) {
        Optional<CompanyRecord> company = repository.deleteCompanyById(id);
        return company.orElse(null);
    }

    public CompanyRecord addCompany(CompanyDtoForCreation dto) {
        Optional<CompanyRecord> company = repository.addCompany(dto);
        return company.orElse(null);
    }

    public CompanyRecord updateCompany(CompanyDtoForEdit companyDto) {
        Optional<CompanyRecord> company = repository.updateCompany(companyDto);
        return company.orElse(null);
    }

}
