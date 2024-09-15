package ru.medoedoed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.medoedoed.common.data.BidDecision;
import ru.medoedoed.common.data.BidStatus;
import ru.medoedoed.common.data.TenderStatus;
import ru.medoedoed.common.dto.bid.BidData;
import ru.medoedoed.common.dto.bid.CreateBidRequest;
import ru.medoedoed.common.dto.bid.CreateBidResponse;
import ru.medoedoed.common.exceptions.InsufficientRightsException;
import ru.medoedoed.common.exceptions.InvalidBidException;
import ru.medoedoed.common.exceptions.InvalidEmployeeException;
import ru.medoedoed.common.exceptions.InvalidTenderException;
import ru.medoedoed.dao.jpa.BidJpa;
import ru.medoedoed.dao.jpa.EmployeeJpa;
import ru.medoedoed.dao.repository.BidRepository;
import ru.medoedoed.dao.repository.EmployeeRepository;
import ru.medoedoed.dao.repository.TenderRepository;
import ru.medoedoed.service.converter.BidDataConverter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidDataConverter bidDataConverter;
    private final BidRepository bidRepository;
    private final EmployeeRepository employeeRepository;
    private final TenderRepository tenderRepository;
    private final BidVersionService bidVersionService;

    public CreateBidResponse createBid(CreateBidRequest bidRequest) {
        var bidData = BidData.builder().
                name(bidRequest.getName()).
                description(bidRequest.getDescription()).
                status(BidStatus.Created).
                tenderId(bidRequest.getTenderId()).
                authorType(bidRequest.getAuthorType()).
                authorId(bidRequest.getAuthorId()).
                version(1).
                createdAt(LocalDateTime.now()).build();

        var bidJpa = bidDataConverter.DataToJpa(bidData);
        UUID bidId = bidRepository.save(bidJpa).getId();

        bidVersionService.initVersion(bidId);

        return CreateBidResponse.builder().
                id(bidId).
                name(bidData.getName()).
                status(bidData.getStatus()).
                authorType(bidData.getAuthorType()).
                authorId(bidData.getAuthorId()).
                version(bidData.getVersion()).
                createdAt(bidData.getCreatedAt()).build();
    }

    public List<BidData> getBidsByUsername(int limit, int offset, String username) {
        ArrayList<BidData> bids = new ArrayList<>(bidRepository.
                findAllByStatus(BidStatus.Published).stream().
                map(bidDataConverter::JpaToData).
                toList());

        var employeeJpa = employeeRepository.findByUsername(username).orElse(null);
        if (employeeJpa == null) throw new InvalidEmployeeException(username);

        UUID userId = employeeJpa.getId();

        List<BidJpa> allBids = bidRepository.findAllByAuthorId(userId);
        bids.addAll(allBids.stream()
                .filter(tender -> !BidStatus.Published.equals(tender.getStatus()))
                .map(bidDataConverter::JpaToData)
                .toList());


        int start = Math.min(offset, bids.size());
        int end = Math.min(start + limit, bids.size());

        return bids.subList(start, end);
    }

    public BidData editBid(UUID bidId, String username, Map<String, Object> updates) {
        var employeeJpa = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidEmployeeException(username));

        var bidJpa = bidRepository.findById(bidId)
                .orElseThrow(() -> new InvalidBidException(bidId));

        if (!bidJpa.getAuthorId().equals(employeeJpa.getId())) {
            throw new InsufficientRightsException(username, bidId);
        }

        if (updates.containsKey("name")) {
            bidJpa.setName((String) updates.get("name"));
        }

        if (updates.containsKey("description")) {
            bidJpa.setDescription((String) updates.get("description"));
        }

        bidJpa.setVersion(bidJpa.getVersion() + 1);

        bidJpa = bidRepository.save(bidJpa);
        bidVersionService.initVersion(bidId);
        return bidDataConverter.JpaToData(bidJpa);
    }

    public List<BidData> getBidsForTender(UUID tenderId, @Min(1) @Max(100) int limit, @Min(0) int offset) {
        List<BidJpa> allBidsJpa = bidRepository.findAllByTenderId(tenderId);

        List<BidData> bids = allBidsJpa.stream()
                .map(bidDataConverter::JpaToData)
                .sorted(Comparator.comparing(BidData::getName))
                .collect(Collectors.toList());

        int start = Math.min(offset, bids.size());
        int end = Math.min(start + limit, bids.size());

        return bids.subList(start, end);
    }

    public BidStatus getBidStatus(UUID bidId, String username) {
        BidJpa bid = bidRepository.findById(bidId).orElseThrow(() -> new InvalidBidException(bidId));
        EmployeeJpa employee = employeeRepository.findByUsername(username).orElseThrow(() -> new InvalidEmployeeException(username));

        if (!bid.getAuthorId().equals(employee.getId())) {
            throw new InsufficientRightsException(username, bidId);
        }

        return bid.getStatus();
    }

    public BidData updateBidStatus(UUID bidId, BidStatus status, String username) {
        BidJpa bid = bidRepository.findById(bidId).orElseThrow(() -> new InvalidBidException(bidId));
        bid.setStatus(status);
        bidRepository.save(bid);
        return bidDataConverter.JpaToData(bid);
    }

    public BidData submitBidDecision(UUID bidId, BidDecision decision, String username) {
        var bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new InvalidBidException(bidId));

        var employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidEmployeeException(username));

        var tenderId = bid.getTenderId();
        var tender = tenderRepository.findById(tenderId).orElseThrow(() -> new InvalidTenderException(tenderId));

        if (!employee.getId().equals(bid.getAuthorId())) {
            throw new InsufficientRightsException(username, bidId);
        }


        if (decision.equals(BidDecision.Approved)) {
            tender.setStatus(TenderStatus.Closed);
            tenderRepository.save(tender);
        } else {
            bid.setStatus(BidStatus.Canceled);
        }


        var updatedBid = bidRepository.save(bid);

        return bidDataConverter.JpaToData(updatedBid);
    }

    public BidData rollbackTenderVersion(UUID bidId, @Min(1) int version) {
        bidVersionService.rollback(bidId, version);

        return bidDataConverter.JpaToData(bidRepository.findById(bidId)
                .orElseThrow(() -> new InvalidTenderException(bidId)));
    }
}
