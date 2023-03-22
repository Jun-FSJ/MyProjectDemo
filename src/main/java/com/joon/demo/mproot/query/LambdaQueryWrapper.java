package com.joon.demo.mproot.query;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.interfaces.Compare;
import com.baomidou.mybatisplus.core.conditions.interfaces.Func;
import com.baomidou.mybatisplus.core.conditions.segments.AbstractISegmentList;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.joon.demo.mproot.utils.ExceptionUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.core.toolkit.StringPool.COMMA;
import static com.baomidou.mybatisplus.core.toolkit.StringPool.DOT;

/**
 * 自定义wrapper
 *
 * @author Joon
 * @date 2023-03-17 10:41
 */
@Slf4j
public class LambdaQueryWrapper<T> extends com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<T> {
    /**
     * 缓存相关的执行方法
     */
    private static final Map<String, MethodHandle> METHOD = new HashMap<>();
    private static final ISqlSegment[] DEFAULT_SQL_SEGMENTS;
    /**
     * 关联条件
     */
    private SharedString joinCondition;
    /**
     * 关联表需要查询的字段
     */
    private SharedString joinSelectSql;

    @Setter
    private LambdaQueryWrapper<?> parent;
    /**
     * join条件
     */
    private final List<AbsJoin<T, ?>> joins = new LinkedList<>();

    /* 解析LambdaQueryWrapper类支持的方法并缓存 */
    static {
        DEFAULT_SQL_SEGMENTS = new ISqlSegment[]{() -> "1", SqlKeyword.EQ, () -> "1"};
        ExceptionUtils.convert(() -> {
            MethodHandles.Lookup lookup = MethodHandles.lookup();

            MethodType oneType = MethodType.methodType(Object.class, Object.class);
            METHOD.put(SqlKeyword.IS_NULL.name(), lookup.findVirtual(Func.class, "isNull", oneType));
            METHOD.put(SqlKeyword.IS_NOT_NULL.name(), lookup.findVirtual(Func.class, "isNotNull", oneType));
            METHOD.put(SqlKeyword.GROUP_BY.name(), lookup.findVirtual(Func.class, "groupBy", oneType));
            METHOD.put(SqlKeyword.ASC.name(), lookup.findVirtual(Func.class, "orderByAsc", oneType));
            METHOD.put(SqlKeyword.DESC.name(), lookup.findVirtual(Func.class, "orderByDesc", oneType));

            MethodType collectionType = MethodType.methodType(Object.class, Object.class, Collection.class);
            METHOD.put(SqlKeyword.IN.name(), lookup.findVirtual(Func.class, "in", collectionType));
            METHOD.put(SqlKeyword.NOT_IN.name(), lookup.findVirtual(Func.class, "notIn", collectionType));

            MethodType twoType = MethodType.methodType(Object.class, Object.class, Object.class);
            METHOD.put(SqlKeyword.LIKE.name(), lookup.findVirtual(Compare.class, "like", twoType));
            METHOD.put(SqlKeyword.NOT_LIKE.name(), lookup.findVirtual(Compare.class, "notLike", twoType));
            METHOD.put(SqlKeyword.EQ.name(), lookup.findVirtual(Compare.class, "eq", twoType));
            METHOD.put(SqlKeyword.NE.name(), lookup.findVirtual(Compare.class, "ne", twoType));
            METHOD.put(SqlKeyword.GT.name(), lookup.findVirtual(Compare.class, "gt", twoType));
            METHOD.put(SqlKeyword.GE.name(), lookup.findVirtual(Compare.class, "ge", twoType));
            METHOD.put(SqlKeyword.LT.name(), lookup.findVirtual(Compare.class, "lt", twoType));
            METHOD.put(SqlKeyword.LE.name(), lookup.findVirtual(Compare.class, "le", twoType));

            MethodType threeType = MethodType.methodType(Object.class, Object.class, Object.class, Object.class);
            METHOD.put(SqlKeyword.BETWEEN.name(), lookup.findVirtual(Compare.class, "between", threeType));
            METHOD.put(SqlKeyword.NOT_BETWEEN.name(), lookup.findVirtual(Compare.class, "notBetween", threeType));
        });
    }

    /**
     * 重写获取列名的方法
     *
     * @param columnFunction 列
     * @return String
     */
    @Override
    protected String columnToString(SFunction<T, ?> columnFunction, boolean onlyColumn) {
        String sql;
        if (columnFunction instanceof ColumnFunction) {
            sql = columnFunction.apply(null).toString();
        } else {
            sql = super.columnToString(columnFunction, onlyColumn);
        }
        // 如果是join
        if (this.isJoin() || this instanceof AbsJoin<?, ?>) {
            return TableInfoHelper.getTableInfo(this.getEntityClass()).getTableName() + TableInfo.DOT + sql;
        }
        return sql;
    }

    /**
     * 获取执行方法
     *
     * @param methodName 方法名称
     * @return MethodHandle
     */
    public MethodHandle getMethodHandle(String methodName) {
        MethodHandle handle = METHOD.get(methodName);
        if (handle == null) {
            throw new UnsupportedOperationException();
        }
        return handle;
    }

    /**
     * 增加连接条件
     *
     * @param join join对象
     */
    public LambdaQueryWrapper<T> addJoin(AbsJoin<T, ?> join) {
        join.setParent(this);
        this.joins.add(join);
        return this;
    }

    /**
     * 清除关联条件
     */
    public LambdaQueryWrapper<T> clearJoin() {
        this.joins.clear();
        return this;
    }

    /**
     * 获取关联关系的数量
     *
     * @return 数量
     */
    private int joinCount() {
        return this.calcJoinCount(this.getRootWrapper(this));
    }

    /**
     * 获取根wrapper
     *
     * @param wrapper wrapper
     * @return 根wrapper
     */
    private LambdaQueryWrapper<?> getRootWrapper(LambdaQueryWrapper<?> wrapper) {
        if (wrapper.parent == null) {
            return wrapper;
        }
        return this.getRootWrapper(wrapper.parent);
    }

    /**
     * 计算总的链接数量
     *
     * @param wrapper 请求wrapper
     * @return 链接数量
     */
    private int calcJoinCount(LambdaQueryWrapper<?> wrapper) {
        if (wrapper == null || wrapper.joins.isEmpty()) {
            return 1;
        }
        return 1 + wrapper.joins.stream().mapToInt(this::calcJoinCount).sum();
    }

    /**
     * 判断是否为链接查询
     *
     * @return 校验结果
     */
    public boolean isJoin() {
        return !CollectionUtil.isEmpty(this.joins);
    }

    @Override
    public String getSqlFirst() {
        this.initJoinCondition();
        return super.getSqlFirst();
    }

    @Override
    public String getSqlSelect() {
        return this.joinSelectSql == null ? this.getSqlSelect4Join() : this.joinSelectSql.getStringValue();
    }

    private String getSqlSelect4Join() {
        String sql = super.getSqlSelect();
        if (StringUtils.isBlank(sql) || this instanceof AbsJoin<?, ?> || !this.isJoin()) {
            return sql;
        }
        String tablePref = TableInfoHelper.getTableInfo(this.getEntityClass()).getTableName() + DOT;
        return Arrays.stream(sql.split(COMMA)).map(v -> tablePref + v).collect(Collectors.joining(COMMA));
    }

    /**
     * 初始化关联条件
     */
    public void initJoinCondition() {
        if (!this.isJoin() || this.joinCondition != null) {
            return;
        }
        StringBuilder condition = new StringBuilder();
        StringBuilder select = new StringBuilder();
        this.joins.forEach(join -> {
            // 合并on条件
            condition.append(join.getCondition());
            MergeSegments mergeSegments = join.getExpression();
            // 如果有多重join查询
            if (join.isJoin()) {
                join.initJoinCondition();
                condition.append(join.joinCondition());
            } else if (join.nonEmptyOfNormal()) {
                condition.append(AbsJoin.AND).append(mergeSegments.getNormal().getSqlSegment());
            }

            if (StringUtils.isNotBlank(join.getSqlSelect())) {
                select.append(join.getSqlSelect()).append(COMMA);
            }

            // 合并查询参数
            this.paramNameValuePairs.putAll(join.getParamNameValuePairs());

            // 合并聚合函数等
            this.merge(expression.getGroupBy(), mergeSegments.getGroupBy());
            this.merge(expression.getOrderBy(), mergeSegments.getOrderBy());
            this.merge(expression.getHaving(), mergeSegments.getHaving());
        });
        if (StringUtils.isBlank(condition)) {
            throw new IllegalArgumentException("关联查询条件不能为空");
        }
        if (this.isEmptyOfNormal()) {
            if (nonEmptyOfGroupBy() || nonEmptyOfHaving() || nonEmptyOfOrderBy()) {
                expression.add(DEFAULT_SQL_SEGMENTS);
            }
        }
        if (select.length() > 0) {
            String sqlSelect = this.getSqlSelect4Join();
            if (StringUtils.isNotBlank(sqlSelect)) {
                select.append(sqlSelect);
            } else {
                select.setLength(select.length() - 1);
            }
            this.joinSelectSql = new SharedString(select.toString());
        }
        this.joinCondition = new SharedString(condition.toString());
    }

    /**
     * 拼接条件
     *
     * @return 返回拼接的查询条件
     */
    public String joinCondition() {
        return this.joinCondition == null ? null : joinCondition.getStringValue();
    }

    /**
     * 分组条件不为空(不包含entity)
     */
    private boolean nonEmptyOfGroupBy() {
        return CollectionUtil.isNotEmpty(getExpression().getGroupBy());
    }

    /**
     * 排序条件不为空(不包含entity)
     */
    private boolean nonEmptyOfOrderBy() {
        return CollectionUtil.isNotEmpty(getExpression().getOrderBy());
    }

    /**
     * 聚合条件不为空(不包含entity)
     */
    private boolean nonEmptyOfHaving() {
        return CollectionUtil.isNotEmpty(getExpression().getHaving());
    }

    /**
     * segment 合并
     *
     * @param target 目标segment
     * @param source 源segment
     */
    private void merge(AbstractISegmentList target, AbstractISegmentList source) {
        if (CollectionUtil.isEmpty(source)) {
            return;
        }
        if (source.size() == 1) {
            source.add(source.get(0));
        }
        target.addAll(source);
        source.clear();
    }


    /**
     * 内连接
     *
     * @param clazz    关联表实体class
     * @param consumer 内连接对象消费者
     * @param <T1>     关联表实体
     * @return this
     */
    public <T1> InnerJoin<T, T1> innerJoin(Class<T1> clazz, Consumer<AbsJoin<T, T1>> consumer) {
        InnerJoin<T, T1> innerJoin = this.innerJoin(clazz);
        consumer.accept(innerJoin);
        return innerJoin;
    }

    /**
     * 内连接
     *
     * @param clazz 关联表实体class
     * @param <T1>  关联表实体
     * @return this
     */
    public <T1> InnerJoin<T, T1> innerJoin(Class<T1> clazz) {
        InnerJoin<T, T1> join = new InnerJoin<>(this.joinCount(), this.getEntityClass(), clazz);
        this.addJoin(join);
        return join;
    }

    /**
     * 左连接
     *
     * @param clazz    关联表实体class
     * @param consumer 内连接对象消费者
     * @param <T1>     关联表实体
     * @return this
     */
    public <T1> LeftJoin<T, T1> leftJoin(Class<T1> clazz, Consumer<AbsJoin<T, T1>> consumer) {
        LeftJoin<T, T1> leftJoin = this.leftJoin(clazz);
        consumer.accept(leftJoin);
        return leftJoin;
    }

    /**
     * 左连接
     *
     * @param clazz 关联表实体class
     * @param <T1>  关联表实体
     * @return this
     */
    public <T1> LeftJoin<T, T1> leftJoin(Class<T1> clazz) {
        LeftJoin<T, T1> join = new LeftJoin<>(this.joinCount(), this.getEntityClass(), clazz);
        this.addJoin(join);
        return join;
    }

    /**
     * 右连接
     *
     * @param clazz    关联表实体class
     * @param consumer 内连接对象消费者
     * @param <T1>     关联表实体
     * @return this
     */
    public <T1> RightJoin<T, T1> rightJoin(Class<T1> clazz, Consumer<AbsJoin<T, T1>> consumer) {
        RightJoin<T, T1> rightJoin = this.rightJoin(clazz);
        consumer.accept(rightJoin);
        return rightJoin;
    }

    /**
     * 右连接
     *
     * @param clazz 关联表实体class
     * @param <T1>  关联表实体
     * @return this
     */
    public <T1> RightJoin<T, T1> rightJoin(Class<T1> clazz) {
        RightJoin<T, T1> join = new RightJoin<>(this.joinCount(), this.getEntityClass(), clazz);
        this.addJoin(join);
        return join;
    }
}
