package com.semghh.DateUtils;

import java.util.Date;

import static com.semghh.DateUtils.DateFormatUtils.*;

/**
 * @author SemgHH
 */
public class Sample {

    public static void main(String[] args) {

        //format日期
        String format = DateFormatUtils.formatByPattern("yyyy MM dd");
        System.out.println(format);

        //FormatPattern类提供了常用的format格式
        String format1 = DateFormatUtils.formatByPattern(FormatPattern.hyphen_1,new Date());
        System.out.println(format1);


        //任意调整日期
        Date date = adjustDate().set(YEAR, 2022)
                .set(MONTH, 9)
                .set(DATE, 13)
                .getDate(); //2022-9-13

        String format2 = DateFormatUtils.formatByPattern(FormatPattern.hyphen_1, date);
        System.out.println(format2);


        //延后1天
        Date delay = DateFormatUtils.adjustDate(date).delay(DATE, 1).getDate();
        String format3 = formatByPattern(FormatPattern.hyphen_1, delay);
        //2022-09-14
        System.out.println(format3);


        //提前1天
        Date forward = adjustDate(date).forward(DATE, 1).getDate();
        String format4 = formatByPattern(FormatPattern.hyphen_1, forward);
        //2022-09-12
        System.out.println(format4);


    }
}
