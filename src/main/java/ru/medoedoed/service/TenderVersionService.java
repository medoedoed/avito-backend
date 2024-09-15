package ru.medoedoed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.medoedoed.common.exceptions.InvalidTenderException;
import ru.medoedoed.dao.jpa.TenderVersionJpa;
import ru.medoedoed.dao.repository.TenderRepository;
import ru.medoedoed.dao.repository.TenderVersionRepository;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TenderVersionService {
    private final TenderVersionRepository tenderVersionRepository;
    private final TenderRepository tenderRepository;

    public void initVersion(UUID tenderId) {
        var tenderJpa = tenderRepository.findById(tenderId)
                .orElseThrow(() -> new InvalidTenderException(tenderId));

        var tenderVersionJpa = TenderVersionJpa.builder()
                .originalTenderId(tenderId)
                .name(tenderJpa.getName())
                .description(tenderJpa.getDescription())
                .serviceType(tenderJpa.getServiceType())
                .version(tenderJpa.getVersion()).build();

        tenderVersionRepository.save(tenderVersionJpa);
    }

    public void rollback(UUID tenderId, Integer version) {
        var tenderVersionJpa = tenderVersionRepository.findByOriginalTenderIdAndVersion(tenderId, version)
                .orElseThrow(() -> new InvalidTenderException(tenderId));

        var tenderJpa = tenderRepository.findById(tenderId)
                .orElseThrow(() -> new InvalidTenderException(tenderId));

        tenderJpa.setName(tenderVersionJpa.getName());
        tenderJpa.setDescription(tenderVersionJpa.getDescription());
        tenderJpa.setServiceType(tenderVersionJpa.getServiceType());

        tenderJpa.setVersion(tenderJpa.getVersion() + 1);
        tenderRepository.save(tenderJpa);
        initVersion(tenderId);
    }




}
