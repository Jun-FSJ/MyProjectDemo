package com.example.myprojectdemo.mproot.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询分组
 * 使用demo：
 * LambdaQueryWrapper.select((info) -> {
 * return info.getField().getAnnotation(Group.class)
 * })
 *
 * @author Joon
 * @date 2023-03-17
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Group {
    /**
     * @return 分组名称
     */
    String[] value();
}

