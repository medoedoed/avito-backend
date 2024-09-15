package ru.medoedoed.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.medoedoed.dao.jpa.OrganizationJpa;

import java.util.UUID;

public interface OrganizationRepository
        extends JpaRepository<OrganizationJpa, UUID> {
}
