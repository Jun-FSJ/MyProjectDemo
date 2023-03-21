package com.example.myprojectdemo.mproot;

/**
 * 保存,修改接口
 *
 * @author Joon
 * @date 2023-03-17 10:03
 */
public interface SaveOrUpdate extends Save, Update {

    /**
     * 校验必填参数的方法实现
     *
     * @author Joon
     * @date 2022-09-07 10:03
     */
    @Override
    default void checkRequire4Update() {
        Save.super.checkRequire();
    }
}
