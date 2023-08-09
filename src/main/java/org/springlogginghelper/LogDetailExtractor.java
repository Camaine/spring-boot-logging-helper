package org.springlogginghelper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springlogginghelper.config.ConfigDetails;
import org.springframework.core.env.Environment;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;


@Slf4j
public class LogDetailExtractor {
    private static final String DEFAULT_VERSION = "1.0.0";
    private static final String DEFAULT_LOG_LEVEL = "INFO";
    private static final String EXCEPTION_LOG_LEVEL = "ERROR";
    private static final String DEFAULT_ENVIRONMENT = "local";
    private static final String TIMESTAMP_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private static Environment env = null;
    private static ConfigDetails configDetails = new ConfigDetails();
    private final ObjectMapper objectMapper;

    public LogDetailExtractor(Environment env, ConfigDetails configDetails, ObjectMapper objectMapper) {
        this.env = env;
        this.configDetails = configDetails;
        this.objectMapper = objectMapper;
    }

    public LogDetails fromRequest(Method method, HttpServletRequest request) {
        LogDetails logDetails = new LogDetails();
        logDetails.setLevel(DEFAULT_LOG_LEVEL);
        populateCommonFields(logDetails, method);
        extractHeadersFromRequest(request, logDetails);
        logDetails.setRequestBody(getRequestPayload(request));
        return logDetails;
    }

    public LogDetails fromResponse(Method method, Object result, HttpServletResponse response) {
        LogDetails logDetails = new LogDetails();
        populateCommonFields(logDetails, method);
        logDetails.setVersion(DEFAULT_VERSION);
        logDetails.setLevel(DEFAULT_LOG_LEVEL);
        logDetails.setResponseBody(result.toString());
        logDetails.setStatus(Integer.toString(response.getStatus()));
        return logDetails;
    }

    public static LogDetails fromThrowable(Throwable error) {
        if(configDetails.getExcludeExceptionAttributes().contains(error.getClass().getName())){
            return null;
        }

        LogDetails logDetails = new LogDetails();
        populateExceptionFields(logDetails, error);
        return logDetails;
    }

    public static LogDetails fromMessage(String logType, String message) {
        LogDetails logDetails = new LogDetails();
        populateMessageFields(logDetails, logType, message);
        return logDetails;
    }

    private void populateCommonFields(LogDetails logDetails, Method method) {
        logDetails.setTimestamp(getCurrentTimestamp());
        logDetails.setReqId(JSONLogging.getTrxId());
        logDetails.setPackageName(method.getDeclaringClass().getName());
        logDetails.setEnvironment(getEnvironment());
    }

    private static void populateExceptionFields(LogDetails logDetails, Throwable error) {
        logDetails.setVersion(getVersion());
        logDetails.setLogType("EXCEPTION");
        logDetails.setLevel(EXCEPTION_LOG_LEVEL);
        logDetails.setTimestamp(getCurrentTimestamp());
        logDetails.setReqId(getRequestId());
        logDetails.setMessage(error.getMessage());
        logDetails.setEnvironment(getEnvironment());
        logDetails.setClientTimestamp(getCurrentTimestamp());
        logDetails.setStatus("500");
        logDetails.setExceptionStackTrace(getStackTrace(error));
    }

    private static void populateMessageFields(LogDetails logDetails, String logType, String message) {
        logDetails.setVersion(getVersion());
        logDetails.setLogType(logType);
        logDetails.setLevel("INFO");
        logDetails.setTimestamp(getCurrentTimestamp());
        logDetails.setReqId(getRequestId());
        logDetails.setMessage(message);
        logDetails.setEnvironment(getEnvironment());
        logDetails.setClientTimestamp(getCurrentTimestamp());
        logDetails.setStatus("200");
    }

    private void extractHeadersFromRequest(HttpServletRequest request, LogDetails logDetails) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = EncryptionDecoder.decodeBase64(request.getHeader(headerName));
            if (!configDetails.getExcludeRequestAttributes().contains(headerName) && configDetails.getIncludeHeaderAttributes().contains(headerName)) {
                logDetails.addHeader(headerName, headerValue);
            }
        }
    }

    private static String getCurrentTimestamp() {
        return new SimpleDateFormat(TIMESTAMP_PATTERN).format(new Date());
    }

    private static String getEnvironment() {
        String[] activeProfiles = env.getActiveProfiles();
        return activeProfiles.length > 0 ? activeProfiles[0] : DEFAULT_ENVIRONMENT;
    }

    private static String getVersion() {
        return DEFAULT_VERSION; // You might want to change this based on where you get the actual version from.
    }

    private static String getRequestId() {
        return UUID.randomUUID().toString(); // This is a placeholder. You might want to change this.
    }

    private static String getStackTrace(Throwable error) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        error.printStackTrace(pw);
        return sw.toString();
    }

    private JsonElement getRequestPayload(HttpServletRequest request) {
        String requestBody = (String) request.getAttribute("requestBody");
        return requestBodyParser(requestBody);
    }

    private JsonElement requestBodyParser(String request) {
        StringBuilder modifiedRequest = new StringBuilder();
        try {
            JsonNode jsonNode = objectMapper.readTree(request);
            parseNode(jsonNode, modifiedRequest);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return new JsonParser().parse(modifiedRequest.toString());
    }

    private void parseNode(JsonNode jsonNode, StringBuilder modifiedRequest) {
        if (jsonNode.isObject()) {
            modifiedRequest.append("{");
            Iterator<Map.Entry<String, JsonNode>> iterator = jsonNode.fields();

            boolean firstElementProcessed = false; // This flag will be used to check if we need to append comma or not.

            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> entry = iterator.next();
                String key = entry.getKey();

                if (!configDetails.getExcludeRequestAttributes().contains(key)) {

                    // Appending comma if it's not the first element being processed
                    if (firstElementProcessed) {
                        modifiedRequest.append(",");
                    } else {
                        firstElementProcessed = true;
                    }

                    modifiedRequest.append("\"").append(key).append("\":");

                    if (entry.getValue().isObject()) {
                        parseNode(RegexValidator.maskingIfRegexMatched(entry.getValue()), modifiedRequest);
                    } else {
                        modifiedRequest.append(RegexValidator.maskingIfRegexMatched(entry.getValue()));
                    }
                }
            }

            modifiedRequest.append("}");
        } else {
            modifiedRequest.append(RegexValidator.maskingIfRegexMatched(jsonNode));
        }
    }


}
