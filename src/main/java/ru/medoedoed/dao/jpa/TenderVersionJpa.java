package ru.medoedoed.dao.jpa;

import jakarta.persistence.*;
import lombok.*;
import ru.medoedoed.common.data.ServiceType;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tender_version")
public class TenderVersionJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "original_tender_id")
    private UUID originalTenderId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "service_type", nullable = false)
    private ServiceType serviceType;

    @Column(name = "version", updatable = false, nullable = false)
    private Integer version;
}
