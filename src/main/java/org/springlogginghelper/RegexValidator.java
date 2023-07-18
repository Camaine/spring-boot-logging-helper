package org.springlogginghelper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.springlogginghelper.context.ProvidedRegex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidator {

    public static JsonNode maskingIfRegexMatched(JsonNode jsonNode) {
        String value = jsonNode.asText();

        if(isPersonalIdentificationNumber(value)) {
            value = maskingPersonalIdentificationNumber(value);
            return new TextNode(value);
        }
        else if(isPhoneNumber(value)) {
            value = maskingPhoneNumber(value);
            return new TextNode(value);
        }

        return jsonNode;
    }



    private static boolean isPersonalIdentificationNumber(String value) {
        Pattern pattern = Pattern.compile(ProvidedRegex.KR_PERSONAL_IDENTIFICATION_NUMBER);
        Matcher matcher = pattern.matcher(value);
        if(matcher.find()) {
            return true;
        }
        return false;
    }

    private static boolean isPhoneNumber(String value) {
        Pattern pattern = Pattern.compile(ProvidedRegex.KR_PHONE_NUMBER);
        Matcher matcher = pattern.matcher(value);
        if(matcher.find()) {
            return true;
        }
        return false;
    }

    private static String maskingPersonalIdentificationNumber(String value) {
        if(value.length() == 13) { // 0000000000000
            return value.substring(0, 7) + "******";
        }
        if(value.length() == 14) { // 000000-00000000
            return value.substring(0, 8) + "******";
        }
        return value;
    }

    private static String maskingPhoneNumber(String value) {
        if(value.length() == 12) { // 00-0000-0000
            return value.substring(0, 3) + "****" + value.substring(7, 12);
        }
        if(value.length() == 13) { // 000-0000-0000
            return value.substring(0, 4) + "****" + value.substring(8, 13);
        }
        return value;
    }
}
