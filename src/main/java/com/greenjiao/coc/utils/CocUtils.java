package com.greenjiao.coc.utils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.apache.commons.lang3.StringUtils.trim;

/**
 * @Author Yan
 * @Date 2023/06/09 12:08
 * @Version 1.0
 */
public class CocUtils {
    /**
     * 检查一个实体类对象是否所有字段都为空
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static boolean allFieldsAreNull(Object obj) throws IllegalAccessException {
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.get(obj) != null) {
                return false;
            }
        }
        return true;
    }

    public static LocalDateTime getNowLocalDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String createTime = LocalDateTime.now().format(formatter);
        // 需要修改解析器，支持空格和分隔符,否则parse解析字符串时有空格会报错
        DateTimeFormatter parser = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return LocalDateTime.parse(createTime.replace(" ", "T"), parser);
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs) {
        if (str != null && strs != null) {
            for (String s : strs) {
                if (str.equalsIgnoreCase(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }
}
