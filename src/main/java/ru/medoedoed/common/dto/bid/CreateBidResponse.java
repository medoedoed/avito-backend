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
public class CreateBidResponse {
    private UUID id;
    private String name;
    private BidStatus status;
    private BidAuthorType authorType;
    private UUID authorId;
    private Integer version;
    private LocalDateTime createdAt;
}
