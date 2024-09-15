package ru.medoedoed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.medoedoed.common.data.ServiceType;
import ru.medoedoed.common.data.TenderStatus;
import ru.medoedoed.common.dto.tender.CreateTenderRequest;
import ru.medoedoed.common.dto.tender.TenderData;
import ru.medoedoed.common.exceptions.InsufficientRightsException;
import ru.medoedoed.common.exceptions.InvalidEmployeeException;
import ru.medoedoed.common.exceptions.InvalidOrganizationException;
import ru.medoedoed.service.EmployeeService;
import ru.medoedoed.service.OrganizationResponsibleService;
import ru.medoedoed.service.OrganizationService;
import ru.medoedoed.service.TenderService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tenders")
@Validated
public class TenderController {
    private final TenderService tenderService;
    private final EmployeeService employeeService;
    private final OrganizationService organizationService;
    private final OrganizationResponsibleService organizationResponsibleService;

    @GetMapping()
    public ResponseEntity<?> getTenders(
            @RequestParam(name = "limit", defaultValue = "10") @Min(1) @Max(100) int limit,
            @RequestParam(name = "offset", defaultValue = "0") @Min(0) int offset,
            @RequestParam(name = "service_type", required = false) List<String> serviceTypesString) {
        List<ServiceType> serviceTypes = serviceTypesString == null ? Collections.emptyList() : serviceTypesString.stream()
                .map(ServiceType::valueOf)
                .collect(Collectors.toList());

        List<TenderData> tenders = tenderService.getTenders(limit, offset, serviceTypes);
        return ResponseEntity.ok(tenders);
    }

    @PostMapping("/new")
    public ResponseEntity<?> createTender(@RequestBody CreateTenderRequest tenderRequest) {
        var username = tenderRequest.getCreatorUsername();
        var organizationId = tenderRequest.getOrganizationId();

        if (!employeeService.employeeExistsByUsername(username)) {
            throw new InvalidEmployeeException(username);
        }

        if (!organizationService.organizationExistsById(organizationId))
            throw new InvalidOrganizationException(organizationId);

        if (!organizationResponsibleService.employeeIsResponsible(username, organizationId))
            throw new InsufficientRightsException(username, organizationId);

        return ResponseEntity.ok(tenderService.createTender(tenderRequest));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getUserTenders(
            @RequestParam(name = "limit", defaultValue = "10") @Min(1) @Max(100) int limit,
            @RequestParam(name = "offset", defaultValue = "0") @Min(0) int offset,
            @RequestParam(name = "username") String username) {
        List<TenderData> tenders = tenderService.getTendersByUsername(limit, offset, username);
        return ResponseEntity.ok(tenders);

    }

    @GetMapping("/{tenderId}/status")
    public ResponseEntity<?> getTenderStatus(
            @PathVariable UUID tenderId,
            @RequestParam String username) {

        if (!employeeService.employeeExistsByUsername(username))
            throw new InvalidEmployeeException(username);

        if (!tenderService.tenderIsAccessible(tenderId, username)) {
            throw new InsufficientRightsException(username, tenderId);
        }

        TenderStatus tenderStatus = tenderService.getTenderStatus(tenderId);
        return ResponseEntity.ok(tenderStatus);

    }

    @PutMapping("/{tenderId}/status")
    public ResponseEntity<?> updateTenderStatus(
            @PathVariable UUID tenderId,
            @RequestParam(name = "status") TenderStatus status,
            @RequestParam(name = "username") String username) {

        if (!employeeService.employeeExistsByUsername(username))
            throw new InvalidEmployeeException(username);

        if (!tenderService.tenderIsAccessible(tenderId, username)) {
            throw new InsufficientRightsException(username, tenderId);
        }

        TenderData updatedTender = tenderService.updateTenderStatus(tenderId, status);
        return ResponseEntity.ok(updatedTender);

    }

    @PatchMapping("/{tenderId}/edit")
    public ResponseEntity<?> editTender(
            @PathVariable UUID tenderId,
            @RequestParam String username,
            @RequestBody Map<String, Object> updates) {

        if (!employeeService.employeeExistsByUsername(username)) {
            throw new InvalidEmployeeException(username);
        }

        if (!tenderService.tenderIsAccessible(tenderId, username)) {
            throw new InsufficientRightsException(username, tenderId);
        }

        TenderData updatedTender = tenderService.editTender(tenderId, updates);
        return ResponseEntity.ok(updatedTender);

    }

    @PutMapping("/{tenderId}/rollback/{version}")
    public ResponseEntity<?> rollbackTender(
            @PathVariable UUID tenderId,
            @PathVariable @Min(1) int version,
            @RequestParam String username) {
        if (!tenderService.tenderIsAccessible(tenderId, username)) {
            throw new InsufficientRightsException(username, tenderId);
        }

        TenderData rolledBackTender = tenderService.rollbackTenderVersion(tenderId, version);
        return ResponseEntity.ok(rolledBackTender);
    }
}

