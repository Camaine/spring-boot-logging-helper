package org.springlogginghelper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springlogginghelper.context.JSONContext;

import java.util.Random;
import java.util.UUID;

@Component
public class JSONLogging {

    private static final ThreadLocal<String> TRX_CONTEXT = ThreadLocal.withInitial(() -> Strings.EMPTY);
    private static final Logger logger = LoggerFactory.getLogger(LoggingAnnotationAdvisor.class);

    public String requestLogging(LogDetails logDetails) {
        JsonObject jsonObject = createJsonRequestObject(logDetails);
        logMessage(jsonObject,logDetails.getLevel());

        return jsonObject.toString().concat("\r\n");
    }

    public String responseLogging(LogDetails logDetails) {
        JsonObject jsonObject = createJsonResponseObject(logDetails);
        logMessage(jsonObject,logDetails.getLevel());

        return jsonObject.toString().concat("\r\n");
    }

    public static String exceptionThrowLogging(LogDetails logDetails) {
        if(logDetails == null) {
            return Strings.EMPTY;
        }
        JsonObject jsonObject = createJsonExceptionObject(logDetails);
        logMessage(jsonObject,logDetails.getLevel());

        return jsonObject.toString().concat("\r\n");
    }

    public static String customLogging(LogDetails logDetails) {
        if(logDetails == null) {
            return Strings.EMPTY;
        }
        JsonObject jsonObject = createJsonCustomObject(logDetails);
        logMessage(jsonObject,logDetails.getLevel());

        return jsonObject.toString().concat("\r\n");
    }

    private JsonObject createJsonRequestObject(LogDetails logDetails) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty(JSONContext.VERSION, logDetails.getVersion());
        jsonObject.addProperty(JSONContext.LOG_TYPE, logDetails.getLogType());
        jsonObject.addProperty(JSONContext.LEVEL, logDetails.getLevel());
        jsonObject.addProperty(JSONContext.TIMESTAMP, logDetails.getTimestamp());

        for(String key : logDetails.getHeaders().keySet()) {
            jsonObject.addProperty(key, logDetails.getHeaders().get(key));
        }

        jsonObject.addProperty(JSONContext.REQ_ID, logDetails.getReqId());
        jsonObject.add(JSONContext.REQUEST_BODY, logDetails.getRequestBody());
        jsonObject.addProperty(JSONContext.MESSAGE, logDetails.getMessage());
        jsonObject.addProperty(JSONContext.PACKAGE_NAME, logDetails.getPackageName());

        return jsonObject;
    }

    private JsonObject createJsonResponseObject(LogDetails logDetails) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty(JSONContext.VERSION, logDetails.getVersion());
        jsonObject.addProperty(JSONContext.LOG_TYPE, logDetails.getLogType());
        jsonObject.addProperty(JSONContext.LEVEL, logDetails.getLevel());
        jsonObject.addProperty(JSONContext.TIMESTAMP, logDetails.getTimestamp());
        jsonObject.addProperty(JSONContext.REQ_ID, logDetails.getReqId());
        jsonObject.addProperty(JSONContext.MESSAGE, logDetails.getMessage());
        jsonObject.addProperty(JSONContext.PACKAGE_NAME, logDetails.getPackageName());
        jsonObject.addProperty(JSONContext.STATUS, logDetails.getStatus());

        return jsonObject;
    }

    public static JsonObject createJsonExceptionObject(LogDetails logDetails) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty(JSONContext.VERSION, logDetails.getVersion());
        jsonObject.addProperty(JSONContext.LOG_TYPE, logDetails.getLogType());
        jsonObject.addProperty(JSONContext.LEVEL, logDetails.getLevel());
        jsonObject.addProperty(JSONContext.TIMESTAMP, logDetails.getTimestamp());
        jsonObject.addProperty(JSONContext.REQ_ID, logDetails.getReqId());
        jsonObject.addProperty(JSONContext.PACKAGE_NAME, logDetails.getPackageName());
        jsonObject.addProperty(JSONContext.STATUS, logDetails.getStatus());
        jsonObject.addProperty(JSONContext.EXCEPTION_STACK_TRACE, logDetails.getExceptionStackTrace());

        return jsonObject;
    }

    public static JsonObject createJsonCustomObject(LogDetails logDetails) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty(JSONContext.VERSION, logDetails.getVersion());
        jsonObject.addProperty(JSONContext.LOG_TYPE, logDetails.getLogType());
        jsonObject.addProperty(JSONContext.LEVEL, logDetails.getLevel());
        jsonObject.addProperty(JSONContext.TIMESTAMP, logDetails.getTimestamp());
        jsonObject.addProperty(JSONContext.REQ_ID, logDetails.getReqId());
        jsonObject.addProperty(JSONContext.PACKAGE_NAME, logDetails.getPackageName());
        jsonObject.addProperty(JSONContext.MESSAGE, logDetails.getMessage());
        jsonObject.addProperty(JSONContext.STATUS, logDetails.getStatus());

        return jsonObject;
    }

    private static void logMessage(JsonObject jsonObject, String level) {
        Gson gson = new Gson();
        if(level.equals("ERROR")) {
            logger.error(gson.toJson(jsonObject));
            return;
        }
        logger.info(gson.toJson(jsonObject));
    }

    public static void setTrxId() {
        TRX_CONTEXT.set(generateMessageId(35));
    }

    public static String getTrxId() {
        return TRX_CONTEXT.get();
    }

    private static String generateMessageId(int size) {
        StringBuilder messageId = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < size; i++) {
            int r = rnd.nextInt(3);
            switch (r) {
                case 0: // a-z
                    messageId.append((char) ((rnd.nextInt(26)) + 97));
                    break;
                case 1: // A-Z
                    messageId.append((char) ((rnd.nextInt(26)) + 65));
                    break;
                case 2: // 0-9
                    messageId.append((rnd.nextInt(10)));
                    break;
                default:
                    messageId.append(UUID.randomUUID());
                    break;
            }
        }
        return messageId.toString();
    }

}


