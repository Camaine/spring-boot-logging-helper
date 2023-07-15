package org.springlogginghelper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoggingException {
    private final JSONLogging jsonLogging;
    private final LogDetailExtractor logDetailExtractor;

    public void logException(Exception e) {
        LogDetails logDetails = logDetailExtractor.fromThrowable(e);
        jsonLogging.exceptionThrowLogging(logDetails);
    }
}
