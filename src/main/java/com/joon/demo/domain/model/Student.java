package com.joon.demo.domain.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.joon.demo.mproot.query.annotation.Query;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生对象
 *
 * @author : Joon
 * @date : 2023/3/14 16:44
 * @modyified By :
 */
@Data
@TableName(value = "student")
public class Student implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1813448251634000424L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 姓名 */
    @Query(keyword = SqlKeyword.LIKE)
    private String name;
    /** 年龄 */
    private Integer age;
    /** 性别 */
    private Integer sex;
    /** 爱好 */
    private String hobby;
    /** 学校Id */
    private Integer schoolId;
}
