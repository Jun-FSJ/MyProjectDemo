package com.example.myprojectdemo.mproot.exception;

import com.example.myprojectdemo.api.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 业务异常
 *
 * @author Joon
 * @date 2023-03-17 09:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException implements AbsException {
    /** 错误码 */
    private Integer code;
    /** 错误信息 */
    private String message;

    public BusinessException(AbsException e) {
        super(e.getMessage());
        this.code = e.getCode();
        this.message = e.getMessage();
    }

    public BusinessException(Exception e, String message) {
        super(message, e);
    }

    public BusinessException(String message) {
        super(message);
        this.code = HttpStatus.ERROR;
        this.message = message;
    }

    public BusinessException(Exception exception) {
        super(exception);
        this.code = HttpStatus.ERROR;
        this.message = exception.getMessage();
    }
}
