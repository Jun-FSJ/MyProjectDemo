package com.joon.demo.mproot.injector;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 查询json方法
 *
 * @author Joon
 * @date 2023-03-17 15:05
 */
class ListJson extends AbstractMethod {
    private static final String METHOD_NAME = "listJson";

    public ListJson() {
        super(METHOD_NAME);
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
        String sql = String.format(SqlMethod.SELECT_MAPS.getSql(), sqlFirst(), sqlSelectColumns(tableInfo, true),
                tableInfo.getTableName(), sqlWhereEntityWrapper(true, tableInfo), sqlOrderBy(tableInfo), sqlComment());

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addSelectMappedStatementForOther(mapperClass, METHOD_NAME, sqlSource, JSONObject.class);
    }
}
