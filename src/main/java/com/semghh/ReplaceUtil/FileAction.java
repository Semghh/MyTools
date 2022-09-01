package com.semghh.ReplaceUtil;

import java.io.File;

/**
 * 一个文件操作接口,用于对文件进行任意的操作
 */
public interface FileAction {

    boolean apply(File file);
    void action(File file);

}
