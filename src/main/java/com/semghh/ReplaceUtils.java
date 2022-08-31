package com.semghh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplaceUtils {


    /**
     * 按行替换 srcFile 文件中的指定的 srcPattern .
     *
     * 按顺序从 srcFile文件中读取包含 srcPattern的一行, 再从 aimFile读取下一个非空的一整行,替换。  可以替换这一行全部的 srcPattern
     *
     * 这意味着你的 aimFile 文件中，必须是干干净净的替换文本 ,而srcFile可以是不干净的
     *
     *
     *
     *
     * 编码 UTF-8
     *
     * @param srcPattern  源文件中需要替换的pattern ,必须是正则
     * @param sourceFile   源文件
     * @param replacePatternFile  替换文本。 每一个替换pattern必须是干净的另起一段。
     * @param resFile 输出最终的结果文件. 输出的文件结构和 源文件保持一致
     * @IOException e 抛出任何异常, 未抛出任何异常则执行成功。
     *
     *
     * Notes:
     *      支持 源文件数量 和 替换文本数量 不匹配的情况。
     *      保证源文件 文件结构不变的基础上，尽可能的替换 替换文本。
     *
     *
     *
     */
    public static void replaceByLine(String srcPattern, File sourceFile, File replacePatternFile , File resFile) {
        try (FileInputStream fisa = new FileInputStream(sourceFile);
             FileInputStream fisb = new FileInputStream(replacePatternFile);
             FileOutputStream fops = new FileOutputStream(resFile)) {

            Scanner a = new Scanner(fisa);
            Scanner b = new Scanner(fisb);
            while (a.hasNextLine()) {
                String lineA = a.nextLine();
                if (lineA.matches(".*"+srcPattern+".*")) {
                    String lineb = null;
                    while (b.hasNextLine()) {
                        lineb = b.nextLine();
                        if (lineb == null || lineb.length() == 0) {continue;} //过滤空的替换文本
                        break;
                    }
                    if (lineb != null) {
                        lineA = lineA.replaceAll(srcPattern, lineb);
                    }
                }
                fops.write(lineA.getBytes(StandardCharsets.UTF_8));
                fops.write("\n".getBytes(StandardCharsets.UTF_8));
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 这可能是一个泛用性更低的方法。
     *
     * 按照给定的正则判定为一组。 每一组有一个唯一的name标识, 根据name 匹配 替换文本中的name.
     * 以sourceFile为结构不变。匹配的name为空,则不会修改
     *
     * replacePatternFile 必须满足结构:  "<name>" IS '<replacePattern>'  从双引号中取出 name 从单引号中取出对应的文本
     *
     * @param srcPattern 需要替换的正则
     * @param sourceFile 源结构文件
     * @param replacePatternFile 替换的源文件
     * @param resFile 输出结果
     */
    public static void replaceByField(String groupPattern,String srcPattern, File sourceFile,File replacePatternFile,File resFile){

        try (FileInputStream fisa = new FileInputStream(sourceFile);
             FileInputStream fisb = new FileInputStream(replacePatternFile);
             FileOutputStream fops = new FileOutputStream(resFile)) {

            HashMap<String,String> nameToReplacePattern = new HashMap<>();

            Pattern pattern = Pattern.compile("\".*\" IS '.*'");
            String strB = new String(fisb.readAllBytes());
            Matcher matcher = pattern.matcher(strB);
            while (matcher.find()){
                String group = matcher.group();
                String[] split = group.split("\"");
                String[] split1 = group.split("'");
                nameToReplacePattern.put(kebabCaseToCamelCase(split[1]),split1[1]);
            }
            String strA = new String(fisa.readAllBytes());

            pattern = Pattern.compile(groupPattern);
            matcher = pattern.matcher(strA);
            while (matcher.find()){
                String originGroup = matcher.group();
                Pattern namePatter = Pattern.compile("name=\".*\"");
                Matcher nameMatcher = namePatter.matcher(originGroup);
                if (nameMatcher.find()) {
                    String[] split = nameMatcher.group().split("\"");
                    //如果存在对应的name
                    if (nameToReplacePattern.containsKey(split[1])){
                        String s = originGroup.replaceAll(srcPattern, nameToReplacePattern.get(split[1]));
                        strA = strA.replace(originGroup,s);
                    }
                }
            }

            fops.write(strA.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 下划线 kebabCase 转化为 小驼峰式 CamelCase
     */
    public static String kebabCaseToCamelCase(String kebabCase){
        String[] split = kebabCase.split("_");

        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for (String s : split) {
            if (first){
                sb.append(s);
                first = false;
                continue;
            }
            sb.append((s.charAt(0)+"").toUpperCase());
            sb.append(s.substring(1));
        }
        return sb.toString();
    }

    /**
     * 小驼峰式 CamelCase 转化为 kebabCase
     */
    public static String CamelCaseToKebabCase(String camelCase){
        char[] chars = camelCase.toCharArray();
        StringBuilder sb = new StringBuilder();
        if (isUpperCase(chars[0])){
            sb.append((char)(chars[0]+32));
        }else {
            sb.append(chars[0]);
        }
        for (int i = 1; i < chars.length; i++) {
            if (!isUpperCase(chars[i])){
                sb.append(chars[i]);
            }else {
                sb.append("_");
                sb.append((char)(chars[i]+32));
            }
        }
        return sb.toString();
    }

    public static boolean isUpperCase(char c){
        return 'A'<=c && c<='Z';
    }

}
