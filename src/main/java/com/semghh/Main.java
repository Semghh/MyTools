package com.semghh;


import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {

    public static void main(String[] args) throws IOException {
        File srcFile = new File("C:\\Users\\SemgHH\\Desktop\\a.txt");
        File replaceFile = new File("C:\\Users\\SemgHH\\Desktop\\b.txt");
        File resFile = new File("C:\\Users\\SemgHH\\Desktop\\res.txt");
        String srcPattern = "�\\?";

//        ReplaceUtils.replaceByLine(srcPattern,srcFile,replaceFile,resFile);



        //替换table
//        String groupPattern = "<td class=\"字段\">�\\?</td>\r*\n*\t*<td>\r*\n*\t*<input name=\".*\" type=\"text\" class=\"w180\"/>";
//        ReplaceUtils.replaceByField(groupPattern,srcPattern,srcFile,replaceFile,resFile);




        String groupPattern1 = "<th data-options=\"field:'.*'.*>%</th>";
        //替换表头
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


}
