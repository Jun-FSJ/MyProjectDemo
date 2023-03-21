package com.example.myprojectdemo.api;

import com.example.myprojectdemo.mproot.exception.AbsException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * API 响应码
 *
 * @author Joon
 * @date 2023-03-17 16:33
 */
@Getter
@AllArgsConstructor
public enum ApiCode implements AbsException {

    /**
     * 操作成功
     **/
    SUCCESS(HttpStatus.SUCCESS, "操作成功"),
    /**
     * 非法访问
     **/
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "非法访问"),
    /**
     * 没有权限
     **/
    NOT_PERMISSION(HttpStatus.FORBIDDEN, "没有权限"),
    /**
     * 你请求的资源不存在
     **/
    NOT_FOUND(HttpStatus.NOT_FOUND, "你请求的资源不存在"),
    /**
     * 系统异常
     **/
    ERROR(HttpStatus.ERROR, "系统异常"),
    /**
     * 操作失败
     **/
    FAIL(50001, "操作失败"),
    /**
     * 请求参数解析异常
     **/
    PARAMETER_PARSE_EXCEPTION(50002, "请求参数解析异常"),
    /**
     * HTTP内容类型异常
     **/
    HTTP_MEDIA_TYPE_EXCEPTION(50003, "HTTP内容类型异常"),
    /**
     * 系统处理异常
     **/
    YSHOP_SYSTEM_EXCEPTION(50004, "系统处理异常"),
    /**
     * 业务处理异常
     **/
    BUSINESS_EXCEPTION(50005, "业务处理异常"),
    /**
     * 数据库处理异常
     **/
    DAO_EXCEPTION(50006, "数据库处理异常"),
    /**
     * 没有访问权限
     **/
    UNAUTHENTICATED_EXCEPTION(50007, "未认证"),
    /**
     * 没有访问权限
     **/
    UNAUTHORIZED_EXCEPTION(50008, "没有访问权限"),
    /**
     * JWT Token解析异常
     **/
    JWTDECODE_EXCEPTION(50009, "Token解析异常"),

    REQUEST_METHOD_NOT_SUPPORTED(50010, "METHOD NOT SUPPORTED"),
    /**
     * 访问次数受限制
     **/
    BAD_LIMIT_EXCEPTION(50011, "访问次数受限制"),
    /**
     * 请求参数校验异常
     **/
    PARAMETER_EXCEPTION(50012, "请求参数校验异常"),
    /**
     * 处理中
     **/
    PROCESSING(50013, "正在处理中"),
    TENANT_MISS(50014, "miss the tenant info"),
    NOT_LOGGED_IN(50015, "not logged in"),
    EXISTS(50016, "data exists"),
    NOT_EXISTS(50017, "data not exists"),
    MISSING_PARAM(50018, "参数缺失"),
    ;

    private final Integer code;
    private final String message;

    public static ApiCode getApiCode(int code) {
        ApiCode[] ecs = ApiCode.values();
        for (ApiCode ec : ecs) {
            if (ec.getCode() == code) {
                return ec;
            }
        }
        return SUCCESS;
    }
}
