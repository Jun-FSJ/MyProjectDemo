package com.example.myprojectdemo.mproot.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 处理新增和更新的基础数据填充，配合BaseEntity和MyBatisPlusConfig使用
 *
 * @author Joon
 * @date 2023-03-17 15:34
 */
@Slf4j
@Component
public class MetaHandler implements MetaObjectHandler {


    /**
     * 新增数据执行
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            Date now = new Date();
            if (metaObject.hasSetter("createTime")) {
                log.debug("自动插入 createTime");
                this.setFieldValByName("createTime", now, metaObject);
            }
            if (metaObject.hasSetter("updateTime")) {
                log.debug("自动插入 updateTime");
                this.setFieldValByName("updateTime", now, metaObject);
            }
            if (metaObject.hasSetter("isDel")) {
                log.debug("自动插入 isDel");
                this.setFieldValByName("isDel", 0, metaObject);
            }

        } catch (Exception e) {
            log.error("自动注入失败:{}", e);
        }
    }

    /**
     * 更新数据执行
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            Date now = new Date();
            if (metaObject.hasSetter("updateTime")) {
                log.debug("自动插入 updateTime");
                this.setFieldValByName("updateTime", now, metaObject);
            }
            if (metaObject.hasSetter("isDel")) {
                log.debug("自动插入 isDel");
                this.setFieldValByName("isDel", null, metaObject);
            }
            if (metaObject.hasSetter("createTime")) {
                log.debug("自动插入 createTime");
                this.setFieldValByName("createTime", null, metaObject);
            }
        } catch (Exception e) {
            log.error("自动注入失败:{}", e);
        }
    }

}
