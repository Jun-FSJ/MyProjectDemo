package com.example.myprojectdemo.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : Joon
 * @date : 2023/3/17 18:00
 * @modyified By :
 */
@Data
public class StudentQueryVO implements Serializable {
    private static final long serialVersionUID = -3483698442915278665L;
    /** 姓名 */
    private String name;
    /** 年龄 */
    private Integer age;
    /** 性别 */
    private Integer sex;
    /** 爱好 */
    private String hobby;
    /** 学校名称 */
    private String schoolName;
}
