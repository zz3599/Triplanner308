/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author brook
 */
public class DateUtils {

    private static final SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
    private static final SimpleDateFormat inputformat = new SimpleDateFormat("yyyy-MM-dd");

    public static Timestamp toTimestamp(String string) {
        Timestamp result = null;
        try {
            result = Timestamp.valueOf(string);
        } catch (Exception e) {
            try {
                result = new Timestamp(inputformat.parse(string).getTime());
            } catch (Exception ex) {
            }
        }
        return result;
    }

    public static String toDate(Date timestamp) {
        return dateformat.format(timestamp.getTime());
    }

    public static int diffDays(Timestamp a, Timestamp b) {
        long adays = TimeUnit.MILLISECONDS.toDays(a.getTime());
        long bdays = TimeUnit.MILLISECONDS.toDays(b.getTime());
        return (int) (bdays - adays);
    }

    public static Timestamp incrementDay(Timestamp expectedDt, int numdays) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(expectedDt.getTime());
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        //cal.set(Calendar.AM_PM, Calendar.AM);
        cal.add(Calendar.DAY_OF_MONTH, numdays);
        expectedDt = new Timestamp(cal.getTime().getTime());
        return expectedDt;
    }

    public static boolean inRange(Timestamp time, Timestamp start, Timestamp end) {
        long timestart = start.getTime();
        long timeend = end.getTime();
        long curTime = time.getTime();
        return curTime >= timestart && curTime <= timeend;
    }
}
