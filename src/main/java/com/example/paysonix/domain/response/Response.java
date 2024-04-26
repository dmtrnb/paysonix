package com.example.paysonix.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Response {

    private Status status;
    @JsonProperty("result")
    private List<Result> results;
}
