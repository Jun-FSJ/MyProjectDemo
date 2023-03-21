package com.example.myprojectdemo.mproot.common.service;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.AbstractChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.myprojectdemo.mproot.common.mapper.CoreMapper;
import com.example.myprojectdemo.mproot.query.Helper;
import com.example.myprojectdemo.mproot.query.LambdaQueryChainWrapper;
import com.example.myprojectdemo.utils.AssertUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 封装 MyBatis Plus Service
 *
 * @author Joon
 * @date 2023-03-17 10:27
 */
public interface Service<T> extends IService<T> {
    /**
     * 链式查询 lambda 式
     * <p>注意：不支持 Kotlin </p>
     *
     * @return LambdaQueryWrapper 的包装类
     */
    @Override
    default LambdaQueryChainWrapper<T> lambdaQuery() {
        return new LambdaQueryChainWrapper<>(this.getBaseMapper());
    }

    /**
     * 翻页查询
     *
     * @param page  翻页对象
     * @param param 查询参数
     */
    default <E extends IPage<T>> E page(E page, T param) {
        return this.page(page, Helper.queryWrapper(param));
    }

    /**
     * 根据 entity 条件，删除记录
     *
     * @param queryWrapper 实体包装类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    @Override
    default boolean remove(Wrapper<T> queryWrapper) {
        return IService.super.remove(this.executeWrapper(queryWrapper));
    }

    /**
     * 根据 whereEntity 条件，更新记录
     *
     * @param entity        实体对象
     * @param updateWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper}
     */
    @Override
    default boolean update(T entity, Wrapper<T> updateWrapper) {
        return IService.super.update(entity, this.executeWrapper(updateWrapper));
    }

    /**
     * 根据 Wrapper 条件，查询总记录数
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    @Override
    default long count(Wrapper<T> queryWrapper) {
        return IService.super.count(this.executeWrapper(queryWrapper));
    }

    /**
     * 获取并校验数据
     *
     * @param id id
     * @return 数据
     */
    default T getAndCheckById(Serializable id) {
        AssertUtils.isNull(id);
        T data = this.getById(id);
        AssertUtils.isNull(data);
        return data;
    }

    /**
     * 查询列表
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    @Override
    default List<T> list(Wrapper<T> queryWrapper) {
        return IService.super.list(this.executeWrapper(queryWrapper));
    }

    /**
     * 查询（根据ID 批量查询）
     *
     * @param idList 主键ID列表
     */
    @Override
    default List<T> listByIds(Collection<? extends Serializable> idList) {
        return CollectionUtil.isEmpty(idList) ? Collections.emptyList() : IService.super.listByIds(idList);
    }

    /**
     * 翻页查询
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    @Override
    default <E extends IPage<T>> E page(E page, Wrapper<T> queryWrapper) {
        return IService.super.page(page, this.executeWrapper(queryWrapper));
    }

    /**
     * 查询列表
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    @Override
    default List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper) {
        return IService.super.listMaps(this.executeWrapper(queryWrapper));
    }

    /**
     * 查询json数组
     *
     * @param queryWrapper 查询wrapper
     * @return com.alibaba.fastjson2.JSONArray
     */
    JSONArray listJson(Wrapper<T> queryWrapper);

    /**
     * 查询对象数据
     *
     * @param queryWrapper 查询wrapper
     * @param clazz        对象字节码
     * @param <E>          对象
     * @return List
     */
    default <E> List<E> listObjs(Wrapper<T> queryWrapper, Class<E> clazz) {
        return this.listJson(this.executeWrapper(queryWrapper)).toList(clazz);
    }

    /**
     * 根据 Wrapper 条件，查询全部记录
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param mapper       转换函数
     */
    @Override
    default <V> List<V> listObjs(Wrapper<T> queryWrapper, Function<? super Object, V> mapper) {
        return IService.super.listObjs(this.executeWrapper(queryWrapper), mapper);
    }

    /**
     * 翻页查询
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    @Override
    default <E extends IPage<Map<String, Object>>> E pageMaps(E page, Wrapper<T> queryWrapper) {
        return IService.super.pageMaps(page, queryWrapper);
    }

    /**
     * 内连接查询集合
     *
     * @param wrapper 查询wrapper
     * @return com.alibaba.fastjson2.JSONArray
     */
    T join(Wrapper<T> wrapper);

    /**
     * 内连接查询集合
     *
     * @param wrapper 查询wrapper
     * @return com.alibaba.fastjson2.JSONArray
     */
    List<T> joinList(Wrapper<T> wrapper);

    /**
     * 内连接分页查询
     *
     * @param page    分页参数
     * @param wrapper 查询wrapper
     * @return com.alibaba.fastjson2.JSONArray
     */
    <P extends IPage<T>> P joinPage(P page, Wrapper<T> wrapper);

    /**
     * 内连接查询集合
     *
     * @param wrapper 查询wrapper
     * @return com.alibaba.fastjson2.JSONArray
     */
    JSONObject joinJson(Wrapper<T> wrapper);

    /**
     * 内连接查询集合
     *
     * @param wrapper 查询wrapper
     * @return com.alibaba.fastjson2.JSONArray
     */
    JSONArray joinJsonList(Wrapper<T> wrapper);

    /**
     * 内连接分页查询
     *
     * @param page    分页参数
     * @param wrapper 查询wrapper
     * @return com.alibaba.fastjson2.JSONArray
     */
    <P extends IPage<JSONObject>> P joinJsonPage(P page, Wrapper<T> wrapper);

    /**
     * chainWrapper 不能直接执行。需要获取真正的wrapper
     *
     * @param wrapper Wrapper
     * @return Wrapper
     */
    @SuppressWarnings("ALL")
    default Wrapper<T> executeWrapper(Wrapper<T> wrapper) {
        if (wrapper instanceof AbstractChainWrapper) {
            AbstractChainWrapper chainWrapper = (AbstractChainWrapper) wrapper;
            return chainWrapper.getWrapper();
        }
        return wrapper;
    }

    /**
     * 插入（批量）
     *
     * @param entityList 实体对象集合
     * @param check      是否执行校验方法
     */
    default boolean saveBatch(Collection<T> entityList, boolean check) {
        return saveBatch(entityList, DEFAULT_BATCH_SIZE, check);
    }

    /**
     * 插入（批量）
     *
     * @param entityList 实体对象集合
     * @param batchSize  插入批次数量
     * @param check      是否执行校验方法
     */
    boolean saveBatch(Collection<T> entityList, int batchSize, boolean check);

    /**
     * 获取对应 entity 的 BaseMapper
     *
     * @return BaseMapper
     */
    @Override
    CoreMapper<T> getBaseMapper();
}
