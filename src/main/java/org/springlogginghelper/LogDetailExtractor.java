package org.springlogginghelper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final Environment env;

    private final ConfigDetails configDetails;

    public LogDetailExtractor(Environment env, ConfigDetails configDetails) {
        this.env = env;
        this.configDetails = configDetails;
    }

    public LogDetails fromRequest(Method method, HttpServletRequest request) {

        LogDetails logDetails = new LogDetails();

        try {
            request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        } catch (IllegalStateException ignored) {}

        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = EncryptionDecoder.decodeBase64(request.getHeader(headerName));
                // check if header is in excluded headers list
                if (!configDetails.getExcludeRequestAttributes().contains(headerName)
                        && configDetails.getIncludeHeaderAttributes().contains(headerName)) {
                    logDetails.addHeader(headerName, headerValue);
                }
            }
        }

        JSONLogging.setTrxId();

        // Extract values
        String packageName = method.getDeclaringClass().getName();
        String methodName = method.getName();
        String osType = extractOsType(request);
        String browserType = extractBrowserType(request);
        String browserLang = extractBrowserLang(request);
        String clientTimestamp = getCurrentTimestamp();

        // Populate LogDetails object

        logDetails.setVersion("1.0.0");
        logDetails.setLogType("REQUEST");
        logDetails.setLevel("INFO");
        logDetails.setTimestamp(getCurrentTimestamp());
        logDetails.setReqId(JSONLogging.getTrxId());
        logDetails.setReqUserId("defaultUser");
        logDetails.setRequestBody(getRequestPayload(request));
        logDetails.setMessage("Executing method: " + methodName);
        logDetails.setPackageName(packageName);
        logDetails.setEnvironment(getEnvironment());
        logDetails.setOsType(osType);
        logDetails.setBrowserType(browserType);
        logDetails.setBrowserLang(browserLang);
        logDetails.setClientTimestamp(clientTimestamp);

        return logDetails;
    }


    public LogDetails fromResponse(Method method, Object result, HttpServletResponse response) {
        // Obtain required information
        String packageName = method.getDeclaringClass().getName();
        String methodName = method.getName();
        String statusCode = Integer.toString(response.getStatus());

        // Populate LogDetails object
        LogDetails logDetails = new LogDetails();
        logDetails.setVersion("1.0.0");
        logDetails.setLogType("RESPONSE");
        logDetails.setLevel("INFO");
        logDetails.setTimestamp(getCurrentTimestamp());
        logDetails.setReqId(JSONLogging.getTrxId());
        logDetails.setReqUserId("defaultUser");
        logDetails.setResponseBody(result.toString()); // Depending on your response type, you may want to format this differently
        logDetails.setMessage("Method executed: " + methodName);
        logDetails.setPackageName(packageName);
        logDetails.setEnvironment(getEnvironment());
        logDetails.setStatus(statusCode);

        return logDetails;
    }


    public LogDetails fromThrowable(Throwable error) {

        if(configDetails.getExcludeExceptionAttributes().contains(error.getClass().getName())){
            return null;
        }

        // Values that could be constants
        String logType = "EXCEPTION";
        String level = "ERROR";

        // Message and user id - placeholders for now
        String message = error.getMessage();
        String reqUserId = "defaultUser";

        // Values that might need to be generated on the fly
        String version = getVersion();
        String timestamp = getCurrentTimestamp();
        String reqId = getRequestId();
        String clientTimestamp = getCurrentTimestamp();
        String status = "500"; // HTTP status 500 indicates server error

        // Stack trace of the exception
        String exceptionStackTrace = error.getMessage();

        LogDetails logDetails = new LogDetails();
        logDetails.setVersion(version);
        logDetails.setLogType(logType);
        logDetails.setLevel(level);
        logDetails.setTimestamp(timestamp);
        logDetails.setReqId(reqId);
        logDetails.setReqUserId(reqUserId);
        logDetails.setMessage(message);
        logDetails.setEnvironment(getEnvironment());
        logDetails.setClientTimestamp(clientTimestamp);
        logDetails.setStatus(status);
        logDetails.setExceptionStackTrace(exceptionStackTrace);

        return logDetails;
    }

    private String getApiStatus(HttpServletResponse response) {
        if (response != null) {
            return String.valueOf(response.getStatus());
        }
        return "200";
    }

    private String getEnvironment() {
        String[] activeProfiles = env.getActiveProfiles();
        String environment = "local";
        if (activeProfiles.length > 0) {
            environment = activeProfiles[0];
        }
        return environment;
    }

    private String getVersion() {
        // Replace this with your actual version retrieval logic
        return "1.0.0";
    }

    private String getCurrentTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date());
    }

    private String getTransactionId() {
        // This is a placeholder. Replace with actual transaction ID generation logic
        return UUID.randomUUID().toString();
    }

    private String getRequestId() {
        // This is a placeholder. Replace with actual request ID generation logic
        return UUID.randomUUID().toString();
    }

    // These are placeholder methods, replace with actual implementation
    private String extractOsType(HttpServletRequest request) {
        return "Linux"; // Placeholder
    }

    private String extractBrowserType(HttpServletRequest request) {
        return "Chrome"; // Placeholder
    }

    private String extractBrowserLang(HttpServletRequest request) {
        return "en-US"; // Placeholder
    }

    private String getStackTrace(Throwable error) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        error.printStackTrace(pw);
        return sw.toString();
    }

    private String getRequestPayload(HttpServletRequest request) {
        String requestBody = (String) request.getAttribute("requestBody");
        return requestBodyParser(requestBody);
    }


    private String requestBodyParser(String request) {
        ObjectMapper objectMapper = new ObjectMapper();
        StringBuilder modifiedRequest = new StringBuilder();
        try {
            JsonNode jsonNode = objectMapper.readTree(request);
            parseNode(jsonNode, modifiedRequest, "", true);
        } catch (IOException e) {
            // Handle the exception...
            log.error(e.getMessage());
        }
        return modifiedRequest.toString();
    }

    private void parseNode(JsonNode jsonNode, StringBuilder modifiedRequest, String parentKey, boolean isRoot) {
        if (jsonNode.isObject()) {
            modifiedRequest.append("{\n");
            Iterator<Map.Entry<String, JsonNode>> iterator = jsonNode.fields();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> entry = iterator.next();
                String key = entry.getKey();
                //String key = isRoot ? entry.getKey() : parentKey + "." + entry.getKey();
                if(!configDetails.getExcludeRequestAttributes().contains(key)){
                    modifiedRequest.append("\"").append(key).append("\": ");
                    parseNode(RegexValidator.maskingIfRegexMatched(entry.getValue()), modifiedRequest, key, false);
                    if (iterator.hasNext()) {
                        modifiedRequest.append(",\n");
                    }
                }
            }
            modifiedRequest.append("\n}");
        } else {
            modifiedRequest.append("\"").append(jsonNode.asText()).append("\"");
        }
    }


}
