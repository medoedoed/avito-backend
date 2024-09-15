package ru.medoedoed.common.dto.bid;

import lombok.*;
import ru.medoedoed.common.data.BidAuthorType;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBidRequest {
    private String name;
    private String description;
    private UUID tenderId;
    private BidAuthorType authorType;
    private UUID authorId;
}
