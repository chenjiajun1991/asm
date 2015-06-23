package com.sam.yh.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class EmailAddressValidator {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

    public static boolean isValidEmail(String emailAddress) {
        if (StringUtils.isBlank(emailAddress)) {
            return false;
        }

        Pattern p = Pattern.compile(EMAIL_PATTERN);
        Matcher m = p.matcher(emailAddress);
        return m.matches();
    }

}
