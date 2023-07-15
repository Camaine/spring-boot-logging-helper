package org.springlogginghelper.config;

import lombok.RequiredArgsConstructor;

import java.util.List;


/**
 * The LoggingConfiguration class provides methods for configuring the logging settings.
 * This class should be extended by your configuration class.
 */
@RequiredArgsConstructor
public class LoggingConfiguration implements LoggingConfigurer {

    private final ConfigDetails configDetails;

    // Exclude a specific attribute from request logging
    @Override
    public LoggingConfigurer addExcludeRequest(String attributeName) {
        configDetails.getExcludeRequestAttributes().add(attributeName);
        return null;
    }

    // Exclude a list of attributes from request logging
    @Override
    public LoggingConfigurer addExcludeRequestList(List<String> attributeNameList) {
        configDetails.getExcludeRequestAttributes().addAll(attributeNameList);
        return null;
    }

    // Exclude a specific attribute from response logging
    @Override
    public LoggingConfigurer addExcludeResponse(String attributeName) {
        configDetails.getExcludeResponseAttributes().add(attributeName);
        return null;
    }

    // Exclude a list of attributes from response logging
    @Override
    public LoggingConfigurer addExcludeResponseList(List<String> attributeNameList) {
        configDetails.getExcludeResponseAttributes().addAll(attributeNameList);
        return null;
    }

    // Exclude a specific exception class from logging
    @Override
    public LoggingConfigurer addExcludeException(Object exceptionClass) {
        configDetails.getExcludeExceptionAttributes().add(exceptionClass.getClass().getName());
        return null;
    }

    // Exclude a list of exception classes from logging
    @Override
    public LoggingConfigurer addExcludeExceptionList(List<Object> attributeNameList) {
        for(Object exceptionClass : attributeNameList)
            configDetails.getExcludeExceptionAttributes().add(exceptionClass.getClass().getName());
        return null;
    }

    // Include a specific header attribute in logging
    @Override
    public LoggingConfigurer addIncludeHeader(String attributeName) {
        configDetails.getIncludeHeaderAttributes().add(attributeName);
        return null;
    }

    // Include a list of header attributes in logging
    @Override
    public LoggingConfigurer addIncludeHeaderList(List<String> attributeNameList) {
        configDetails.getIncludeHeaderAttributes().addAll(attributeNameList);
        return null;
    }
}


