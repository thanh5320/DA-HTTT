package com.hust.movie_review.utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

import static com.hust.movie_review.common.Constant.SDF_FORMAT;

public class TimeUtils {
    public static Date convertOffsetDateTimeToDate(OffsetDateTime databaseObject) {
        long epochMilli = databaseObject.toInstant().toEpochMilli();
        return new Date(epochMilli);
    }

    public static OffsetDateTime convertDateToOffsetDateTime(Date userObject) {
        return userObject.toInstant().atOffset(ZoneOffset.UTC);
    }

    public static Integer countNumDay(Long fromDay, Long toDay) {
        return (int) ((fromDay - toDay) / (1000 * 60 * 60 * 24) + 1);
    }

    public static Date getDateFromTimeBeforeXDay(Date date, Integer numDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -numDay);
        return calendar.getTime();
    }

    public static Date longToDate(Long time){
        return new Date(time);
    }

    public static String convertDateToString(Date date) {
        Format formatter = new SimpleDateFormat(SDF_FORMAT);
        String str = formatter.format(date);
        return str;
    }
}
