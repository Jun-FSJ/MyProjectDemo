package com.joon.demo.mproot.query;

/**
 * Inner join查询
 *
 * @author Joon
 * @date 2023-03-17 09:39
 */
public class InnerJoin<JT, T> extends AbsJoin<JT, T> {

    public InnerJoin(int num, Class<JT> jtClass, Class<T> clazz) {
        super(num, jtClass, clazz);
    }

    @Override
    String joinDelimiter() {
        return " INNER JOIN ";
    }
}
