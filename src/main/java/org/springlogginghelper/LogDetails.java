package org.springlogginghelper;

import com.google.gson.JsonElement;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class LogDetails {
    private String version;
    private String logType;
    private String level;
    private String timestamp;
    private String reqId;
    private String reqUserId;
    private JsonElement requestBody;
    private String responseBody;
    private String message;
    private String packageName;
    private String environment;
    private String osType;
    private String browserType;
    private String browserLang;
    private String clientTimestamp;
    private String status;
    private String exceptionStackTrace;
    private Map<String, String> headers = new HashMap<>();

    public void addHeader(String headerName, String headerValue) {
        headers.put(headerName, headerValue);
    }

}

