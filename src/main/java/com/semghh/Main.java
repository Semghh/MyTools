package com.semghh;


import com.semghh.ReplaceUtil.ReplaceUtils;
import com.semghh.ReplaceUtil.SimpleReplaceFileAction;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {

    static File srcFile = new File("C:\\Users\\SemgHH\\Desktop\\a.txt");
    static File replaceFile = new File("C:\\Users\\SemgHH\\Desktop\\b.txt");
    static File resFile = new File("C:\\Users\\SemgHH\\Desktop\\res.txt");

    static String srcPattern = "�\\?";

    public static void main(String[] args) throws IOException {

//        ReplaceUtils.replaceByLine(srcPattern,srcFile,replaceFile,resFile);


    }


    /***
     * 替换 <table>标签
     */
    @Test
    public void ReplaceTable(){

        String groupPattern = "<td class=\"字段\">�\\?</td>\r*\n*\t*<td>\r*\n*\t*<input name=\".*\" type=\"text\" class=\"w180\"/>";
        ReplaceUtils.replaceByField(groupPattern,srcPattern,srcFile,replaceFile,resFile);
    }

    /**
     * 替换 表头
     */
    @Test
    public void replaceThead(){
        String groupPattern1 = "<th data-options=\"field:'.*'.*>%</th>";
        ReplaceUtils.NameResolver nameResolver = new ReplaceUtils.NameResolver() {
            @Override
            public String getNameFromGroup(String group) {
                //<th data-options="field:'netuserId',width:150,tip:true"></th>

                Pattern pattern = Pattern.compile("field:'.*'");
                Matcher matcher = pattern.matcher(group);
                if (matcher.find()){
                    return matcher.group().split("'")[1];
                }
                return null;
            }
        };
        ReplaceUtils.replaceByField(groupPattern1,"%",srcFile,replaceFile,resFile,nameResolver);
    }

    /**
     * 替换 类名
     */
    @Test
    public void replaceClassName(){
        //按照指定正则,替换文件夹下所有文件,
        File srcDir = new File("C:\\Users\\SemgHH\\Desktop\\a");
        ReplaceUtils.recursionWithAction(srcDir,new SimpleReplaceFileAction("Resview.tfPlm2b2cgrouporder","TfPlm2b2cgrouporder"));
    }
}
