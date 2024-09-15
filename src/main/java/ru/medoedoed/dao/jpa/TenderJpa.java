package ru.medoedoed.dao.jpa;

import jakarta.persistence.*;
import lombok.*;
import ru.medoedoed.common.data.ServiceType;
import ru.medoedoed.common.data.TenderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tender")
public class TenderJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "service_type", nullable = false)
    private ServiceType serviceType;

    @Column(name = "tender_status", nullable = false)
    private TenderStatus status;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "version")
    private Integer version;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
