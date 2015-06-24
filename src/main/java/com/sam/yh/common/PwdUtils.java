package com.sam.yh.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public final class PwdUtils {
    private static final String PWD_REGEX = "^(\\w){8,20}$";

    public static boolean isValidPwd(String password) {
        if (StringUtils.isBlank(password)) {
            return false;
        }

        Pattern pattern = Pattern.compile(PWD_REGEX);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();

    }

    public static String genMd5Pwd(String userPhone, String salt, String password) {
        // return DigestUtils.md5Hex(userPhone + salt + password);
        return password;
    }

}
