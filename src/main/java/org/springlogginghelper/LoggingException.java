package org.springlogginghelper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoggingException {
    public static void logException(Exception e) {
        LogDetails logDetails = LogDetailExtractor.fromThrowable(e);
        JSONLogging.exceptionThrowLogging(logDetails);
    }
}
