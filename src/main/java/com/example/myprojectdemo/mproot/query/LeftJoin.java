package com.example.myprojectdemo.mproot.query;

/**
 * left join查询
 *
 * @author Joon
 * @date 2023-03-17 09:39
 */
public class LeftJoin<JT, T> extends AbsJoin<JT, T> {

    public LeftJoin(int num, Class<JT> jtClass, Class<T> clazz) {
        super(num, jtClass, clazz);
    }

    @Override
    String joinDelimiter() {
        return " LEFT JOIN ";
    }
}
