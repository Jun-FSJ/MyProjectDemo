package com.joon.demo.mproot.exception;

/**
 * 基础异常
 *
 * @author Joon
 * @date 2023-02-18 14:20
 */
public interface AbsException {
    /**
     * 异常错误码
     *
     * @return 错误码
     */
    Integer getCode();

    /**
     * 异常错误信息
     *
     * @return 错误信息
     */
    String getMessage();
}
