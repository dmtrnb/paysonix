package com.example.paysonix.service;

import com.example.paysonix.domain.response.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SomeServiceTest {

    private static final String OUTPUT_HASH = "fdf8d7fefcac4a863babef1f9694772bf03e2f86e7cccdfbc73571d69cdbafc5";
    private static final Map<String, String> PARAMS_MAP = Map.of("param1", "value1", "param2", "value2");

    @Autowired
    private SomeService service;

    @Test
    public void testIsOk() {
        List<Result> results = service.doSomething(PARAMS_MAP);

        assertEquals(1, results.size());
        assertEquals(OUTPUT_HASH, results.get(0).getSignature());
    }

    @Test
    public void testEmptyParams() {
        List<Result> results = service.doSomething(Map.of());

        assertEquals(0, results.size());
    }
}
