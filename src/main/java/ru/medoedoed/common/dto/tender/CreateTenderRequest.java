package ru.medoedoed.common.dto.tender;

import lombok.*;
import ru.medoedoed.common.data.ServiceType;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTenderRequest {
    private String name;
    private String description;
    private ServiceType serviceType;
    private UUID organizationId;
    private String creatorUsername;
}
