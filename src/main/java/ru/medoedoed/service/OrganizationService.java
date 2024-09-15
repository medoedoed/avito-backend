package ru.medoedoed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.medoedoed.dao.repository.OrganizationRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    public boolean organizationExistsById(UUID organizationId) {
        return organizationRepository.existsById(organizationId);
    }
}
