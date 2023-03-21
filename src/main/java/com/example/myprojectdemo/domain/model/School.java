package com.example.myprojectdemo.domain.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 学校 对象
 * @author : Joon
 * @date : 2023/3/17 17:55
 * @modyified By :
 */
@Data
@TableName("school")
public class School implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = -6906482005195338799L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 学校名称 */
    private String schoolName;
}
