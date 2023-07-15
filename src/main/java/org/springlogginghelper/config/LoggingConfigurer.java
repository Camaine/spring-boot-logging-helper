package org.springlogginghelper.config;

import java.util.List;

public interface LoggingConfigurer {
    LoggingConfigurer addExcludeRequest(String attributeName);
    LoggingConfigurer addExcludeRequestList(List<String> attributeNameList);
    LoggingConfigurer addExcludeResponse(String attributeName);
    LoggingConfigurer addExcludeResponseList(List<String> attributeNameList);
    LoggingConfigurer addExcludeException(Object attributeName);
    LoggingConfigurer addExcludeExceptionList(List<Object> attributeNameList);
    LoggingConfigurer addIncludeHeader(String attributeName);
    LoggingConfigurer addIncludeHeaderList(List<String> attributeNameList);
}

