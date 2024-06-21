package com.rbc.zfe0.road.services.transferagent.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static String getCurrentDateReturnString() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        TimeZone inZone = calendar.getTimeZone();
        String pattern = "yyyy-MM-dd hh:mm:ss a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String createDateString = simpleDateFormat.format(new Date());
        TimeZone outZone = TimeZone.getTimeZone("CST");
        simpleDateFormat.setTimeZone(outZone);

        return simpleDateFormat.format(calendar.getTime());

    }

    public static Date getCurrentDateReturnDate() throws Exception {
        Date retDate = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        TimeZone inZone = calendar.getTimeZone();

        String pattern = "yyyy-MM-dd hh:mm:ss a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String createDateString = simpleDateFormat.format(new Date());
        TimeZone outZone = TimeZone.getTimeZone("CST");
        simpleDateFormat.setTimeZone(outZone);

        String strDate = simpleDateFormat.format(calendar.getTime());
        retDate = simpleDateFormat.parse(strDate);

        return retDate;
    }
}
