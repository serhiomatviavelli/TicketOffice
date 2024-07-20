package ru.example.ticket_office.service;

import jooq.db.tables.records.CompanyRecord;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.ticket_office.dto.CompanyDtoForCreation;
import ru.example.ticket_office.dto.CompanyDtoForEdit;
import ru.example.ticket_office.repository.CompanyRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository repository;

    public CompanyRecord deleteCompanyById(Long id) {
        Optional<CompanyRecord> company = repository.deleteCompanyById(id);
        return company.isEmpty() ? null
                : company.get();
    }

    public CompanyRecord addCompany(CompanyDtoForCreation dto) {
        boolean isPhoneValid = dto.getPhone()
                .matches("(?:\\+|\\d)[\\d\\-\\(\\) ]{9,}\\d");
        if (isPhoneValid) {
            Optional<CompanyRecord> company = repository.addCompany(dto);
            if (company.isPresent()) {
                return company.get();
            }
        }
        return null;
    }

    public CompanyRecord updateCompany(CompanyDtoForEdit companyDto) {
        boolean isPhoneValid = companyDto.getPhone()
                .matches("(?:\\+|\\d)[\\d\\-\\(\\) ]{9,}\\d");
        if (isPhoneValid) {
            Optional<CompanyRecord> company = repository.updateCompany(companyDto);
            if (company.isPresent()) {
                return company.get();
            }
        }
        return null;
    }

}
