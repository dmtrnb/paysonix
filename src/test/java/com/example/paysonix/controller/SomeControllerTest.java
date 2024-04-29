package com.example.paysonix.controller;

import com.example.paysonix.configuration.JsonConfig;
import com.example.paysonix.domain.response.Result;
import com.example.paysonix.service.SecretCipher;
import com.example.paysonix.service.SomeService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(JsonConfig.class)
@WebMvcTest(SomeController.class)
public class SomeControllerTest {

    private static final String OUTPUT_HASH = "fdf8d7fefcac4a863babef1f9694772bf03e2f86e7cccdfbc73571d69cdbafc5";
    private static final String PATH = "/";
    private static final String URI_PARAM = "?operationId=opId";
    private static final String TOKEN_HEADER_NAME = "Token";
    private static final String TOKEN_HEADER_VALUE = "Token";
    private static final String RESPONSE_JSON = "{\"status\":\"success\",\"result\":[{\"signature\":\"" + OUTPUT_HASH + "\",}]}";
    private static final String PARAM_1_NAME = "param1";
    private static final String PARAM_1_VALUE = "value1";
    private static final String PARAM_2_NAME = "param2";
    private static final String PARAM_2_VALUE = "value2";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SomeService someService;

    @MockBean
    private SecretCipher secretCipher;

    @BeforeEach
    public void setUp() throws IllegalBlockSizeException, BadPaddingException {
        when(someService.doSomething(any())).thenReturn(List.of(new Result(OUTPUT_HASH)));
        when(secretCipher.decrypt(any())).thenReturn(TOKEN_HEADER_VALUE);
    }

    @Test
    public void testWithoutHeaderToken() throws Exception {
        mockMvc.perform(post(PATH + URI_PARAM)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param(PARAM_1_NAME, PARAM_1_VALUE)
                        .param(PARAM_2_NAME, PARAM_2_VALUE))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testWithInvalidHeaderToken() throws Exception {
        mockMvc.perform(post(PATH + URI_PARAM)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param(PARAM_1_NAME, PARAM_1_VALUE)
                        .param(PARAM_2_NAME, PARAM_2_VALUE)
                        .header(TOKEN_HEADER_NAME, "InvalidToken"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testIsOk() throws Exception {
        mockMvc.perform(post(PATH + URI_PARAM)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param(PARAM_1_NAME, PARAM_1_VALUE)
                        .param(PARAM_2_NAME, PARAM_2_VALUE)
                        .header(TOKEN_HEADER_NAME, TOKEN_HEADER_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string(RESPONSE_JSON));

        verify(secretCipher, times(1)).decrypt(any());
    }

    @Test
    public void testThatIgnoresIgnoredFields() throws Exception {
        when(someService.doSomething(any())).thenReturn(List.of(new ExpandedResult(OUTPUT_HASH, "hello")));

        mockMvc.perform(post(PATH + URI_PARAM)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param(PARAM_1_NAME, PARAM_1_VALUE)
                        .param(PARAM_2_NAME, PARAM_2_VALUE)
                        .header(TOKEN_HEADER_NAME, TOKEN_HEADER_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string(RESPONSE_JSON));

    }

    @Test
    public void testWithoutUriVariable() throws Exception {
        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param(PARAM_1_NAME, PARAM_1_VALUE)
                        .param(PARAM_2_NAME, PARAM_2_VALUE)
                        .header(TOKEN_HEADER_NAME, TOKEN_HEADER_VALUE))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testMultipleValueParams() throws Exception {
        mockMvc.perform(post(PATH + URI_PARAM)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param(PARAM_1_NAME, "hello")
                        .param(PARAM_2_NAME, PARAM_2_VALUE)
                        .param(PARAM_1_NAME, PARAM_1_VALUE)
                        .header(TOKEN_HEADER_NAME, TOKEN_HEADER_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string(RESPONSE_JSON));

    }

    static class ExpandedResult extends Result {

        @JsonIgnore
        private final String hello;

        public ExpandedResult(String signature, String hello) {
            super(signature);
            this.hello = hello;
        }
    }


}
