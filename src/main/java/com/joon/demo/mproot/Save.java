package com.joon.demo.mproot;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

/**
 * 保存接口
 *
 * @author Joon
 * @date 2023-03-17 10:03
 */
public interface Save {
    /**
     * 校验必填参数的方法实现
     *
     * @author Joon
     * @date 2022-09-07 10:03
     */
    default void checkRequire() {

    }

    /**
     * 填充唯一标识
     *
     * @param uniqueCode 唯一标识
     */
    default void fillUniqueCode(String uniqueCode) {

    }

    /**
     * 生成唯一标识
     */
    default void generatorUniqueCode() {
        this.fillUniqueCode(IdWorker.getIdStr());
    }
}
