package ru.medoedoed.service.converter;

import org.springframework.stereotype.Service;
import ru.medoedoed.dao.jpa.TenderJpa;
import ru.medoedoed.common.dto.tender.TenderData;

@Service
public class TenderDataConverter implements DataConverter<TenderJpa, TenderData> {
    @Override
    public TenderJpa DataToJpa(TenderData data) {
        var result = new TenderJpa();
        result.setId(data.getId());
        result.setName(data.getName());
        result.setDescription(data.getDescription());
        result.setStatus(data.getStatus());
        result.setServiceType(data.getServiceType());
        result.setOrganizationId(data.getOrganizationId());
        result.setVersion(data.getVersion());
        result.setCreatedAt(data.getCreatedAt());
        return result;
    }

    @Override
    public TenderData JpaToData(TenderJpa jpa) {
        return TenderData.
                builder().
                id(jpa.getId()).
                name(jpa.getName()).
                description(jpa.getDescription()).
                status(jpa.getStatus()).
                organizationId(jpa.getOrganizationId()).
                version(jpa.getVersion()).
                serviceType(jpa.getServiceType()).
                createdAt(jpa.getCreatedAt()).
                build();
    }
}
