package ru.medoedoed.common.dto.tender;

import lombok.*;
import ru.medoedoed.common.data.ServiceType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditTenderRequest {
    private String name;
    private String description;
    private ServiceType serviceType;
}
