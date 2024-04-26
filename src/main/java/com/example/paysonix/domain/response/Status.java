package com.example.paysonix.domain.response;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Status {
    SUCCESS("success");

    @JsonValue
    private final String status;

    Status(String status) {
        this.status = status;
    }
}
