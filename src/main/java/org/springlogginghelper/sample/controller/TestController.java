package org.springlogginghelper.sample.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springlogginghelper.Logging;
import org.springlogginghelper.LoggingException;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final LoggingException loggingException;

    @Logging
    @RequestMapping("/test")
    public String testEndpoint() {
        return "Test response";
    }

    @Logging
    @RequestMapping("/testEntity")
    public String testRequestEndpoint(@RequestBody TestModel testModel) {
        return "Test response";
    }

    @Logging
    @RequestMapping("/testException")
    public String testExceptionEndpoint() {
        throw new RuntimeException("This is a test exception.");
    }

    @Logging
    @RequestMapping("/testExceptionCatch")
    public String testExceptionCatchEndpoint() {
        try {
            throw new RuntimeException("This is a test exception catch.");
        } catch(RuntimeException e) {
            loggingException.logException(e);
        }
        return "test";
    }

}

