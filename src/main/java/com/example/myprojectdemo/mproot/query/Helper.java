package com.example.myprojectdemo.mproot.query;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.myprojectdemo.mproot.query.annotation.Query;
import com.example.myprojectdemo.mproot.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 查询基础BO
 *
 * @author Joon
 * @date 2023-03-17 16:19
 */
@Slf4j
public class Helper {

    /** 元数据缓存 */
    private static final Map<Class<?>, List<Meta>> CACHE = new ConcurrentHashMap<>();

    private Helper() {
    }

    /**
     * 单例对象
     */
    private interface Instance {
        Helper INSTANCE = new Helper();
    }

    /**
     * 获取wrapper
     *
     * @param instance 需要映射的对象
     * @param <T>      泛型
     * @return LambdaQueryWrapper
     */
    public static <T> LambdaQueryWrapper<T> queryWrapper(Object instance) {
        return Instance.INSTANCE.getQueryWrapper(instance);
    }

    /**
     * 获取wrapper
     *
     * @param instance 需要映射的对象
     * @param <T>      泛型
     * @return LambdaQueryWrapper
     */
    public static <T> LambdaQueryWrapper<T> queryWrapper(Object instance, Class<T> clazz) {
        return Instance.INSTANCE.getQueryWrapper(instance, clazz);
    }

    /**
     * 获取wrapper
     *
     * @param instance 需要映射的对象
     * @param <T>      泛型
     * @return LambdaQueryWrapper
     */
    public static <T> LambdaQueryWrapper<T> queryWrapper(Object instance, T entity) {
        return Instance.INSTANCE.getQueryWrapper(instance, entity);
    }

    /**
     * 获取查询wrapper
     *
     * @param <T> 实体
     * @return QueryWrapper
     */
    public <T> LambdaQueryWrapper<T> getQueryWrapper(Object instance, T entity) {
        LambdaQueryWrapper<T> wrapper = getQueryWrapper(instance);
        if (entity != null) {
            wrapper.setEntity(entity);
            wrapper.setEntityClass((Class<T>) entity.getClass());
        }
        return wrapper;
    }

    /**
     * 获取查询wrapper
     *
     * @param <T> 实体
     * @return QueryWrapper
     */
    public <T> LambdaQueryWrapper<T> getQueryWrapper(Object instance, Class<T> clazz) {
        LambdaQueryWrapper<T> wrapper = getQueryWrapper(instance);
        wrapper.setEntityClass(clazz);
        return wrapper;
    }

    /**
     * 获取查询wrapper
     *
     * @param <T> 实体
     * @return QueryWrapper
     */
    public <T> LambdaQueryWrapper<T> getQueryWrapper(Object instance) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        if (instance == null) {
            return wrapper;
        }

        List<Meta> metas = this.getMetaList(instance.getClass());
        if (!CollectionUtils.isEmpty(metas)) {
            metas.forEach(meta -> meta.buildParam(wrapper, instance));
        }
        return wrapper;
    }

    /**
     * 初始化Meta
     *
     * @param clazz 对应的类型
     * @return Meta
     */
    private List<Meta> getMetaList(Class<?> clazz) {
        List<Meta> metas = CACHE.get(clazz);
        if (metas != null) {
            return metas;
        }
        synchronized (clazz) {
            return CACHE.computeIfAbsent(clazz, this::initMeta);
        }
    }

    /**
     * 初始化data
     *
     * @param clazz Class
     * @return List<Meta>
     */
    private List<Meta> initMeta(Class<?> clazz) {
        List<Meta> metas = new LinkedList<>();
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        for (Field field : FieldUtils.getAllFieldsList(clazz)) {
            Method method = this.getReadMethod(clazz, field.getName());
            if (method == null) {
                continue;
            }

            Query query = AnnotationUtils.findAnnotation(field, Query.class);
            // 如果没有query注解则使用默认配置规则
            if (query == null) {
                metas.add(this.buildMeta(lookup, StringUtils.camelToUnderline(field.getName()), method, null));
            } else if (!query.ignore()) {
                String[] mappings = query.mapping();
                if (ArrayUtils.getLength(mappings) > 0) {
                    for (String mapping : mappings) {
                        metas.add(this.buildMeta(lookup, mapping, method, query));
                    }
                } else {
                    metas.add(this.buildMeta(lookup, field.getName(), method, query));
                }
            }

            // Query query = AnnotationUtils.findAnnotation(field, Query.class);
            // // 如果该字段需要忽略不处理
            // if (query != null && query.ignore()) {
            //     continue;
            // }
            //
            // Meta meta;
            // if (query != null) {
            //     String mapping = StringUtils.isNotBlank(query.mapping()) ? query.mapping() : field.getName();
            //     // 处理需要映射的字段
            //     // 根据是否需要驼峰映射设置列名
            //     meta = new Meta(query.camelToUnderline() ? StringUtils.camelToUnderline(mapping) : mapping);
            //     // 设置查询条件
            //     meta.setSqlKeyword(query.keyword());
            //     meta.setSort(query.index());
            // } else {
            //     meta = new Meta(StringUtils.camelToUnderline(field.getName()));
            // }
            //
            // metas.add(meta);
            // meta.setMethod(ExceptionUtils.convert(() -> lookup.unreflect(method)));
        }
        metas.sort(Comparator.comparingInt(Meta::getSort));
        return metas;
    }

    private Meta buildMeta(MethodHandles.Lookup lookup, String columnName, Method method, Query query) {
        Meta meta;
        if (query != null) {
            meta = new Meta(query.camelToUnderline() ? StringUtils.camelToUnderline(columnName) : columnName);
            meta.setSqlKeyword(query.keyword());
            meta.setSort(query.index());
        } else {
            meta = new Meta(columnName);
        }
        meta.setMethod(ExceptionUtils.convert(() -> lookup.unreflect(method)));
        return meta;
    }

    /**
     * 获取getter
     *
     * @param clazz：字节码
     * @param propertyName：属性名
     * @return getter方法
     */
    private Method getReadMethod(Class<?> clazz, String propertyName) {
        PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(clazz, propertyName);
        if (descriptor == null) {
            return null;
        }
        // 获取getter
        return PropertyUtils.getReadMethod(descriptor);
    }
}


