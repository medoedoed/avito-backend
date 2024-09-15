package ru.medoedoed.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.medoedoed.common.data.TenderStatus;
import ru.medoedoed.dao.jpa.TenderJpa;

import java.util.List;
import java.util.UUID;

@Repository
public interface TenderRepository extends JpaRepository<TenderJpa, UUID> {
    List<TenderJpa> findAllByOrganizationId(UUID organizationId);
    List<TenderJpa> findAllByStatus(TenderStatus status);
}
