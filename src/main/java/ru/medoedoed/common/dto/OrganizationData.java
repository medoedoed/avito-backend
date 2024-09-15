package ru.medoedoed.common.dto;

import lombok.*;
import ru.medoedoed.common.data.OrganizationType;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationData {
    private UUID id;
    private String name;
    private String description;
    private OrganizationType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
