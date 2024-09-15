package ru.medoedoed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.medoedoed.common.exceptions.InvalidEmployeeException;
import ru.medoedoed.dao.repository.EmployeeRepository;
import ru.medoedoed.dao.repository.OrganizationResponsibleRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationResponsibleService {
    private final EmployeeRepository employeeRepository;
    private final OrganizationResponsibleRepository organizationResponsibleRepository;

    public boolean employeeIsResponsible(String employeeUsername, UUID organizationId) {
        var employeeJpa = employeeRepository.findByUsername(employeeUsername).orElse(null);
        if (employeeJpa == null) throw new InvalidEmployeeException(employeeUsername);
        return organizationResponsibleRepository.existsByEmployeeIdAndOrganizationId(employeeJpa.getId(), organizationId);
    }
}
