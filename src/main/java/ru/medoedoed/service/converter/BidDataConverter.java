package ru.medoedoed.service.converter;

import org.springframework.stereotype.Service;
import ru.medoedoed.common.dto.bid.BidData;
import ru.medoedoed.dao.jpa.BidJpa;

@Service
public class BidDataConverter implements DataConverter<BidJpa, BidData> {

    @Override
    public BidJpa DataToJpa(BidData data) {
        var result = new BidJpa();
        result.setId(data.getId());
        result.setName(data.getName());
        result.setDescription(data.getDescription());
        result.setStatus(data.getStatus());
        result.setTenderId(data.getTenderId());
        result.setAuthorType(data.getAuthorType());
        result.setAuthorId(data.getAuthorId());
        result.setVersion(data.getVersion());
        result.setCreatedAt(data.getCreatedAt());
        return result;
    }

    @Override
    public BidData JpaToData(BidJpa jpa) {
        return BidData.builder()
                .id(jpa.getId())
                .name(jpa.getName())
                .description(jpa.getDescription())
                .status(jpa.getStatus())
                .tenderId(jpa.getTenderId())
                .authorType(jpa.getAuthorType())
                .authorId(jpa.getAuthorId())
                .version(jpa.getVersion())
                .createdAt(jpa.getCreatedAt())
                .build();
    }
}
