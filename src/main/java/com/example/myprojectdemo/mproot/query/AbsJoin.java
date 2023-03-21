package com.example.myprojectdemo.mproot.query;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.example.myprojectdemo.mproot.utils.ColumnsUtils;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 断言工具类
 *
 * @author Joon
 * @date 2023-03-17 14:29
 */
public abstract class AbsJoin<JT, T> extends LambdaQueryWrapper<T> {
    /** 关联查询的参数名称步长 */
    public static final int PARAM_NAME_SEQ_STEP = 100_0000;
    /** AND拼接符 */
    public static final String AND = StringUtils.SPACE + TableInfo.AND.toUpperCase() + StringUtils.SPACE;
    /** 当前表的信息 */
    protected final TableInfo tableInfo;
    /** 列名的前缀 */
    protected final String columnPref;
    /** 拼接表的前缀 */
    protected final String joinColumnPref;
    /** 查询条件处理 */
    @Getter
    private StringBuilder condition;

    public AbsJoin(int num, Class<JT> jtClass, Class<T> clazz) {
        super();
        if (num < 0 || Integer.MAX_VALUE / PARAM_NAME_SEQ_STEP <= num) {
            throw new IllegalArgumentException("参数num非法");
        }
        this.setEntityClass(clazz);
        this.tableInfo = TableInfoHelper.getTableInfo(clazz);
        this.columnPref = tableInfo.getTableName() + TableInfo.DOT;
        this.joinColumnPref = TableInfoHelper.getTableInfo(jtClass).getTableName() + TableInfo.DOT;
        this.paramNameSeq.set(PARAM_NAME_SEQ_STEP * num);
    }

    /**
     * 表和链接表的关联条件
     * demo:
     * Aaaa:aaaa, Bbbb:bbb => atable.aaaa = btable.bbb
     *
     * @param jtFunction 链接表的字段
     * @param sFunction  关联表的字段
     * @return co.yixiang.common.mproot.query.Join
     */
    public LambdaQueryWrapper<T> on(SFunction<JT, ?> jtFunction, SFunction<T, ?> sFunction) {
        return on(ColumnsUtils.columns(jtFunction), ColumnsUtils.columns(sFunction));
    }

    /**
     * 表和链接表的关联条件
     * demo:
     * Aaaa:aaaa, Bbbb:bbb => atable.aaaa = btable.bbb
     *
     * @param jtColumn 链接表的字段
     * @param column   关联表的字段
     * @return co.yixiang.common.mproot.query.Join
     */
    private LambdaQueryWrapper<T> on(String jtColumn, String column) {
        this.initCondition();
        this.condition.append(joinColumnPref).append(jtColumn);
        this.condition.append(TableInfo.EQUALS);
        this.condition.append(columnPref).append(column);
        return this;
    }

    /**
     * 获取条件拼接对象，如果为null则初始化
     *
     * @return StringBuilder
     */
    private void initCondition() {
        if (this.condition != null) {
            this.condition.append(AND);
        } else {
            // 拼接前缀如 inner join tableName ON
            this.condition = new StringBuilder(joinDelimiter()).append(tableName()).append(" ON ");
        }
    }

    /**
     * 关联表的表名
     *
     * @return 表名称
     */
    public String tableName() {
        return tableInfo.getTableName();
    }

    /**
     * 返回链接符，如 inner join, left join 等
     *
     * @return java.lang.String
     */
    abstract String joinDelimiter();
}
