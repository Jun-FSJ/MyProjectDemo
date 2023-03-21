package com.example.myprojectdemo.utils;

import com.example.myprojectdemo.api.ApiCode;
import com.example.myprojectdemo.api.HttpStatus;
import com.example.myprojectdemo.mproot.exception.AbsException;
import com.example.myprojectdemo.mproot.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 断言工具类
 *
 * @author Joon
 * @date 2023-03-17
 */
public class AssertUtils {

    /**
     * 判断对象是否为空
     *
     * @param value 判断数据
     */
    public static void isNull(Object... value) {
        isNull(ApiCode.PARAMETER_EXCEPTION, value);
    }

    /**
     * 判断对象是否为空
     *
     * @param value     判断数据
     * @param exception 异常信息
     */
    public static void isNull(Object value, String exception) {
        isNull(value, HttpStatus.ERROR, exception);
    }

    /**
     * 判断对象是否为空
     *
     * @param value   判断数据
     * @param message 异常信息
     */
    public static void isNull(Object value, Supplier<String> message) {
        if (value == null) {
            throw new BusinessException(message.get());
        }
    }

    /**
     * 判断对象是否为空
     *
     * @param value     判断数据
     * @param exception 异常信息
     */
    public static void isNull(Object value, AbsException exception) {
        isNull(value, exception.getCode(), exception.getMessage());
    }

    /**
     * 判断对象是否为空
     *
     * @param values    判断数据
     * @param exception 异常信息
     */
    public static void isNull(AbsException exception, Object... values) {
        for (Object value : values) {
            isNull(value, exception.getCode(), exception.getMessage());
        }
    }

    /**
     * 判断对象是否为空
     *
     * @param value     判断数据
     * @param exception 异常信息
     */
    public static void isNull(Object value, Integer code, String exception) {
        if (value == null) {
            throw new BusinessException(code, exception);
        }
    }

    /**
     * 判断对象是否为空
     *
     * @param values 判断数据
     */
    public static void isAnyNull(Object... values) {
        isAnyNull(ApiCode.PARAMETER_EXCEPTION, values);
    }

    /**
     * 判断对象是否为空
     *
     * @param values    判断数据
     * @param exception 异常信息
     */
    public static void isAnyNull(String exception, Object... values) {
        for (Object data : values) {
            isNull(data, exception);
        }
    }

    /**
     * 判断对象是否为空
     *
     * @param values  判断数据
     * @param message 异常信息
     */
    public static void isAnyNull(Supplier<String> message, Object... values) {
        for (Object data : values) {
            isNull(data, message);
        }
    }

    /**
     * 判断对象是否为空
     *
     * @param values    判断数据
     * @param exception 异常信息
     */
    public static void isAnyNull(AbsException exception, Object... values) {
        isAnyNull(exception.getCode(), exception.getMessage(), values);
    }

    /**
     * 判断对象是否为空
     *
     * @param values    判断数据
     * @param exception 异常信息
     */
    public static void isAnyNull(Integer code, String exception, Object... values) {
        for (Object data : values) {
            isNull(data, code, exception);
        }
    }

    /**
     * 判断对象是否为空
     *
     * @param value 判断数据
     */
    public static void isNoneNull(Object value) {
        isNoneNull(value, ApiCode.PARAMETER_EXCEPTION);
    }

    /**
     * 判断对象是否为空
     *
     * @param value     判断数据
     * @param exception 异常信息
     */
    public static void isNoneNull(Object value, String exception) {
        isNoneNull(value, HttpStatus.ERROR, exception);
    }

    /**
     * 判断对象是否为空
     *
     * @param value     判断数据
     * @param exception 异常信息
     */
    public static void isNoneNull(Object value, Supplier<String> exception) {
        if (value != null) {
            throw new BusinessException(HttpStatus.ERROR, exception.get());
        }
    }

    /**
     * 判断对象是否为空
     *
     * @param value     判断数据
     * @param exception 异常信息
     */
    public static void isNoneNull(Object value, AbsException exception) {
        isNoneNull(value, exception.getCode(), exception.getMessage());
    }

    /**
     * 判断对象是否为空
     *
     * @param data      判断数据
     * @param exception 异常信息
     */
    public static void isNoneNull(Object data, Integer code, String exception) {
        if (data != null) {
            throw new BusinessException(code, exception);
        }
    }

    /**
     * 判断集合是否为空
     *
     * @param data 判断数据
     */
    public static void isEmpty(Collection<?>... data) {
        isEmpty(ApiCode.PARAMETER_EXCEPTION, data);
    }

    /**
     * 判断集合是否为空
     *
     * @param data      判断数据
     * @param exception 异常信息
     */
    public static void isEmpty(Collection<?> data, String exception) {
        isEmpty(data, HttpStatus.ERROR, exception);
    }

    /**
     * 判断集合是否为空
     *
     * @param data      判断数据
     * @param exception 异常信息
     */
    public static void isEmpty(Collection<?> data, AbsException exception) {
        isEmpty(data, exception.getCode(), exception.getMessage());
    }

    /**
     * 判断集合是否为空
     *
     * @param data      判断数据
     * @param exception 异常信息
     */
    public static void isEmpty(Collection<?> data, Integer code, String exception) {
        if (CollectionUtils.isEmpty(data)) {
            throw new BusinessException(code, exception);
        }
    }

    /**
     * 判断集合是否为空
     *
     * @param data      判断数据
     * @param exception 异常信息
     */
    public static void isEmpty(Collection<?> data, Supplier<String> exception) {
        if (CollectionUtils.isEmpty(data)) {
            throw new BusinessException(exception.get());
        }
    }

    /**
     * 判断集合是否为空
     *
     * @param data      判断数据
     * @param exception 异常信息
     */
    public static void isEmpty(AbsException exception, Collection<?>... data) {
        for (Collection<?> collection : data) {
            isEmpty(collection, exception);
        }
    }

    /**
     * 判断集合是否为空
     *
     * @param data 判断数据
     */
    public static void isEmpty(Map data) {
        isEmpty(data, ApiCode.PARAMETER_EXCEPTION);
    }

    /**
     * 判断集合是否为空
     *
     * @param data      判断数据
     * @param exception 异常信息
     */
    public static void isEmpty(Map data, String exception) {
        isEmpty(data, HttpStatus.ERROR, exception);
    }

    /**
     * 判断集合是否为空
     *
     * @param data      判断数据
     * @param exception 异常信息
     */
    public static void isEmpty(Map data, Supplier<String> exception) {
        if (CollectionUtils.isEmpty(data)) {
            throw new BusinessException(exception.get());
        }
    }

    /**
     * 判断集合是否为空
     *
     * @param data      判断数据
     * @param exception 异常信息
     */
    public static void isEmpty(Map data, AbsException exception) {
        isEmpty(data, exception.getCode(), exception.getMessage());
    }

    /**
     * 判断集合是否为空
     *
     * @param data      判断数据
     * @param exception 异常信息
     */
    public static void isEmpty(Map data, Integer code, String exception) {
        if (CollectionUtils.isEmpty(data)) {
            throw new BusinessException(code, exception);
        }
    }

    /**
     * 判断对象是否为空
     *
     * @param data 判断数据
     */
    public static void isTrue(boolean... data) {
        for (boolean d : data) {
            isTrue(d, ApiCode.PARAMETER_EXCEPTION);
        }
    }

    /**
     * 判断对象是否为空
     *
     * @param data      判断数据
     * @param exception 异常信息
     */
    public static void isTrue(boolean data, String exception) {
        isTrue(data, HttpStatus.ERROR, exception);
    }

    /**
     * 判断对象是否为空
     *
     * @param data    判断数据
     * @param message 异常信息
     */
    public static void isTrue(boolean data, Supplier<String> message) {
        if (data) {
            throw new BusinessException(message.get());
        }
    }

    /**
     * 判断对象是否为空
     *
     * @param data      判断数据
     * @param exception 异常信息
     */
    public static void isTrue(boolean data, AbsException exception) {
        isTrue(data, exception.getCode(), exception.getMessage());
    }

    /**
     * 判断对象是否为空
     *
     * @param data      判断数据
     * @param exception 异常信息
     */
    public static void isTrue(boolean data, Integer code, String exception) {
        if (data) {
            throw new BusinessException(code, exception);
        }
    }

    /**
     * 判断对象是否为false
     *
     * @param data 判断数据
     */
    public static void isFalse(boolean data) {
        isFalse(data, ApiCode.PARAMETER_EXCEPTION);
    }

    /**
     * 判断对象是否为false
     *
     * @param data      判断数据
     * @param exception 异常信息
     */
    public static void isFalse(boolean data, String exception) {
        isFalse(data, HttpStatus.ERROR, exception);
    }

    /**
     * 判断对象是否为false
     *
     * @param data     判断数据
     * @param supplier 异常信息
     */
    public static void isFalse(boolean data, Supplier<String> supplier) {
        if (!data) {
            throw new BusinessException(supplier.get());
        }
    }

    /**
     * 判断对象是否为false
     *
     * @param data      判断数据
     * @param exception 异常信息
     */
    public static void isFalse(boolean data, AbsException exception) {
        isFalse(data, exception.getCode(), exception.getMessage());
    }

    /**
     * 判断对象是否为false
     *
     * @param data      判断数据
     * @param exception 异常信息
     */
    public static void isFalse(boolean data, Integer code, String exception) {
        if (!data) {
            throw new BusinessException(code, exception);
        }
    }

    /**
     * 判断字符串是否为空白字符串
     *
     * @param data 字符串数据
     */
    public static void isBlank(String... data) {
        for (String d : data) {
            isBlank(d, ApiCode.PARAMETER_EXCEPTION);
        }
    }

    /**
     * 判断字符串是否为空白字符串
     *
     * @param data      字符串数据
     * @param exception 异常信息
     */
    public static void isBlank(String data, String exception) {
        isBlank(data, HttpStatus.ERROR, exception);
    }

    /**
     * 判断字符串是否为空白字符串
     *
     * @param data     字符串数据
     * @param supplier 异常信息
     */
    public static void isBlank(String data, Supplier<String> supplier) {
        if (StringUtils.isBlank(data)) {
            throw new BusinessException(HttpStatus.ERROR, supplier.get());
        }
    }

    /**
     * 判断字符串是否为空白字符串
     *
     * @param data      字符串数据
     * @param exception 异常信息
     */
    public static void isBlank(String data, AbsException exception) {
        isBlank(data, exception.getCode(), exception.getMessage());
    }

    /**
     * 判断字符串是否为空白字符串
     *
     * @param data      字符串数据
     * @param exception 异常信息
     */
    public static void isBlank(String data, Integer code, String exception) {
        if (StringUtils.isBlank(data)) {
            throw new BusinessException(code, exception);
        }
    }

    /**
     * 判断字符串是否为空白字符串
     *
     * @param data 字符串数据
     */
    public static void isAnyBlank(String... data) {
        isAnyBlank(ApiCode.PARAMETER_EXCEPTION, data);
    }

    /**
     * 判断字符串是否为空白字符串
     *
     * @param data    字符串数据
     * @param message 异常信息
     */
    public static void isAnyBlank(Supplier<String> message, String... data) {
        if (StringUtils.isAnyBlank(data)) {
            throw new BusinessException(message.get());
        }
    }

    /**
     * 判断字符串是否为空白字符串
     *
     * @param data 字符串数据
     * @param e    异常信息
     */
    public static void isAnyBlank(AbsException e, String... data) {
        isAnyBlank(e.getCode(), e.getMessage(), data);
    }

    /**
     * 判断字符串是否为空白字符串
     *
     * @param data    字符串数据
     * @param code    异常编码
     * @param message 异常信息
     */
    public static void isAnyBlank(Integer code, String message, String... data) {
        if (StringUtils.isAnyBlank(data)) {
            throw new BusinessException(code, message);
        }
    }

    /**
     * 判断字符串是否为空白字符串
     *
     * @param data 字符串数据
     */
    public static void isAllBlank(String... data) {
        isAnyBlank(ApiCode.PARAMETER_EXCEPTION, data);
    }

    /**
     * 判断字符串是否为空白字符串
     *
     * @param data 字符串数据
     * @param e    异常信息
     */
    public static void isAllBlank(AbsException e, String... data) {
        if (StringUtils.isAllBlank(data)) {
            throw new BusinessException(e);
        }
    }

    /**
     * 判断数值是否为正数
     *
     * @param data 字符串数据
     */
    public static void nullOrNotPositiveNumber(Number data) {
        nullOrNotPositiveNumber(data, ApiCode.PARAMETER_EXCEPTION);
    }

    /**
     * 判断数值是否为正数
     *
     * @param data      字符串数据
     * @param exception 异常信息
     */
    public static void nullOrNotPositiveNumber(Number data, AbsException exception) {
        if (data == null || data.doubleValue() <= 0) {
            throw new BusinessException(exception);
        }
    }

    /**
     * 判断数值是否为非负数
     *
     * @param data 字符串数据
     */
    public static void nullOrNotNegativeNumber(Number data) {
        nullOrNotNegativeNumber(data, ApiCode.PARAMETER_EXCEPTION);
    }

    /**
     * 判断数值是否为非负数
     *
     * @param data      字符串数据
     * @param exception 异常信息
     */
    public static void nullOrNotNegativeNumber(Number data, AbsException exception) {
        if (data == null || data.doubleValue() < 0) {
            throw new BusinessException(exception);
        }
    }
}
