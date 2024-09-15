package ru.medoedoed.dao.jpa;

import jakarta.persistence.*;
import lombok.*;
import ru.medoedoed.common.data.BidAuthorType;
import ru.medoedoed.common.data.BidStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bid")
public class BidJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "status")
    private BidStatus status;

    @Column(name = "tender_id")
    private UUID tenderId;

    @Column(name = "author_type")
    private BidAuthorType authorType;

    @Column(name = "author_id")
    private UUID authorId;

    @Column(name = "version")
    private Integer version;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
