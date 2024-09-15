package ru.medoedoed.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.medoedoed.common.data.ServiceType;
import ru.medoedoed.common.data.TenderStatus;
import ru.medoedoed.common.dto.tender.CreateTenderRequest;
import ru.medoedoed.common.dto.tender.CreateTenderResponse;
import ru.medoedoed.common.dto.tender.TenderData;
import ru.medoedoed.common.exceptions.InvalidEmployeeException;
import ru.medoedoed.common.exceptions.InvalidTenderException;
import ru.medoedoed.dao.jpa.EmployeeJpa;
import ru.medoedoed.dao.jpa.TenderJpa;
import ru.medoedoed.dao.repository.EmployeeRepository;
import ru.medoedoed.dao.repository.OrganizationResponsibleRepository;
import ru.medoedoed.dao.repository.TenderRepository;
import ru.medoedoed.service.converter.TenderDataConverter;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenderService {
    private final TenderRepository tenderRepository;
    private final TenderVersionService tenderVersionService;
    private final TenderDataConverter tenderDataConverter;
    private final EmployeeRepository employeeRepository;
    private final OrganizationResponsibleRepository organizationResponsibleRepository;

    public CreateTenderResponse createTender(CreateTenderRequest tenderRequest) {
        var tenderData = TenderData.builder().
                name(tenderRequest.getName()).
                description(tenderRequest.getDescription()).
                status(TenderStatus.Created).
                organizationId(tenderRequest.getOrganizationId()).
                version(1).
                serviceType(tenderRequest.getServiceType()).
                createdAt(LocalDateTime.now()).build();

        var tenderJpa = tenderDataConverter.DataToJpa(tenderData);
        UUID tenderId = tenderRepository.save(tenderJpa).getId();

        tenderVersionService.initVersion(tenderId);

        return CreateTenderResponse.builder().
                id(tenderId).
                name(tenderData.getName()).
                description(tenderData.getDescription()).
                status(tenderData.getStatus()).
                serviceType(tenderData.getServiceType()).
                version(tenderData.getVersion()).
                createdAt(tenderData.getCreatedAt()).build();
    }

    public TenderData editTender(UUID tenderId, Map<String, Object> updates) {
        TenderJpa tenderJpa = tenderRepository.findById(tenderId)
                .orElseThrow(() -> new InvalidTenderException(tenderId));

        if (updates.containsKey("name")) {
            tenderJpa.setName((String) updates.get("name"));
        }

        if (updates.containsKey("description")) {
            tenderJpa.setDescription((String) updates.get("description"));
        }

        if (updates.containsKey("serviceType")) {
            tenderJpa.setServiceType((ServiceType) updates.get("serviceTypes"));

        }

        tenderJpa.setVersion(tenderJpa.getVersion() + 1);

        tenderRepository.save(tenderJpa);
        tenderVersionService.initVersion(tenderJpa.getId());

        return tenderDataConverter.JpaToData(tenderJpa);
    }

    public List<TenderData> getTenders(int limit, int offset, List<ServiceType> serviceTypes) {
        List<TenderJpa> allTenders = tenderRepository.findAll();
        List<TenderJpa> filteredTenders = allTenders.stream()
                .filter(tender -> serviceTypes.isEmpty() || serviceTypes.contains(tender.getServiceType()))
                .toList();

        int start = Math.min(offset, filteredTenders.size());
        int end = Math.min(start + limit, filteredTenders.size());

        if (start > end) {
            return Collections.emptyList();
        }

        return filteredTenders.subList(start, end).stream()
                .map(tenderDataConverter::JpaToData)
                .sorted(Comparator.comparing(TenderData::getName))
                .collect(Collectors.toList());

    }

    public List<TenderData> getTendersByUsername(int limit, int offset, String username) {
        List<TenderData> tenders = new ArrayList<>(tenderRepository.
                findAllByStatus(TenderStatus.Published).stream().
                map(tenderDataConverter::JpaToData).
                toList());

        var employeeJpa = employeeRepository.findByUsername(username).orElse(null);
        if (employeeJpa == null) throw new InvalidEmployeeException(username);

        var organizationResponsibleJpa = organizationResponsibleRepository.findByEmployeeId(employeeJpa.getId()).orElse(null);

        if (organizationResponsibleJpa != null) {
            UUID organizationId = organizationResponsibleJpa.getOrganizationId();
            List<TenderJpa> allTenders = tenderRepository.findAllByOrganizationId(organizationId);
            tenders.addAll(allTenders.stream()
                    .filter(tender -> !TenderStatus.Published.equals(tender.getStatus()))
                    .map(tenderDataConverter::JpaToData)
                    .toList());
        }

        int start = Math.min(offset, tenders.size());
        int end = Math.min(start + limit, tenders.size());

        return tenders.subList(start, end);
    }

    public boolean tenderIsAccessible(UUID tenderId, String username) {
        TenderJpa tenderJpa = tenderRepository.findById(tenderId)
                .orElseThrow(() -> new InvalidTenderException(tenderId));
        if (tenderJpa.getStatus().equals(TenderStatus.Published)) return true;

        EmployeeJpa employeeJpa = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidEmployeeException(username));

        var responsible = organizationResponsibleRepository.findByEmployeeId(employeeJpa.getId()).orElse(null);
        if (responsible == null) return false;
        return tenderJpa.getOrganizationId().equals(responsible.getOrganizationId());
    }

    public TenderStatus getTenderStatus(UUID tenderId) {
        var jpa = tenderRepository.findById(tenderId).orElse(null);
        if (jpa == null) throw new InvalidTenderException(tenderId);
        return jpa.getStatus();
    }

    public TenderData updateTenderStatus(UUID tenderId, TenderStatus newTenderStatus) {
        TenderJpa tenderJpa = tenderRepository.findById(tenderId).orElse(null);
        if (tenderJpa == null) throw new InvalidTenderException(tenderId);

        tenderJpa.setStatus(newTenderStatus);
        tenderRepository.save(tenderJpa);
        return tenderDataConverter.JpaToData(tenderJpa);
    }

    public boolean tenderExistsById(UUID tenderId) {
        return tenderRepository.existsById(tenderId);
    }

    public TenderData rollbackTenderVersion(UUID tenderId, @Min(1) int version) {
        tenderVersionService.rollback(tenderId, version);

        return tenderDataConverter.JpaToData(tenderRepository.findById(tenderId)
                .orElseThrow(() -> new InvalidTenderException(tenderId)));
    }
}
