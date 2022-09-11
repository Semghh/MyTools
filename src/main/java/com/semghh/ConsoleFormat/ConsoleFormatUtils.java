package com.semghh.ConsoleFormat;

/**
 * @author SemgHH
 */
public class ConsoleFormatUtils {
    /**
     * 字体颜色格式化
     * @param colorType 传入一个颜色
     * @param content 字符串内容
     * @return 格式化好的字符串
     */
    public static String fontColorFormat(FontColor colorType, String content) {
        return String.format("\033[%dm%s\033[0m", colorType.colorCode, content);
    }

    /**
     * 背景颜色格式化
     * @param backgroundColor 传入一个背景颜色
     * @param content 字符串内容
     * @return 格式化好的字符串
     */
    public static String backgroundColorFormat(BackgroundColor backgroundColor,String content){
        return String.format("\033[%dm%s\033[0m",backgroundColor.backgroundColorCode,content);
    }

    /**
     * 字体样式格式化
     * @param fontStyle 传入一个字体样式
     * @param content 字符串内容
     * @return 格式化好的字符串
     */
    public static String fontStyleFormat(FontStyle fontStyle,String content){
        return String.format("\033[%dm%s\033[0m",fontStyle.styleCode,content);
    }

    public static String format(int formatCode,String content){
        return String.format("\033[%dm%s\033[0m",formatCode,content);
    }

    /**
     * 格式化字体
     * @param color  颜色
     * @param style  样式
     * @param background 背景颜色
     * @param content 字符串内容
     * @return 格式化好的字符串
     */
    public static String format(FontColor color,FontStyle style,BackgroundColor background,String content){
        return String.format("\033[%d;%d;%dm%s\033[0m",
                color.colorCode, style.styleCode,background.backgroundColorCode,content);
    }



}
