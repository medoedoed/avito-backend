package ru.medoedoed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.medoedoed.common.exceptions.InvalidBidException;
import ru.medoedoed.dao.jpa.BidVersionJpa;
import ru.medoedoed.dao.repository.BidRepository;
import ru.medoedoed.dao.repository.BidVersionRepository;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BidVersionService {
    private final BidVersionRepository bidVersionRepository;
    private final BidRepository bidRepository;

    public void initVersion(UUID bidId) {
        var bidJpa = bidRepository.findById(bidId)
                .orElseThrow(() -> new InvalidBidException(bidId));

        var bidVersionJpa = BidVersionJpa.builder()
                .originalBidId(bidId)
                .name(bidJpa.getName())
                .description(bidJpa.getDescription())
                .version(bidJpa.getVersion()).build();

        bidVersionRepository.save(bidVersionJpa);
    }

    public void rollback(UUID bidId, Integer version) {
        var bidVersionJpa = bidVersionRepository.findByOriginalBidIdAndVersion(bidId, version)
                .orElseThrow(() -> new InvalidBidException(bidId));

        var bidJpa = bidRepository.findById(bidId)
                .orElseThrow(() -> new InvalidBidException(bidId));

        bidJpa.setName(bidVersionJpa.getName());
        bidJpa.setDescription(bidVersionJpa.getDescription());

        bidJpa.setVersion(bidJpa.getVersion() + 1);
        bidRepository.save(bidJpa);
        initVersion(bidId);
    }
}
