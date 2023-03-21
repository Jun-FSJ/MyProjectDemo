package com.example.myprojectdemo.mproot.common.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.example.myprojectdemo.mproot.Save;
import com.example.myprojectdemo.mproot.Update;
import com.example.myprojectdemo.mproot.common.mapper.CoreMapper;
import com.example.myprojectdemo.mproot.common.service.Service;
import org.apache.ibatis.binding.MapperMethod;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.baomidou.mybatisplus.core.toolkit.StringUtils.checkValNull;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * 基础service
 *
 * @author Joon
 * @date 2023-03-17 09:58
 */
public class ServiceImpl<M extends CoreMapper<T>, T> extends com.baomidou.mybatisplus.extension.service.impl.ServiceImpl<M, T> implements Service<T> {

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    public boolean save(T entity) {
        this.saveCheckHandler(entity);
        return super.save(entity);
    }

    /**
     * 批量插入
     *
     * @param entityList ignore
     * @param batchSize  ignore
     * @return ignore
     */
    @Override
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        return saveBatch(entityList, batchSize, true);
    }

    /**
     * 插入（批量）
     *
     * @param entityList 实体对象集合
     * @param batchSize  插入批次数量
     * @param check      是否执行校验方法
     */
    @Override
    public boolean saveBatch(Collection<T> entityList, int batchSize, boolean check) {
        if (isEmpty(entityList)) {
            return false;
        }
        if (check) {
            entityList.forEach(this::saveCheckHandler);
        }
        return super.saveBatch(entityList, batchSize);
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
        return SqlHelper.saveOrUpdateBatch(this.entityClass, this.mapperClass, this.log, entityList, batchSize,
                (sqlSession, entity) -> {
            Object idVal = tableInfo.getPropertyValue(entity, keyProperty);
            if (checkValNull(idVal) || isEmpty(sqlSession.selectList(getSqlStatement(SqlMethod.SELECT_BY_ID), entity))) {
                this.saveCheckHandler(entity);
                return true;
            } else {
                this.updateCheckHandler(entity);
                return false;
            }
        }, (sqlSession, entity) -> {
            MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
            param.put(Constants.ENTITY, entity);
            sqlSession.update(getSqlStatement(SqlMethod.UPDATE_BY_ID), param);
        });
    }

    /**
     * 根据 ID 选择修改
     *
     * @param entity 实体对象
     */
    @Override
    public boolean updateById(T entity) {
        if (entity == null) {
            return false;
        }
        this.updateCheckHandler(entity);
        return super.updateById(entity);
    }

    @Override
    public boolean updateBatchById(Collection<T> entityList, int batchSize) {
        entityList.forEach(this::updateCheckHandler);
        return super.updateBatchById(entityList, batchSize);
    }

    /**
     * 根据 whereEntity 条件，更新记录
     *
     * @param entity        实体对象
     * @param updateWrapper 实体对象封装操作类 {@link UpdateWrapper}
     */
    @Override
    public boolean update(T entity, Wrapper<T> updateWrapper) {
        this.updateCheckHandler(entity);
        return Service.super.update(entity, updateWrapper);
    }

    /**
     * 保存的公共部分，如果实现了Save接口，则执行填充uuid和校验必填参数方法调用
     *
     * @param entity 实体对象
     */
    protected void saveCheckHandler(T entity) {
        if (entity instanceof Save) {
            Save save = (Save) entity;
            save.generatorUniqueCode();
            save.checkRequire();
        }
        this.saveOrUpdateCheckHandler(entity);
    }

    /**
     * 保存的公共部分，如果实现了Save接口，则执行填充uuid和校验必填参数方法调用
     *
     * @param entity 实体对象
     */
    protected void updateCheckHandler(T entity) {
        if (entity instanceof Update) {
            Update update = (Update) entity;
            update.checkRequire4Update();
        }
        this.saveOrUpdateCheckHandler(entity);
    }

    protected void saveOrUpdateCheckHandler(T entity) {
    }

    /**
     * 根据 Wrapper，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param throwEx      有多个 result 是否抛出异常
     */
    @Override
    public T getOne(Wrapper<T> queryWrapper, boolean throwEx) {
        return super.getOne(this.executeWrapper(queryWrapper), throwEx);
    }

    /**
     * 根据 Wrapper，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    @Override
    public Map<String, Object> getMap(Wrapper<T> queryWrapper) {
        return super.getMap(this.executeWrapper(queryWrapper));
    }

    /**
     * 根据 Wrapper，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param mapper       转换函数
     */
    @Override
    public <V> V getObj(Wrapper<T> queryWrapper, Function<? super Object, V> mapper) {
        return super.getObj(this.executeWrapper(queryWrapper), mapper);
    }

    /**
     * 查询json数组
     *
     * @param queryWrapper 查询wrapper
     * @return com.alibaba.fastjson2.JSONArray
     */
    @Override
    public JSONArray listJson(Wrapper<T> queryWrapper) {
        return this.getBaseMapper().listJson(this.executeWrapper(queryWrapper));
    }

    /**
     * 内连接查询集合
     *
     * @param wrapper 查询wrapper
     * @return com.alibaba.fastjson2.JSONArray
     */
    @Override
    public T join(Wrapper<T> wrapper) {
        return this.getBaseMapper().join(this.executeWrapper(wrapper));
    }

    /**
     * 内连接查询集合
     *
     * @param wrapper 查询wrapper
     * @return com.alibaba.fastjson2.JSONArray
     */
    @Override
    public List<T> joinList(Wrapper<T> wrapper) {
        return this.getBaseMapper().joinList(this.executeWrapper(wrapper));
    }

    /**
     * 内连接分页查询
     *
     * @param page    分页参数
     * @param wrapper 查询wrapper
     * @return com.alibaba.fastjson2.JSONArray
     */
    @Override
    public <P extends IPage<T>> P joinPage(P page, Wrapper<T> wrapper) {
        return this.getBaseMapper().joinPage(page, this.executeWrapper(wrapper));
    }

    /**
     * 内连接查询集合
     *
     * @param wrapper 查询wrapper
     * @return com.alibaba.fastjson2.JSONArray
     */
    @Override
    public JSONObject joinJson(Wrapper<T> wrapper) {
        return this.getBaseMapper().joinJson(this.executeWrapper(wrapper));
    }

    /**
     * 内连接查询集合
     *
     * @param wrapper 查询wrapper
     * @return com.alibaba.fastjson2.JSONArray
     */
    @Override
    public JSONArray joinJsonList(Wrapper<T> wrapper) {
        return this.getBaseMapper().joinJsonList(this.executeWrapper(wrapper));
    }

    /**
     * 内连接分页查询
     *
     * @param page    分页参数
     * @param wrapper 查询wrapper
     * @return com.alibaba.fastjson2.JSONArray
     */
    @Override
    public <P extends IPage<JSONObject>> P joinJsonPage(P page, Wrapper<T> wrapper) {
        return this.getBaseMapper().joinJsonPage(page, this.executeWrapper(wrapper));
    }
}
