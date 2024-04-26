package com.example.paysonix.controller;

import com.example.paysonix.domain.response.Response;
import com.example.paysonix.domain.response.Status;
import com.example.paysonix.service.SomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SomeController {

    private final SomeService service;

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Response somePostMethod(@RequestBody(required = false) final MultiValueMap<String, String> params,
                                   @RequestParam String operationId) {
        return new Response(Status.SUCCESS, service.doSomething(params));
    }
}
