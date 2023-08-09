package org.springlogginghelper.sample.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springlogginghelper.Logging;
import org.springlogginghelper.LoggingException;
import org.springlogginghelper.sample.event.CustomEvent;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Logging
    @RequestMapping("/test")
    public void testEndpoint() {
        //return "Test response";
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

    @RequestMapping("/testEventPublish")
    public String testEventPublishEndpoint() {
        applicationEventPublisher.publishEvent(new CustomEvent("Test event message"));
        return "Test response";
    }

    @Logging
    @RequestMapping("/testExceptionCatch")
    public String testExceptionCatchEndpoint() {
        try {
            throw new RuntimeException("This is a test exception catch.");
        } catch(RuntimeException e) {
            LoggingException.logException(e);
        }
        return "test";
    }

}

