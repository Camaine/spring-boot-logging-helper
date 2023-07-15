package org.springlogginghelper;

import lombok.RequiredArgsConstructor;
import org.springlogginghelper.config.ConfigDetails;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class EncryptionDecoder {

    private final ConfigDetails configDetails;
    public static String decodeBase64(String str) {
        if(isBase64(str) == false) {
            return str;
        }
        byte[] decodedBytes = java.util.Base64.getDecoder().decode(str);
        return new String(decodedBytes);
    }

    private static boolean isBase64(String value) {

        Pattern pattern = Pattern.compile("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$");
        Matcher matcher = pattern.matcher(value);
        if(matcher.find()) {
            return true;
        }

        return false;
    }
}
