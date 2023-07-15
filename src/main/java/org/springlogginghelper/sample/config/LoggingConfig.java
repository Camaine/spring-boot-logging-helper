package org.springlogginghelper.sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springlogginghelper.EnableLogging;
import org.springlogginghelper.config.ConfigDetails;
import org.springlogginghelper.config.LoggingConfiguration;

import java.util.List;

@EnableLogging
@Configuration
public class LoggingConfig extends LoggingConfiguration{
    public LoggingConfig(ConfigDetails configDetails) {
        super(configDetails);
    }

    @Bean
    public void attributeExclude() {
        //Single Attribute Exclude
        addExcludeRequest("testAttributeExclude");
        addExcludeResponse("test");
        addExcludeException(new RuntimeException());

        //Multi Attribute Exclude
        addExcludeRequestList(List.of("testAttributeExclude1", "testAttributeExclude2"));
        addExcludeResponseList(List.of("test1", "test2"));
        addExcludeExceptionList(List.of(new RuntimeException(), new IllegalArgumentException()));
    }

    @Bean
    public void addHeaderAttribute() {
        //Single Header Attribute Exclude
        addIncludeHeader("test-header");

        //Multi Header Attribute Exclude
        addIncludeHeaderList(List.of("test-header1", "test-header2"));
    }
}

