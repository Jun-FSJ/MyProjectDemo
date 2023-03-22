package com.joon.demo.mproot.query;

/**
 * right join查询
 *
 * @author Joon
 * @date 2023-03-17 09:39
 */
public class RightJoin<JT, T> extends AbsJoin<JT, T> {

    public RightJoin(int num, Class<JT> jtClass, Class<T> clazz) {
        super(num, jtClass, clazz);
    }

    @Override
    String joinDelimiter() {
        return " RIGHT JOIN ";
    }
}
