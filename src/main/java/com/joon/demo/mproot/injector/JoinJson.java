package com.joon.demo.mproot.injector;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

/**
 * 查询json方法
 *
 * @author Joon
 * @date 2023-03-17 15:05
 */
class JoinJson extends AbsJoin {
    public JoinJson(String methodName) {
        super(methodName);
    }

    /**
     * 返回结果类型
     *
     * @param tableInfo 表信息
     * @return 查询返回结果的类型
     */
    @Override
    protected Class<?> resultType(TableInfo tableInfo) {
        return JSONObject.class;
    }
}
