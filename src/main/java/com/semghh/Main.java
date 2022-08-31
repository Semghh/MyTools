package com.semghh;


import java.io.*;


public class Main {

    public static void main(String[] args) throws IOException {
        File srcFile = new File("C:\\Users\\SemgHH\\Desktop\\a.txt");
        File replaceFile = new File("C:\\Users\\SemgHH\\Desktop\\b.txt");
        File resFile = new File("C:\\Users\\SemgHH\\Desktop\\res.txt");
        String srcPattern = "�\\?";

//        ReplaceUtils.replaceByLine(srcPattern,srcFile,replaceFile,resFile);



        String groupPattern = "<td class=\"字段\">�\\?</td>\r*\n*\t*<td>\r*\n*\t*<input name=\".*\" type=\"text\" class=\"w180\"/>";
        ReplaceUtils.replaceByField(groupPattern,srcPattern,srcFile,replaceFile,resFile);



    }


}
