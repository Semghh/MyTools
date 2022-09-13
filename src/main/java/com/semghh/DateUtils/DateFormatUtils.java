package com.semghh.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * @author SemgHH
 * @2022年9月9日11点03分
 */
public class DateFormatUtils {

    public static final int YEAR = Calendar.YEAR;
    public static final int MONTH = Calendar.MONTH;
    public static final int DATE = Calendar.DATE;

    public static final int HOUR = Calendar.HOUR;

    public static final int MINTER = Calendar.MINUTE;

    public static final int SECOND = Calendar.SECOND;

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


    public static Date parseDateString(FormatPattern pattern, String text) {
        switch (pattern) {
            case hyphen_1 -> {
                String[] s = text.split(" ");
                if (s.length == 2) {
                    String[] part1 = s[0].split("-");
                    String[] part2 = s[1].split(":");
                    if (part1.length == 3 && part2.length == 3) {
                        return DateFormatUtils.adjustDate().set(YEAR, Integer.parseInt(part1[0]))
                                .set(MONTH, Integer.parseInt(part1[1])-1)
                                .set(DATE, Integer.parseInt(part1[2]))
                                .set(HOUR, Integer.parseInt(part2[0]))
                                .set(MINTER, Integer.parseInt(part2[1]))
                                .set(SECOND, Integer.parseInt(part2[2])).getDate();
                    }
                }
            }
            case hyphen_2 -> {
                String[] part1 = text.split("-");
                if (part1.length == 3) {
                    return DateFormatUtils.adjustDate().set(YEAR, Integer.parseInt(part1[0]))
                            .set(MONTH, Integer.parseInt(part1[1])-1)
                            .set(DATE, Integer.parseInt(part1[2]))
                            .getDate();
                }
            }
            case underline_1 -> {
                String[] s = text.split(" ");
                if (s.length == 2) {
                    String[] part1 = s[0].split("_");
                    String[] part2 = s[1].split(":");
                    if (part1.length == 3 && part2.length == 3) {
                        return DateFormatUtils.adjustDate().set(YEAR, Integer.parseInt(part1[0]))
                                .set(MONTH, Integer.parseInt(part1[1])-1)
                                .set(DATE, Integer.parseInt(part1[2]))
                                .set(HOUR, Integer.parseInt(part2[0]))
                                .set(MINTER, Integer.parseInt(part2[1]))
                                .set(SECOND, Integer.parseInt(part2[2])).getDate();
                    }
                }
            }
            case underline_2 -> {
                String[] part1 = text.split("_");
                if (part1.length == 3) {
                    return DateFormatUtils.adjustDate().set(YEAR, Integer.parseInt(part1[0]))
                            .set(MONTH, Integer.parseInt(part1[1])-1)
                            .set(DATE, Integer.parseInt(part1[2]))
                            .getDate();
                }
            }
            default ->{
                return null;
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
            calendar.set(mode, calendar.get(mode) + amount);
            return this;
        }

        public DateOperation forward(int mode, int amount) {
            calendar.set(mode, calendar.get(mode) - amount);
            return this;
        }

        public long getTimeInMillis() {
            return calendar.getTimeInMillis();
        }

        public Date getDate() {
            return calendar.getTime();
        }

        public String formatByPattern(String pattern) {
            return DateFormatUtils.formatByPattern(pattern, calendar.getTime());
        }


        public DateOperation set(int mode, int value) {
            if (mode==MONTH){
                calendar.set(MONTH,value-1);
            }else {
            calendar.set(mode, value);

            }
            return this;
        }
    }


}
