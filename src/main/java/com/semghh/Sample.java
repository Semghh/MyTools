package com.semghh;


import com.semghh.ConsoleFormat.BackgroundColor;
import com.semghh.ConsoleFormat.ConsoleFormatUtils;
import com.semghh.ConsoleFormat.FontColor;
import com.semghh.ConsoleFormat.FontStyle;
import com.semghh.ReplaceUtil.SlotNameResolver;
import com.semghh.ReplaceUtil.ReplaceUtils;
import com.semghh.ReplaceUtil.SimpleReplaceFileAction;
import org.junit.Test;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Sample {

    static File srcFile = new File("C:\\Users\\SemgHH\\Desktop\\a.txt");
    static File replaceFile = new File("C:\\Users\\SemgHH\\Desktop\\b.txt");
    static File resFile = new File("C:\\Users\\SemgHH\\Desktop\\res.txt");

    static String srcPattern = "�\\?";

    public static void main(String[] args) throws IOException {

//        ReplaceUtils.replaceByLine(srcPattern,srcFile,replaceFile,resFile);

        String s = "hello,world";

        FontColor[] fontColors = FontColor.values();
        for (FontColor value : fontColors) {
            System.out.println(ConsoleFormatUtils.fontColorFormat(value, s));
        }

        FontStyle[] fontStyles = FontStyle.values();
        for (FontStyle fontStyle : fontStyles) {
            System.out.println(ConsoleFormatUtils.fontStyleFormat(fontStyle,s));
        }

        BackgroundColor[] backgroundColors = BackgroundColor.values();

        for (BackgroundColor backgroundColor : backgroundColors) {

            System.out.println(ConsoleFormatUtils.backgroundColorFormat(backgroundColor,s));

        }



    }


    /***
     * 替换 <table>标签
     */
    @Test
    public void ReplaceTable() {

        String groupPattern = "<td class=\"字段\">�\\?</td>\r*\n*\t*<td>\r*\n*\t*<input name=\".*\" type=\"text\" class=\"w180\"/>";
        ReplaceUtils.replaceByField(groupPattern, srcPattern, srcFile, replaceFile, resFile);
    }

    /**
     * 替换 表头
     */
    @Test
    public void replaceThead() {
        String groupPattern1 = "<th data-options=\"field:'.*'.*>%</th>";
        SlotNameResolver slotNameResolver = new SlotNameResolver() {
            @Override
            public String getNameFromGroup(String group) {
                //<th data-options="field:'netuserId',width:150,tip:true"></th>
                Pattern pattern = Pattern.compile("field:'.*'");
                Matcher matcher = pattern.matcher(group);
                if (matcher.find()) {
                    return matcher.group().split("'")[1];
                }
                return null;
            }
        };
        ReplaceUtils.replaceByField(groupPattern1, "%", srcFile, replaceFile, resFile, slotNameResolver);
    }

    /**
     * 替换 类名
     */
    @Test
    public void replaceClassName() {
        //按照指定正则,替换文件夹下所有文件,
        File srcDir = new File("C:\\Users\\SemgHH\\Desktop\\a");
        ReplaceUtils.recursionWithAction(srcDir, new SimpleReplaceFileAction("Resview.tdMBusOppoPower", "TdMBusOppoPower"));
    }

    @Test
    public void testForCamelCase() {
        String s = "abc_dev_";
        System.out.println(ReplaceUtils.kebabCaceToUpperCamelCase(s));
    }

}
