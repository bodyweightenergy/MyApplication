package com.example.hsalem.myapplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hsalem on 7/8/2014.
 */
public class IPAddressValidator {
    private static final String PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public static boolean validate(final String ip){

        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }
}
