package ru.medoedoed.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.medoedoed.dao.jpa.BidVersionJpa;

import java.util.Optional;
import java.util.UUID;

public interface BidVersionRepository extends JpaRepository<BidVersionJpa, UUID> {
    Optional<BidVersionJpa> findByOriginalBidIdAndVersion(UUID tenderId, Integer version);
}
