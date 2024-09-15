package ru.medoedoed.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class BadResponse {
    @Length(min = 1)
    private String reason;

    @JsonIgnore
    private HttpStatus status;

    public BadResponse(String reason, HttpStatus status) {
        this.reason = reason;
        this.status = status;
    }

    public ResponseEntity<?> returnResponse() {
        return ResponseEntity.status(status).body(this);
    }
}
