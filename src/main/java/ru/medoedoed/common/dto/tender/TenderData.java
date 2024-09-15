package ru.medoedoed.common.dto.tender;

import lombok.*;
import ru.medoedoed.common.data.ServiceType;
import ru.medoedoed.common.data.TenderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TenderData {
    private UUID id;
    private String name;
    private String description;
    private ServiceType serviceType;
    private TenderStatus status;
    private UUID organizationId;
    private Integer version;
    private LocalDateTime createdAt;
}
