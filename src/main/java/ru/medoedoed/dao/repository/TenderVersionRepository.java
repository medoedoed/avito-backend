package ru.medoedoed.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.medoedoed.dao.jpa.TenderVersionJpa;

import java.util.Optional;
import java.util.UUID;

public interface TenderVersionRepository extends JpaRepository<TenderVersionJpa, UUID> {
    Optional<TenderVersionJpa> findByOriginalTenderIdAndVersion(UUID tenderId, Integer version);
}
