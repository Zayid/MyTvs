package com.tvsauto.mytvs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ValidationUtil {

    public static boolean isValidUsername(String mob) {
        String NUM_PATTERN = "\\d{4}";
        Pattern pattern = Pattern.compile(NUM_PATTERN);
        Matcher matcher = pattern.matcher(mob);
        return matcher.matches();
    }
}
