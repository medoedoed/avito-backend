package ru.medoedoed.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.medoedoed.dao.jpa.OrganizationResponsibleJpa;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationResponsibleRepository extends
        JpaRepository<OrganizationResponsibleJpa, UUID> {
    boolean existsByEmployeeIdAndOrganizationId(UUID employeeId, UUID organizationId);
    boolean existsByEmployeeId(UUID employeeId);
    Optional<OrganizationResponsibleJpa> findByEmployeeId(UUID employeeId);
}
