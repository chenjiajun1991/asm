package com.sam.yh.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class MobilePhoneUtils {

    private static final String PHONE_REGEX = "^1[0-9]{10}$";

    public static boolean isValidPhone(String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber)) {
            return false;
        }

        Pattern pattern = Pattern.compile(PHONE_REGEX);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

}
