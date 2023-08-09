package org.springlogginghelper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoggingCustom {

    public static void logCustom(String logType, String logMessage) {
        LogDetails logDetails = LogDetailExtractor.fromMessage(logType, logMessage);
        JSONLogging.customLogging(logDetails);
    }
}
