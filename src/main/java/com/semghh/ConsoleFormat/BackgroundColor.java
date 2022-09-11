package com.semghh.ConsoleFormat;

/**
 * @author SemgHH
 * 背景颜色枚举类
 */

public enum BackgroundColor {
    //黑色
    BLACK(40),
    //红色
    RED(41),
    //绿色
    GREEN(42),
    //黄色
    YELLOW(43),
    //蓝色
    BLUE(44),
    //紫色
    PURPLE(45),
    //深绿
    DEEP_GREEN(46),
    //白色
    WHITE(47);;


    final int backgroundColorCode;

    BackgroundColor(int backgroundColorCode) {
        this.backgroundColorCode = backgroundColorCode;
    }


}
