package org.springlogginghelper.context;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProvidedRegex {
    public static String KR_PERSONAL_IDENTIFICATION_NUMBER = "(\\d{6}[ ,-]-?[1-4]\\d{6})|(\\d{6}[ ,-]?[1-4])";
    public static String KR_PHONE_NUMBER = "(\\d{2,3}[ ,-]-?\\d{2,4}[ ,-]-?\\d{4})";
}
