package com.semghh.ConsoleFormat;

/**
 * @author SemgHH
 */

public enum FontStyle {
    //
    RESET(0),
    //加粗
    BOLD(1),
    //减瘦
    THIN(2),
    //斜体
    ITALIC(3),
    //下划线
    Underline(4),
    //慢速闪烁
    SLOW_BLINK(5),
    //快速闪烁
    FAST_BLINK(6);

    final int styleCode;


    FontStyle(int styleCode) {
        this.styleCode = styleCode;
    }


}
