package ru.medoedoed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.medoedoed.common.data.BidAuthorType;
import ru.medoedoed.common.data.BidDecision;
import ru.medoedoed.common.data.BidStatus;
import ru.medoedoed.common.dto.bid.BidData;
import ru.medoedoed.common.dto.bid.CreateBidRequest;
import ru.medoedoed.common.dto.tender.TenderData;
import ru.medoedoed.common.exceptions.InvalidEmployeeException;
import ru.medoedoed.common.exceptions.InvalidOrganizationException;
import ru.medoedoed.common.exceptions.InvalidTenderException;
import ru.medoedoed.service.BidService;
import ru.medoedoed.service.EmployeeService;
import ru.medoedoed.service.OrganizationService;
import ru.medoedoed.service.TenderService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bids")
@Validated
public class BidController {
    private final BidService bidService;
    private final EmployeeService employeeService;
    private final OrganizationService organizationService;
    private final TenderService tenderService;

    @PostMapping("/new")
    public ResponseEntity<?> createBid(@RequestBody CreateBidRequest bidRequest) {
        var authorType = bidRequest.getAuthorType();
        var authorId = bidRequest.getAuthorId();

        if (authorType.equals(BidAuthorType.User) && !employeeService.employeeExistsById(authorId)) {
            throw new InvalidEmployeeException(authorId.toString());
        }

        if (authorType == BidAuthorType.Organization && !organizationService.organizationExistsById(authorId)) {
            throw new InvalidOrganizationException(authorId);
        }

        if (!tenderService.tenderExistsById(bidRequest.getTenderId())) {
            throw new InvalidTenderException(bidRequest.getTenderId());
        }

        return ResponseEntity.ok(bidService.createBid(bidRequest));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getUserBids(
            @RequestParam(name = "limit", defaultValue = "10") @Min(1) @Max(100) int limit,
            @RequestParam(name = "offset", defaultValue = "0") @Min(0) int offset,
            @RequestParam(name = "username") String username) {
        List<BidData> bids = bidService.getBidsByUsername(limit, offset, username);
        return ResponseEntity.ok(bids);
    }

    @GetMapping("/{tenderId}/list")
    public ResponseEntity<?> getBidsForTender(
            @PathVariable UUID tenderId,
            @RequestParam(name = "username") String username,
            @RequestParam(name = "limit", defaultValue = "10") @Min(1) @Max(100) int limit,
            @RequestParam(name = "offset", defaultValue = "0") @Min(0) int offset) {
        if (!employeeService.employeeExistsByUsername(username)) {
            throw new InvalidEmployeeException(username);
        }

        if (!tenderService.tenderExistsById(tenderId)) {
            throw new InvalidTenderException(tenderId);
        }

        List<BidData> bids = bidService.getBidsForTender(tenderId, limit, offset);
        return ResponseEntity.ok(bids);
    }

    @GetMapping("/{bidId}/status")
    public ResponseEntity<?> getBidStatus(
            @PathVariable UUID bidId,
            @RequestParam String username) {
        BidStatus status = bidService.getBidStatus(bidId, username);
        return ResponseEntity.ok(status);
    }

    @PutMapping("/{bidId}/status")
    public ResponseEntity<?> updateBidStatus(
            @PathVariable UUID bidId,
            @RequestParam BidStatus status,
            @RequestParam String username) {
        BidData updatedBid = bidService.updateBidStatus(bidId, status, username);
        return ResponseEntity.ok(updatedBid);
    }

    @PatchMapping("/{bidId}/edit")
    public ResponseEntity<?> editBid(
            @PathVariable UUID bidId,
            @RequestParam String username,
            @RequestBody Map<String, Object> updates) {
        BidData updatedBid = bidService.editBid(bidId, username, updates);
        return ResponseEntity.ok(updatedBid);
    }

    @PutMapping("/{bidId}/submit_decision")
    public ResponseEntity<?> submitBidDecision(
            @PathVariable UUID bidId,
            @RequestParam BidDecision decision,
            @RequestParam String username) {
        BidData updatedBid = bidService.submitBidDecision(bidId, decision, username);
        return ResponseEntity.ok(updatedBid);
    }

    @PutMapping("/{bidId}/rollback/{version}")
    public ResponseEntity<?> rollbackTender(
            @PathVariable UUID bidId,
            @PathVariable @Min(1) int version,
            @RequestParam String username) {
        if (!employeeService.employeeExistsByUsername(username)) {
            throw new InvalidEmployeeException(username);
        }
        TenderData rolledBackTender = tenderService.rollbackTenderVersion(bidId, version);
        return ResponseEntity.ok(rolledBackTender);
    }
}