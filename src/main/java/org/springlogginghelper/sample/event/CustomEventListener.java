package org.springlogginghelper.sample.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springlogginghelper.Logging;
import org.springlogginghelper.LoggingCustom;

@Component
@RequiredArgsConstructor
public class CustomEventListener {

    @Logging
    @EventListener
    public void handleCustomEvent(CustomEvent event) {
        // Handle the event
        LoggingCustom.logCustom("Listener", event.getMessage());
    }
}
