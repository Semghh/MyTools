package com.semghh.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @2022年9月9日11点03分
 * @author SemgHH
 */
public class DateFormatUtils {

    public static final int YEAR = Calendar.YEAR;
    public static final int MONTH = Calendar.MONTH;
    public static final int DATE = Calendar.DATE;

    /**
     * 按照指定格式format日期
     */
    public static String formatByPattern(String pattern, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static String formatByPattern(FormatPattern pattern, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern.getPattern());
        return sdf.format(date);
    }


    public static String formatByPattern(String pattern) {
        return formatByPattern(pattern, new Date());
    }

    /**
     * 前后调整日期
     */
    public static DateOperation adjustDate(Date date) {
        return new DateOperation(date);
    }

    public static DateOperation adjustDate() {
        return DateFormatUtils.adjustDate(new Date());
    }


    public static Date parseDateString(FormatPattern pattern,String text){
        switch (pattern){
            case hyphen_1 -> {

            }

            default -> {

            }
        }
        return null;
    }







    public static class DateOperation {
        Calendar calendar;
        public DateOperation(Date base) {
            calendar = Calendar.getInstance();
            calendar.setTime(base);
        }

        public DateOperation delay(int mode, int amount) {
            calendar.set(mode,calendar.get(mode)+amount);
            return this;
        }
        public DateOperation forward(int mode,int amount){
            calendar.set(mode,calendar.get(mode)-amount);
            return this;
        }

        public long getTimeInMillis(){
            return calendar.getTimeInMillis();
        }

        public Date getDate(){
            return calendar.getTime();
        }
        public String formatByPattern(String pattern){
            return DateFormatUtils.formatByPattern(pattern,calendar.getTime());
        }


        public DateOperation set(int mode,int value){
            calendar.set(mode,value);
            return this;
        }
    }


}
