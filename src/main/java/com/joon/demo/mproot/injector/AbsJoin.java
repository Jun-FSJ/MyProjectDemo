package com.joon.demo.mproot.injector;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils.convertIf;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 查询json方法
 *
 * @author Joon
 * @date 2023-03-17 15:05
 */
abstract class AbsJoin extends AbstractMethod {
    private final String methodName;

    public AbsJoin(String methodName) {
        super(methodName);
        this.methodName = methodName;
    }

    /**
     * 注入自定义 MappedStatement
     *
     * @param mapperClass mapper 接口
     * @param modelClass  mapper 泛型
     * @param tableInfo   数据库表反射信息
     * @return MappedStatement
     */
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sqlScript = "<script>%s SELECT %s FROM %s %s %s %s %s\n</script>";
        CustomerTable table = new CustomerTable(tableInfo);

        String sql = String.format(sqlScript, sqlFirst(), sqlSelectColumns(table, true), tableInfo.getTableName(),
                sqlInnerJoin(), sqlWhereEntityWrapper(true, table), sqlOrderBy(tableInfo), sqlComment());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addSelectMappedStatementForOther(mapperClass, methodName, sqlSource, this.resultType(tableInfo));
    }

    @Override
    protected String sqlOrderBy(TableInfo tableInfo) {

        /* 不存在排序字段，直接返回空 */
        List<TableFieldInfo> orderByFields = tableInfo.getOrderByFields();
        if (CollectionUtils.isEmpty(orderByFields)) {
            return StringPool.EMPTY;
        }
        orderByFields.sort(Comparator.comparingInt(TableFieldInfo::getOrderBySort));
        StringBuilder sql = new StringBuilder();
        sql.append(NEWLINE).append(" ORDER BY ");
        Function<TableFieldInfo, String> mapper = this.orderSql(tableInfo);
        sql.append(orderByFields.stream().map(mapper).collect(joining(",")));
        /* 当wrapper中传递了orderBy属性，@orderBy注解失效 */
        return convertIf(sql.toString(), String.format("%s == null or %s", WRAPPER, WRAPPER_EXPRESSION_ORDER), true);
    }

    /**
     * 拼接order语句
     *
     * @param tableInfo 表信息
     * @return order 语句的function
     */
    private Function<TableFieldInfo, String> orderSql(TableInfo tableInfo) {
        return (tfi) -> String.format("%s %s", this.joinTable(tableInfo, tfi.getColumn()), tfi.getOrderByType());
    }

    private String joinTable(TableInfo table, String sqlSelect) {
        return table.getTableName() + DOT + sqlSelect;
    }

    /**
     * 返回结果类型
     *
     * @param tableInfo 表信息
     * @return 查询返回结果的类型
     */
    protected Class<?> resultType(TableInfo tableInfo) {
        return tableInfo.getEntityType();
    }

    /**
     * innerJoin 判断条件脚本
     *
     * @return 链接脚本
     */
    private String sqlInnerJoin() {
        return "<if test=\"ew != null and ew.isJoin()\">\n" +
                "                   ${ew.joinCondition}\n" +
                "                </if>";
    }


    class CustomerTable extends TableInfo {
        private TableInfo table;

        public CustomerTable(TableInfo info) {
            super(info.getEntityType());
            this.table = info;
        }

        @Override
        public String getResultMap() {
            return table.getResultMap();
        }

        @Override
        public boolean isAutoInitResultMap() {
            return table.isAutoInitResultMap();
        }

        @Override
        public boolean isWithLogicDelete() {
            return table.isWithLogicDelete();
        }

        @Override
        public String chooseSelect(Predicate<TableFieldInfo> predicate) {
            String keySelect = this.joinTable(table.getKeySqlSelect());
            String fss = table.getFieldList().stream().filter(predicate).map(this::joinTable).collect(joining(COMMA));
            if (StringUtils.isNoneBlank(keySelect, fss)) {
                return keySelect + COMMA + fss;
            }
            return isNotBlank(fss) ? fss : keySelect;
        }

        @Override
        public String getAllSqlWhere(boolean ignoreLogicDelFiled, boolean withId, String prefix) {
            final String newPrefix = StringUtils.defaultString(prefix, EMPTY);
            String filedSqlScript = table.getFieldList().stream().filter(i -> {
                if (ignoreLogicDelFiled) {
                    return !(isWithLogicDelete() && i.isLogicDelete());
                }
                return true;
            }).map(i -> this.where(i, newPrefix)).filter(Objects::nonNull).collect(joining(NEWLINE));
            if (!withId || StringUtils.isBlank(table.getKeyProperty())) {
                return filedSqlScript;
            }

            String property = newPrefix + table.getKeyProperty();
            String ks = this.joinTable(table.getKeyColumn()) + EQUALS + SqlScriptUtils.safeParam(property);
            return convertIf(ks, String.format("%s != null", property), false) + NEWLINE + filedSqlScript;
        }

        private String where(TableFieldInfo info, String pref) {
            if (info.getWhereStrategy() == FieldStrategy.NEVER) {
                return null;
            }
            String script = " AND " + String.format(info.getCondition(), joinTable(info.getColumn()), pref + info.getEl());
            if (info.isPrimitive() || info.getWhereStrategy() == FieldStrategy.IGNORED) {
                return script;
            }

            String property = this.convertIfProperty(pref, info.getProperty());
            if (info.getWhereStrategy() == FieldStrategy.NOT_EMPTY && info.isCharSequence()) {
                return convertIf(script, String.format("%s != null and %s != ''", property, property), false);
            }
            return convertIf(script, String.format("%s != null", property), false);
        }

        private String convertIfProperty(String prefix, String property) {
            return isNotBlank(prefix) ? prefix.substring(0, prefix.length() - 1) + "['" + property + "']" : property;
        }

        private String joinTable(TableFieldInfo fieldInfo) {
            return this.joinTable(fieldInfo.getSqlSelect());
        }

        private String joinTable(String sqlSelect) {
            return AbsJoin.this.joinTable(table, sqlSelect);
        }
    }
}
