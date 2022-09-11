package com.semghh.ReplaceUtil;

import java.io.File;
import java.util.Map;

/**
 *
 * 一个 name 到 replace Text 的映射,被用于填充 对应name的 slot
 * 开发者可以实现 ReplaceFileNameMappingResolver接口,来直接提供一个 name <=> replace text 的映射
 * 这个接口将被如下方法调用
 * @see ReplaceUtils#replaceByField(String, String, File, File, SlotNameResolver, ReplaceFileNameMappingResolver)
 * @author SemgHH
 */
public interface ReplaceFileNameMappingResolver {
    /**
     * 返回name(唯一的name)到替换文本(replace Text) 的唯一映射
     * 始终保持 slot的name 和 替换文本的 name一致
     * @return 对应的映射关系. K 为name  , V 为 替换文本
     */
    Map<String, String> getNameMappingFromReplaceText();
}
