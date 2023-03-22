package com.joon.demo.mproot.query;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.Data;

/**
 * 列名Function
 *
 * @author Joon
 * @date 2023-03-17 09:50
 */
@Data
public class ColumnFunction<T> implements SFunction<T, String> {
    /** 列名 */
    private final String column;

    public ColumnFunction(String column) {
        this.column = column;
    }

    @Override
    public String apply(T t) {
        return this.column;
    }
}
