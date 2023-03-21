package com.example.myprojectdemo.mproot.common.mapper;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 封装核心 MyBatisPlus Mapper
 *
 * @author Joon
 * @date 2023-03-17 15:32
 */
public interface CoreMapper<T> extends BaseMapper<T> {
    /**
     * 查询对象结合
     *
     * @param wrapper 查询wrapper
     * @return com.alibaba.fastjson2.JSONArray
     */
    JSONArray listJson(@Param(Constants.WRAPPER) Wrapper<T> wrapper);

    /**
     * 内连接查询集合
     *
     * @param wrapper 查询wrapper
     * @return com.alibaba.fastjson2.JSONArray
     */
    T join(@Param(Constants.WRAPPER) Wrapper<T> wrapper);

    /**
     * 内连接查询集合
     *
     * @param wrapper 查询wrapper
     * @return com.alibaba.fastjson2.JSONArray
     */
    List<T> joinList(@Param(Constants.WRAPPER) Wrapper<T> wrapper);

    /**
     * 内连接分页查询
     *
     * @param page    分页参数
     * @param wrapper 查询wrapper
     * @return com.alibaba.fastjson2.JSONArray
     */
    <P extends IPage<T>> P joinPage(P page, @Param(Constants.WRAPPER) Wrapper<T> wrapper);

    /**
     * 内连接查询集合
     *
     * @param wrapper 查询wrapper
     * @return com.alibaba.fastjson2.JSONArray
     */
    JSONObject joinJson(@Param(Constants.WRAPPER) Wrapper<T> wrapper);

    /**
     * 内连接查询集合
     *
     * @param wrapper 查询wrapper
     * @return com.alibaba.fastjson2.JSONArray
     */
    JSONArray joinJsonList(@Param(Constants.WRAPPER) Wrapper<T> wrapper);

    /**
     * 内连接分页查询
     *
     * @param page    分页参数
     * @param wrapper 查询wrapper
     * @return com.alibaba.fastjson2.JSONArray
     */
    <P extends IPage<JSONObject>> P joinJsonPage(P page, @Param(Constants.WRAPPER) Wrapper<T> wrapper);
}
