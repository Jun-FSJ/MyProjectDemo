package com.example.myprojectdemo.mproot.query;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.example.myprojectdemo.mproot.annotation.Group;
import com.example.myprojectdemo.mproot.common.mapper.CoreMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.GenericTypeResolver;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 链式查询 lambda 式
 *
 * @author Joon
 * @date 2023-03-17 13:43
 */
public class LambdaQueryChainWrapper<T> extends com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper<T> {
    private static final long serialVersionUID = 5349352242045372776L;

    /**
     * 统计的sql语句
     */
    private static final String COUNT_SQL = "COUNT(%s) AS %s";
    /**
     * 查询json
     */
    private static final String JSON_CONTAINS = "JSON_CONTAINS(%s, %s)";
    /**
     * 查询整型json
     */
    private static final String JSON_CONTAINS_NUMBER_VAL = "JSON_CONTAINS(%s, '%s')";
    /**
     * 查询字符串json
     */
    private static final String JSON_CONTAINS_VARCHAR_VAL = "JSON_CONTAINS(%s, '\"%s\"')";

    /**
     * 分组查询的map缓存
     */
    private final static Map<Field, Set<String>> GROUP_MAP = new HashMap<>();
    @SuppressWarnings("all")
    public LambdaQueryChainWrapper(CoreMapper<T> mapper) {
        this(mapper, new LambdaQueryWrapper<>());
    }

    public LambdaQueryChainWrapper(CoreMapper<T> mapper, LambdaQueryWrapper<T> wrapper) {
        super(mapper);
        wrapper.setEntityClass((Class<T>) GenericTypeResolver.resolveTypeArguments(mapper.getClass(), Mapper.class)[0]);
        super.wrapperChildren = wrapper;
    }

    @Override
    public CoreMapper<T> getBaseMapper() {
        return (CoreMapper<T>) super.getBaseMapper();
    }

    @Override
    public LambdaQueryWrapper<T> getWrapper() {
        return (LambdaQueryWrapper<T>) wrapperChildren;
    }

    /**
     * 重写获取列名的方法
     *
     * @param column 列
     * @return String
     */
    public String columnToString(SFunction<T, ?> column) {
        return this.getWrapper().columnToString(column, true);
    }

    /**
     * 获取count
     *
     * @param column 求count的列
     * @return this
     */
    public LambdaQueryChainWrapper<T> selectCount(String column) {
        return this.selectCount(column, column);
    }

    /**
     * 获取count
     *
     * @param column    求count的列
     * @param alisaName 别名
     * @return this
     */
    public LambdaQueryChainWrapper<T> selectCount(String column, String alisaName) {
        // 1. 获取原有查询sql语句
        String oldSqlSelect = super.wrapperChildren.getSqlSelect();
        // 2.组建count语句
        String countSql = String.format(COUNT_SQL, column, alisaName);
        // 3.拼接原始语句和count语句
        if (StringUtils.isNotBlank(oldSqlSelect)) {
            countSql = oldSqlSelect + StringPool.COMMA + countSql;
        }
        // 4.设置查询语句
        super.wrapperChildren.select(new ColumnFunction<>(countSql));
        return this;
    }

    /**
     * 判断json是否包含val
     * JSON_CONTAINS(corner_ids, JSON_ARRAY(1))
     *
     * @param column 列名
     * @param val    值
     * @return this
     */
    public LambdaQueryChainWrapper<T> jsonContains(SFunction<T, ?> column, String val) {
        return jsonContains(true, column, val);
    }

    /**
     * 判断json是否包含val
     * exp不为空：JSON_CONTAINS(corner_ids -> exp, JSON_ARRAY(1))
     * exp为空: JSON_CONTAINS(corner_ids, JSON_ARRAY(1))
     *
     * @param column 列名
     * @param exp    jsonContains的表达式,
     * @param val    值
     * @return this
     */
    public LambdaQueryChainWrapper<T> jsonContains(SFunction<T, ?> column, String exp, String val) {
        return jsonContains(true, column, exp, val);
    }

    /**
     * 判断json是否包含val
     * JSON_CONTAINS(corner_ids, JSON_ARRAY(1))
     *
     * @param condition 条件
     * @param column    列名
     * @param val       值
     * @return this
     */
    public LambdaQueryChainWrapper<T> jsonContains(boolean condition, SFunction<T, ?> column, String val) {
        return jsonContains(condition, column, null, val);
    }

    /**
     * 判断json是否包含val
     * exp不为空：JSON_CONTAINS(corner_ids -> exp, JSON_ARRAY(1))
     * exp为空: JSON_CONTAINS(corner_ids, JSON_ARRAY(1))
     *
     * @param column 列名
     * @param exp    jsonContains的表达式,
     * @param val    值
     * @return this
     */
    public LambdaQueryChainWrapper<T> jsonContains(boolean condition, SFunction<T, ?> column, String exp, String val) {
        if (condition) {
            this.jsonContains(JSON_CONTAINS, this.jsonContainsColumn(column, exp), val);
        }
        return this;
    }

    /**
     * 判断json是否包含val
     * JSON_CONTAINS(corner_ids, '1')
     *
     * @param column 列名
     * @param val    值
     * @return this
     */
    public LambdaQueryChainWrapper<T> jsonContainsVal(SFunction<T, ?> column, Serializable val) {
        return jsonContainsVal(true, column, val);
    }

    /**
     * 判断json是否包含val
     * exp不为空：JSON_CONTAINS(corner_ids -> exp, JSON_ARRAY(1))
     * exp为空: JSON_CONTAINS(corner_ids, JSON_ARRAY(1))
     *
     * @param column 列名
     * @param exp    jsonContains的表达式,
     * @param val    值
     * @return this
     */
    public LambdaQueryChainWrapper<T> jsonContainsVal(SFunction<T, ?> column, String exp, Serializable val) {
        return jsonContainsVal(true, column, exp, val);
    }

    /**
     * 判断json是否包含val
     * JSON_CONTAINS(corner_ids, '1')
     *
     * @param column 列名
     * @param val    值
     * @return this
     */
    public LambdaQueryChainWrapper<T> jsonContainsVal(boolean condition, SFunction<T, ?> column, Serializable val) {
        return jsonContainsVal(condition, column, null, val);
    }

    /**
     * 判断json是否包含val
     * exp不为空：JSON_CONTAINS(corner_ids -> exp, JSON_ARRAY(1))
     * exp为空: JSON_CONTAINS(corner_ids, JSON_ARRAY(1))
     *
     * @param column 列名
     * @param exp    jsonContains的表达式,
     * @param val    值
     * @return this
     */
    public LambdaQueryChainWrapper<T> jsonContainsVal(boolean condition, SFunction<T, ?> column, String exp,
                                                      Serializable val) {
        if (condition) {
            String conditionExp = val instanceof Number ? JSON_CONTAINS_NUMBER_VAL : JSON_CONTAINS_VARCHAR_VAL;
            this.jsonContains(conditionExp, this.jsonContainsColumn(column, exp), val);
        }
        return this;
    }

    /**
     * 解析json查询的列
     *
     * @param column 解析列名的function
     * @param exp    json的查询表达式
     * @return 最终列
     */
    private String jsonContainsColumn(SFunction<T, ?> column, String exp) {
        String columnName = this.columnToString(column);
        return exp == null ? columnName : columnName + "->'" + exp + "'";
    }

    /**
     * 判断是否包含json
     *
     * @param sqlStr sql字符串
     * @param column 列
     * @param val    值
     */
    private void jsonContains(String sqlStr, String column, Serializable val) {
        super.wrapperChildren.apply(String.format(sqlStr, column, val));
    }

    /**
     * 获取count
     *
     * @param column 求count的列
     * @return this
     */
    public LambdaQueryChainWrapper<T> selectCount(SFunction<T, String> column) {
        String columnName = this.columnToString(column);
        return this.selectCount(columnName, columnName);
    }

    /**
     * 获取count
     *
     * @param column    求count的列
     * @param alisaName 别名
     * @return this
     */
    public LambdaQueryChainWrapper<T> selectCount(SFunction<T, String> column, String alisaName) {
        return this.selectCount(this.columnToString(column), alisaName);
    }

    /**
     * 根据Group的组自动筛选
     *
     * @param groupName 组名
     * @return LambdaQueryChainWrapper
     */
    public LambdaQueryChainWrapper<T> select(String groupName) {
        if (StringUtils.isNotBlank(groupName)) {
            this.select(info -> isGroup(info.getField(), groupName));
        }
        return this;
    }

    /**
     * 不需要查询的列
     *
     * @param columns 排除的列
     * @return LambdaQueryChainWrapper
     */
    @SafeVarargs
    public final LambdaQueryChainWrapper<T> excludeSelect(SFunction<T, ?>... columns) {
        if (ArrayUtils.getLength(columns) > 0) {
            Set<String> columnSet = Arrays.stream(columns).map(this::columnToString).collect(Collectors.toSet());
            this.select(info -> !columnSet.contains(info.getColumn()));
        }
        return this;
    }

    /**
     * 判断是否为组内的数据
     *
     * @param field     字段
     * @param groupName 组名
     * @return 判断结果
     */
    private boolean isGroup(Field field, String groupName) {
        Set<String> set = GROUP_MAP.computeIfAbsent(field, k -> {
            Group anno = field.getAnnotation(Group.class);
            return anno == null ? Collections.emptySet() : Arrays.stream(anno.value()).collect(Collectors.toSet());
        });
        return set.contains(groupName);
    }

    /**
     * 清除关联条件
     */
    public LambdaQueryChainWrapper<T> clearJoin() {
        this.getWrapper().clearJoin();
        return this;
    }

    /**
     * 内连接
     *
     * @param clazz    关联表实体class
     * @param consumer 内连接对象消费者
     * @param <T1>     关联表实体
     * @return this
     */
    public <T1> LambdaQueryChainWrapper<T> innerJoin(Class<T1> clazz, Consumer<AbsJoin<T, T1>> consumer) {
        consumer.accept(this.innerJoin(clazz));
        return this;
    }

    /**
     * 内连接
     *
     * @param clazz 关联表实体class
     * @param <T1>  关联表实体
     * @return this
     */
    public <T1> InnerJoin<T, T1> innerJoin(Class<T1> clazz) {
        return this.getWrapper().innerJoin(clazz);
    }

    /**
     * 内连接
     *
     * @param clazz    关联表实体class
     * @param consumer 内连接对象消费者
     * @param <T1>     关联表实体
     * @return this
     */
    public <T1> LambdaQueryChainWrapper<T> leftJoin(Class<T1> clazz, Consumer<AbsJoin<T, T1>> consumer) {
        consumer.accept(this.leftJoin(clazz));
        return this;
    }

    /**
     * 内连接
     *
     * @param clazz 关联表实体class
     * @param <T1>  关联表实体
     * @return this
     */
    public <T1> LeftJoin<T, T1> leftJoin(Class<T1> clazz) {
        return this.getWrapper().leftJoin(clazz);
    }

    /**
     * 内连接
     *
     * @param clazz    关联表实体class
     * @param consumer 内连接对象消费者
     * @param <T1>     关联表实体
     * @return this
     */
    public <T1> LambdaQueryChainWrapper<T> rightJoin(Class<T1> clazz, Consumer<AbsJoin<T, T1>> consumer) {
        consumer.accept(this.rightJoin(clazz));
        return this;
    }

    /**
     * 内连接
     *
     * @param clazz 关联表实体class
     * @param <T1>  关联表实体
     * @return this
     */
    public <T1> RightJoin<T, T1> rightJoin(Class<T1> clazz) {
        return this.getWrapper().rightJoin(clazz);
    }

    /**
     * 关联查询
     *
     * @return 返回一条数据
     */
    public T join() {
        return this.getBaseMapper().join(this.supportJoin());
    }

    /**
     * 关联查询
     *
     * @return 返回数据集合
     */
    public List<T> joinList() {
        return this.getBaseMapper().joinList(this.supportJoin());
    }

    /**
     * 关联查询
     *
     * @param page 分页参数
     * @param <P>
     * @return 返回分页数据集合
     */
    public <P extends IPage<T>> P joinPage(P page) {
        return getBaseMapper().joinPage(page, this.supportJoin());
    }

    /**
     * 关联查询
     *
     * @return 返回一条json数据
     */
    public JSONObject joinJson() {
        return this.getBaseMapper().joinJson(this.supportJoin());
    }

    /**
     * 关联查询
     *
     * @return 返回分页Json数据集合
     */
    public JSONArray joinJsonList() {
        return this.getBaseMapper().joinJsonList(this.supportJoin());
    }

    /**
     * 关联查询
     *
     * @param page 分页参数
     * @param <P>
     * @return 返回分页Json数据集合
     */
    public <P extends IPage<JSONObject>> P joinJsonPage(P page) {
        return getBaseMapper().joinJsonPage(page, this.supportJoin());
    }

    /**
     * 检验是否支持join查询
     *
     * @return 查询wrapper
     */
    private LambdaQueryWrapper<T> supportJoin() {
        LambdaQueryWrapper<T> wrapper = this.getWrapper();
        if (!wrapper.isJoin()) {
            throw new UnsupportedOperationException("Please fill join query criteria");
        }
        return wrapper;
    }
}
