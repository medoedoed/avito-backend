package ru.medoedoed.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.medoedoed.common.data.BidStatus;
import ru.medoedoed.dao.jpa.BidJpa;

import java.util.List;
import java.util.UUID;

public interface BidRepository extends JpaRepository<BidJpa, UUID> {
    List<BidJpa> findAllByStatus(BidStatus status);
    List<BidJpa> findAllByAuthorId(UUID authorId);
    List<BidJpa> findAllByTenderId(UUID tenderId);
}
