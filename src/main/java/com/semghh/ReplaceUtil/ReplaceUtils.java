package com.semghh.ReplaceUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author SemgHH
 */
public class ReplaceUtils {


    /**
     * 按行替换 srcFile 文件中的指定的 srcPattern .
     * <p>
     * 按顺序从 srcFile文件中读取包含 srcPattern的一行, 再从 aimFile读取下一个非空的一整行,替换。  可以替换这一行全部的 srcPattern
     * <p>
     * 这意味着你的 aimFile 文件中，必须是干干净净的替换文本 ,而srcFile可以是不干净的
     * <p>
     * <p>
     * <p>
     * <p>
     * 编码 UTF-8
     *
     * @param srcPattern         源文件中需要替换的pattern ,必须是正则
     * @param sourceFile         源文件
     * @param replacePatternFile 替换文本。 每一个替换pattern必须是干净的另起一段。
     * @param resFile            输出最终的结果文件. 输出的文件结构和 源文件保持一致
     * @IOException e 抛出任何异常, 未抛出任何异常则执行成功。
     * <p>
     * <p>
     * Notes:
     * 支持 源文件数量 和 替换文本数量 不匹配的情况。
     * 保证源文件 文件结构不变的基础上，尽可能的替换 替换文本。
     */
    public static void replaceByLine(String srcPattern, File sourceFile, File replacePatternFile, File resFile) {
        try (FileInputStream fisa = new FileInputStream(sourceFile);
             FileInputStream fisb = new FileInputStream(replacePatternFile);
             FileOutputStream fops = new FileOutputStream(resFile)) {

            Scanner a = new Scanner(fisa);
            Scanner b = new Scanner(fisb);
            while (a.hasNextLine()) {
                String lineA = a.nextLine();
                if (lineA.matches(".*" + srcPattern + ".*")) {
                    String lineb = null;
                    while (b.hasNextLine()) {
                        lineb = b.nextLine();
                        if (lineb == null || lineb.length() == 0) {
                            continue;
                        } //过滤空的替换文本
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
     * 这可能是一个泛用性比较低的方法
     * <p>
     * 按照给定的正则判定为一组。 每一组有一个唯一的name标识, 根据name 匹配 替换文本中的name. name必须被 "" 包裹
     * 以sourceFile为结构不变。匹配的name为空,则不会修改
     * <p>
     * replacePatternFile 必须满足结构:  "<name>" IS '<replacePattern>'  从双引号中取出 name 从单引号中取出对应的文本
     *
     * @param srcPattern         需要替换的正则
     * @param sourceFile         源结构文件
     * @param replacePatternFile 替换的源文件
     * @param resFile            输出结果
     */
    public static void replaceByField(String groupPattern, String srcPattern, File sourceFile, File replacePatternFile, File resFile) {

        try (FileInputStream fisa = new FileInputStream(sourceFile);
             FileInputStream fisb = new FileInputStream(replacePatternFile);
             FileOutputStream fops = new FileOutputStream(resFile)) {

            HashMap<String, String> nameToReplacePattern = new HashMap<>();

            Pattern pattern = Pattern.compile("\".*\" IS '.*'");
            String strB = new String(fisb.readAllBytes(), StandardCharsets.UTF_8);
            Matcher matcher = pattern.matcher(strB);
            while (matcher.find()) {
                String group = matcher.group();
                String[] split = group.split("\"");
                String[] split1 = group.split("'");
                nameToReplacePattern.put(kebabCaseToLowerCamelCase(split[1]), split1[1]);
            }
            String strA = new String(fisa.readAllBytes());

            pattern = Pattern.compile(groupPattern);
            matcher = pattern.matcher(strA);
            while (matcher.find()) {
                String originGroup = matcher.group();
                Pattern namePatter = Pattern.compile("name=\".*\"");
                Matcher nameMatcher = namePatter.matcher(originGroup);
                if (nameMatcher.find()) {
                    String[] split = nameMatcher.group().split("\"");
                    //如果存在对应的name
                    if (nameToReplacePattern.containsKey(split[1])) {
                        String s = originGroup.replaceAll(srcPattern, nameToReplacePattern.get(split[1]));
                        strA = strA.replace(originGroup, s);
                    }
                }
            }

            fops.write(strA.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 泛用性更高一点儿的方法
     * <p>
     * <p>
     * 通过groupPattern 匹配出一个group ,NameResolver提供一个getName方法,会传入原始Group的字符串。 需要开发者提供这个group对应的name
     *
     * @param groupPattern       匹配一个Group的正则
     * @param srcPattern         需要替换的正则
     * @param sourceFile         源结构文件
     * @param replacePatternFile 替换的源文件
     * @param resFile            输出结果
     * @param resolver           name解决器,从原始group中提取出对应name名
     */
    public static void replaceByField(String groupPattern, String srcPattern, File sourceFile, File replacePatternFile,
                                      File resFile, SlotNameResolver resolver) {

        try (FileInputStream fisa = new FileInputStream(sourceFile);
             FileInputStream fisb = new FileInputStream(replacePatternFile);
             FileOutputStream fops = new FileOutputStream(resFile)) {
            HashMap<String, String> nameToReplacePattern = new HashMap<>();
            Pattern pattern = Pattern.compile("\".*\" IS '.*'");
            String strB = new String(fisb.readAllBytes());
            Matcher matcher = pattern.matcher(strB);
            while (matcher.find()) {
                String group = matcher.group();
                String[] split = group.split("\"");
                String[] split1 = group.split("'");
                nameToReplacePattern.put(kebabCaseToLowerCamelCase(split[1]), split1[1]);
            }
            String strA = new String(fisa.readAllBytes());

            pattern = Pattern.compile(groupPattern);
            matcher = pattern.matcher(strA);
            doReplace(srcPattern, resolver, fops, nameToReplacePattern, strA, matcher);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 泛用性更高一点儿的方法
     * <p>
     * <p>
     * 通过groupPattern 匹配出一个group ,NameResolver提供一个getName方法,会传入原始Group的字符串。 需要开发者提供这个group对应的name
     *
     * @param groupPattern        匹配一个Group的正则
     * @param srcPattern          需要替换的正则
     * @param sourceFile          源结构文件
     * @param resFile             输出结果
     * @param resolver            name解决器,从原始group中提取出对应name名
     * @param replaceFileNameMappingResolver 替换文本解决器,返回一个Map<String,String> key为name value为对应的替换文本
     */
    public static void replaceByField(String groupPattern,
                                      String srcPattern,
                                      File sourceFile,
                                      File resFile,
                                      SlotNameResolver resolver,
                                      ReplaceFileNameMappingResolver replaceFileNameMappingResolver) {

        try (FileInputStream fisa = new FileInputStream(sourceFile);
             FileOutputStream fops = new FileOutputStream(resFile)) {
            Map<String, String> nameFromReplacePattern = replaceFileNameMappingResolver.getNameMappingFromReplaceText();
            String strA = new String(fisa.readAllBytes());
            Pattern pattern = Pattern.compile(groupPattern);
            Matcher matcher = pattern.matcher(strA);
            doReplace(srcPattern, resolver, fops, nameFromReplacePattern, strA, matcher);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void doReplace(String srcPattern, SlotNameResolver resolver,
                                  FileOutputStream fops, Map<String, String> nameToReplacePattern,
                                  String strA, Matcher matcher) throws IOException {
        while (matcher.find()) {
            String originGroup = matcher.group();
            String nameFromGroup = resolver.getNameFromGroup(originGroup);
            //如果存在对应的name
            if (nameFromGroup != null && nameToReplacePattern.containsKey(nameFromGroup)) {
                String s = originGroup.replaceAll(srcPattern, nameToReplacePattern.get(nameFromGroup));
                strA = strA.replace(originGroup, s);
            }
        }
        fops.write(strA.getBytes(StandardCharsets.UTF_8));
    }


    /**
     * 指定Dir下全部文件按照指定的 pattern全部替换为 replaceText
     *
     * @param dirFile     指定的Dir
     * @param pattern     指定的pattern
     * @param replaceText 指定的替换字段
     */
    public static void replaceDirAllPattern(File dirFile, String pattern, String replaceText) {

        if (!dirFile.isDirectory()) {
            return;
        }
        File[] files = dirFile.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            recursionWithReplaceAction(file, pattern, replaceText);
        }
    }

    private static void recursionWithReplaceAction(File file, String pattern, String replaceText) {
        if (file == null) {
            return;
        }
        if (file.isFile()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                String s = new String(fis.readAllBytes()).replaceAll(pattern, replaceText);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(s.getBytes(StandardCharsets.UTF_8));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    recursionWithReplaceAction(child, pattern, replaceText);
                }
            }
        }
    }


    public static void recursionWithAction(File file, FileAction action) {
        if (file == null) {
            return;
        }
        if (file.isFile() && action.apply(file)) {
            action.action(file);
        } else if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    recursionWithAction(child, action);
                }
            }
        }
    }


    /**
     * 下划线 kebabCase 转化为 小驼峰式 LowerCamelCase
     */
    public static String kebabCaseToLowerCamelCase(String kebabCase) {
        String[] split = kebabCase.split("_");

        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for (String s : split) {
            if (first) {
                sb.append(s);
                first = false;
                continue;
            }
            sb.append((s.charAt(0) + "").toUpperCase());
            sb.append(s.substring(1));
        }
        if (kebabCase.charAt(kebabCase.length()-1)=='_'){
            sb.append("_");
        }
        return sb.toString();
    }

    /**
     * 下划线 kebabCase 转化为大驼峰 UpperCamelCase
     */
    public static String kebabCaceToUpperCamelCase(String kebabCase){
        String s = kebabCaseToLowerCamelCase(kebabCase);
        return s.substring(0,1).toUpperCase()+s.substring(1);
    }

    /**
     * 小驼峰式 CamelCase 转化为 kebabCase
     */
    public static String camelCaseToKebabCase(String camelCase) {
        char[] chars = camelCase.toCharArray();
        StringBuilder sb = new StringBuilder();
        if (isUpperCase(chars[0])) {
            sb.append((char) (chars[0] + 32));
        } else {
            sb.append(chars[0]);
        }
        for (int i = 1; i < chars.length; i++) {
            if (!isUpperCase(chars[i])) {
                sb.append(chars[i]);
            } else {
                sb.append("_");
                sb.append((char) (chars[i] + 32));
            }
        }
        return sb.toString();
    }

    public static boolean isUpperCase(char c) {
        return 'A' <= c && c <= 'Z';
    }






}
