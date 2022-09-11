package com.semghh.ReplaceUtil;

import java.io.File;

/**
 * 一个用于返回Group里的唯一name的 解析器。
 *
 * 保持原有结构的文本称为： structure File 结构文本
 * 在结构文本中,存在多个匹配的 slot 每一个slot都有一个唯一的name与之对应。
 * 提供替换数据源的文本称为 : replace File
 * 需要从替换文本中解析出一段字符串,并命名一个唯一的name, 和structure File中的唯一对应。
 *
 *
 * 这个接口用于解析出 name 对应的唯一字符串。在replaceByField()方法中将被匹配slot。 详细参看下面的方法
 * @see ReplaceUtils#replaceByField(String, String, File, File, NameResolver, ReplaceUtils.ReplaceFileResolver)
 * @author SemgHH
 */
public interface NameResolver {
    /**
     * 从group中解析出一个唯一的name,用于后续的注入.
     * @param group 一个Group的原始字符串
     * @return 返回这个Group对应的唯一name
     */
    String getNameFromGroup(String group);
}
