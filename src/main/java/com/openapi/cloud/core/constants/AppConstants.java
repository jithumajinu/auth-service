package com.openapi.cloud.core.constants;

import java.time.format.DateTimeFormatter;

public class AppConstants {

    public static final String CHARSET = "application/json; charset=UTF-8";
    public static final String UTF8 = "UTF-8";
    public static final String AUTH = "authorization";
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int SUCCESS = 200;
    public static final String DF_YYYY_MM_DD = "yyyy/MM/dd";
    public static final String DF_YYYYMMDD = "yyyy-MM-dd";
    public static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern(DF_YYYY_MM_DD);
    public static final DateTimeFormatter YYYYMMDDDASH = DateTimeFormatter.ofPattern(DF_YYYYMMDD);
    public static final String PHONE_REGEX = "^\\+?[0-9]\\d{1,14}$";

    private AppConstants() {
    }
}
