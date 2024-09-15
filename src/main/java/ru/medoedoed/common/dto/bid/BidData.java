package ru.medoedoed.common.dto.bid;

import lombok.*;
import ru.medoedoed.common.data.BidAuthorType;
import ru.medoedoed.common.data.BidStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BidData {
    private UUID id;
    private String name;
    private String description;
    private BidStatus status;
    private UUID tenderId;
    private BidAuthorType authorType;
    private UUID authorId;
    private Integer version;
    private LocalDateTime createdAt;
}
