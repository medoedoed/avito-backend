package ru.medoedoed.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.medoedoed.dao.jpa.EmployeeJpa;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeJpa, UUID> {
    boolean existsByUsername(String username);
    Optional<EmployeeJpa> findByUsername(String username);
}