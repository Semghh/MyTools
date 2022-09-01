package com.semghh.ReplaceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 简易的文本替换器
 */

public class SimpleReplaceFileAction implements FileAction {

    private final String srcPattern;

    private final String replaceText;

    public SimpleReplaceFileAction(String srcPattern, String replaceText) {
        this.srcPattern = srcPattern;
        this.replaceText = replaceText;
    }

    @Override
    public boolean apply(File file) {
        return file.isFile();
    }

    @Override
    public void action(File file) {
        try(FileInputStream fis = new FileInputStream(file);){
            String s = new String(fis.readAllBytes(),StandardCharsets.UTF_8).replaceAll(srcPattern,replaceText);
            try (FileOutputStream fos = new FileOutputStream(file)){
                fos.write(s.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
