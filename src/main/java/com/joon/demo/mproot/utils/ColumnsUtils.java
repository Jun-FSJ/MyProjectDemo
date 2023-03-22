package com.joon.demo.mproot.utils;

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 列名工具类
 *
 * @author Joon
 * @date 2023-03-17 19:57
 */
public class ColumnsUtils {

    /**
     * 转列名
     *
     * @param function 方法lambda表达式如(aaa::ccc)
     * @param <T>      对象
     * @param <R>      字段类型
     * @return 返回对象列名
     */
    public static <T, R> String columns(SFunction<T, R> function) {
        return columns(function, true);
    }

    /**
     * 转列名
     *
     * @param function       方法lambda表达式如(aaa::ccc)
     * @param hump2Underline 是否驼峰转下划线
     * @param <T>            对象
     * @param <R>            字段类型
     * @return 返回对象列名
     */
    public static <T, R> String columns(SFunction<T, R> function, boolean hump2Underline) {
        String propertyName = PropertyNamer.methodToProperty(LambdaUtils.extract(function).getImplMethodName());
        return hump2Underline ? hump2Underline(propertyName) : propertyName;
    }

    /**
     * 转列名
     *
     * @param function 方法lambda表达式如(aaa::ccc)
     * @param <T>      对象
     * @param <R>      字段类型
     * @return 返回对象列名
     */
    @SafeVarargs
    public static <T, R> String columns(SFunction<T, R>... function) {
        return columns(",", function);
    }

    /**
     * 转列名
     *
     * @param delimiter 拼接符
     * @param function  方法lambda表达式如(aaa::ccc)
     * @param <T>       对象
     * @param <R>       字段类型
     * @return 返回对象列名
     */
    @SafeVarargs
    public static <T, R> String columns(String delimiter, SFunction<T, R>... function) {
        return columns(delimiter, true, function);
    }

    /**
     * 转列名
     *
     * @param function       方法lambda表达式如(aaa::ccc)
     * @param hump2Underline 是否驼峰转下划线
     * @param <T>            对象
     * @param <R>            字段类型
     * @return 返回对象列名
     */
    @SafeVarargs
    public static <T, R> String columns(String delimiter, boolean hump2Underline, SFunction<T, R>... function) {
        Stream<String> stream = Arrays.stream(function).map(f -> PropertyNamer.methodToProperty(LambdaUtils.extract(f).getImplMethodName()));
        if (hump2Underline) {
            stream = stream.map(ColumnsUtils::hump2Underline);
        }
        return stream.collect(Collectors.joining(delimiter));
    }

    /**
     * 驼峰转 下划线
     * userName  ---->  user_name
     * user_name  ---->  user_name
     *
     * @param camelCaseStr 驼峰字符串
     * @return 带下滑线的String
     */
    public static String hump2Underline(String camelCaseStr) {
        if (StringUtils.isBlank(camelCaseStr)) {
            return StringUtils.EMPTY;
        }
        // 将驼峰字符串转换成数组
        char[] charArray = camelCaseStr.toCharArray();
        StringBuilder buffer = new StringBuilder();
        // 处理字符串
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] >= 65 && charArray[i] <= 90) {
                buffer.append('_').append(charArray[i] += 32);
            } else {
                buffer.append(charArray[i]);
            }
        }
        return buffer.toString();
    }
}
