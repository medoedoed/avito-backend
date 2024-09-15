package ru.medoedoed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.medoedoed.dao.repository.EmployeeRepository;
import ru.medoedoed.dao.repository.OrganizationResponsibleRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final OrganizationResponsibleRepository organizationResponsibleRepository;

    public boolean employeeExistsByUsername(String employeeUsername) {
        return employeeRepository.existsByUsername(employeeUsername);
    }


    public boolean employeeExistsById(UUID employeeId) {
        return employeeRepository.existsById(employeeId);
    }
}
