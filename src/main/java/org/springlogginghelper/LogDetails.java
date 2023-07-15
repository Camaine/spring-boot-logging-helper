package org.springlogginghelper;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class LogDetails {
    private String version;
    private String logType;
    private String level;
    private String timestamp;
    private String reqId;
    private String reqUserId;
    private String requestBody;
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

    public void setVersion(String version) {
        this.version = version;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public void setReqUserId(String reqUserId) {
        this.reqUserId = reqUserId;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    public void setBrowserLang(String browserLang) {
        this.browserLang = browserLang;
    }

    public void setClientTimestamp(String clientTimestamp) {
        this.clientTimestamp = clientTimestamp;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setExceptionStackTrace(String exceptionStackTrace) {
        this.exceptionStackTrace = exceptionStackTrace;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}

