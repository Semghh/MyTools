package com.semghh.ReplaceUtil;

import java.io.File;

/**
 * 一个文件操作接口,用于对文件进行任意的操作
 * @author SemgHH
 */
public interface FileAction {

    /**
     * 判断当前文件是否可以被处理
     * @param file 一个文件
     * @return true表示可以处理当前文件。 false表示无法处理，跳过
     */
    boolean apply(File file);

    /**
     * 处理当前文件的方法。
     * 对当前文件的处理逻辑,请实现本方法。
     *
     * 对于一个文件，只有 FileAction.apply(File) 方法返回ture以后,才会调用本方法
     * @param file 当前文件
     */
    void action(File file);

}
