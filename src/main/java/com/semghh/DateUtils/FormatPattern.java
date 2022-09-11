package com.semghh.DateUtils;

import java.util.Date;

/**
 *
 * 枚举类。 列出 常见的日期格式化的方式
 * @author SemgHH
 */
public enum FormatPattern {
    /**
     *
     * 用于DateFormatUtils工具类中的部分方法： 例如
     * @see DateFormatUtils#formatByPattern(FormatPattern, Date)
     * @see DateFormatUtils#parseDateString(FormatPattern, String)
     */
    hyphen_1("yyyy-MM-dd HH:mm:ss"),
    hyphen_2("yyyy-MM-dd"),
    underline_1("yyyy_MM_dd HH:mm:ss"),
    underline_2("yyyy_MM_dd")
    ;


    final String pattern;

    FormatPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
