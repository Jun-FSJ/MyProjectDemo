package com.example.myprojectdemo.mproot.query.annotation;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 查询注解
 *
 * @author Joon
 * @date 2023-03-17 20:26
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Query {
    /**
     * 是否忽略该字段
     *
     * @return boolean
     */
    boolean ignore() default false;

    /**
     * 查询关键字(默认为 =)
     *
     * @return SqlKeyword
     */
    SqlKeyword keyword() default SqlKeyword.EQ;

    /**
     * 需要映射的字段，如果是驼峰会被映射成下划线
     *
     * @return String
     */
    String[] mapping() default {};

    /**
     * 是否需要将字段映射成下划线
     *
     * @return boolean
     */
    boolean camelToUnderline() default true;

    /**
     * 条件的下标，根据下标执行拼接排序,升序排列，越大越往后
     *
     * @return 下标
     */
    int index() default 0;
}
