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
public class CreateTenderResponse {
    private UUID id;
    private String name;
    private String description;
    private TenderStatus status;
    private ServiceType serviceType;
    private Integer version;
    private LocalDateTime createdAt;
}
