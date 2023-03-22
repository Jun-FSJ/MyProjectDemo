package com.joon.demo.mproot.query;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.joon.demo.mproot.exception.BusinessException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.lang.invoke.MethodHandle;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 元数据
 *
 * @author Joon
 * @date 2023-03-17 10:11
 */
@Slf4j
@Setter
@Getter
class Meta extends ColumnFunction<Meta> {
    /**
     * 执行方法
     */
    private MethodHandle method;
    /**
     * 查询关键字
     */
    private SqlKeyword sqlKeyword = SqlKeyword.EQ;
    /**
     * 拼接排序
     */
    private int sort;

    public Meta(String columnName) {
        super(columnName);
    }

    /**
     * 构建参数
     *
     * @param wrapper  查询wrapper
     * @param instance 实例对象
     */
    public void buildParam(LambdaQueryWrapper<?> wrapper, Object instance) {
        try {
            if (!this.needParam()) {
                wrapper.getMethodHandle(this.getSqlKeyword().name()).invoke(wrapper, this);
                return;
            }
            // 获取当前字段的参数。如果为空不处理
            Object param = this.getMethod().invoke(instance);
            if (ObjectUtils.isEmpty(param)) {
                return;
            }
            if (!this.isBetween()) {
                wrapper.getMethodHandle(this.getSqlKeyword().name()).invoke(wrapper, this, param);
            } else if (param instanceof Iterable) {
                Iterator<?> it = ((Iterable<?>) param).iterator();
                wrapper.getMethodHandle(this.getSqlKeyword().name()).invoke(wrapper, this, it.next(), it.next());
            } else {
                throw new IllegalArgumentException("范围查询参数需要实现Iterable接口");
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (NoSuchElementException e) {
            throw new BusinessException("请求参数校验异常");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断是否为between
     *
     * @return boolean
     */
    public boolean isBetween() {
        boolean flag;
        switch (sqlKeyword) {
            case BETWEEN:
            case NOT_BETWEEN:
                flag = true;
                break;
            default:
                flag = false;
                break;
        };
        return flag;
    }

    /**
     * 判断是否需要参数
     *
     * @return boolean
     */
    public boolean needParam() {
        boolean flag;
        switch (sqlKeyword) {
            case IS_NULL:
            case IS_NOT_NULL:
            case GROUP_BY:
            case ASC:
            case DESC:
                flag = false;
                break;
            default:
                flag = true;
                break;
        }
        return flag;
    }
}
