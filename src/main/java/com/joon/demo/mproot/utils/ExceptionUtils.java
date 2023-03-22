package com.joon.demo.mproot.utils;


import com.joon.demo.mproot.exception.AbsException;
import com.joon.demo.mproot.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Function;

/**
 * 异常工具类
 *
 * @author Joon
 * @date 2023-03-17 09:08
 */
@Slf4j
public class ExceptionUtils {

    /**
     * 获取堆栈信息
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }

    /**
     * 捕获异常
     *
     * @param supplier 执行任务
     * @param <T>      返回数据类型
     * @return 返回数据
     */
    public static <T> T ignore(Supplier<T> supplier) {
        return ExceptionUtils.ignore(supplier, null);
    }


    /**
     * 捕获异常
     *
     * @param supplier        执行任务
     * @param <T>             返回数据类型
     * @param finallyRunnable 最终执行的函数
     * @return 返回数据
     */
    public static <T> T ignore(Supplier<T> supplier, Runnable finallyRunnable) {
        try {
            return supplier.execute();
        } catch (Exception e) {
            log.error("执行异常", e);
        } finally {
            ExceptionUtils.execRunnable(finallyRunnable);
        }
        return null;
    }

    /**
     * 捕获异常
     *
     * @param executor 执行任务
     */
    public static void ignore(Executor executor) {
        ExceptionUtils.ignore(executor, null);
    }

    /**
     * 捕获异常
     *
     * @param executor        执行任务
     * @param finallyRunnable 最终执行函数
     */
    public static void ignore(Executor executor, Runnable finallyRunnable) {
        try {
            executor.execute();
        } catch (Exception e) {
            log.error("执行异常", e);
        } finally {
            ExceptionUtils.execRunnable(finallyRunnable);
        }
    }

    /**
     * 捕获异常
     *
     * @param supplier 执行任务
     * @param <T>      返回数据类型
     * @return 返回数据
     */
    public static <T> T convert(Supplier<T> supplier) {
        return ExceptionUtils.convert(supplier, (Runnable) null);
    }

    /**
     * 捕获异常
     *
     * @param supplier        执行任务
     * @param <T>             返回数据类型
     * @param finallyRunnable 最终执行函数
     * @return 返回数据
     */
    public static <T> T convert(Supplier<T> supplier, Runnable finallyRunnable) {
        try {
            return supplier.execute();
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            ExceptionUtils.execRunnable(finallyRunnable);
        }
    }

    /**
     * 捕获异常
     *
     * @param supplier 执行任务
     * @param <T>      返回数据类型
     * @return 返回数据
     */
    public static <T> T convert(Supplier<T> supplier, String message) {
        return ExceptionUtils.convert(supplier, message, null);
    }

    /**
     * 捕获异常
     *
     * @param supplier        执行任务
     * @param <T>             返回数据类型
     * @param finallyRunnable 最终执行函数
     * @return 返回数据
     */
    public static <T> T convert(Supplier<T> supplier, String message, Runnable finallyRunnable) {
        try {
            return supplier.execute();
        } catch (Exception e) {
            throw new BusinessException(e, message);
        } finally {
            ExceptionUtils.execRunnable(finallyRunnable);
        }
    }

    /**
     * 捕获异常
     *
     * @param supplier 执行任务
     * @param <T>      返回数据类型
     * @return 返回数据
     */
    public static <T> T convert(Supplier<T> supplier, AbsException exception) {
        return ExceptionUtils.convert(supplier, exception, null);
    }

    /**
     * 捕获异常
     *
     * @param supplier        执行任务
     * @param <T>             返回数据类型
     * @param finallyRunnable 最终执行函数
     * @return 返回数据
     */
    public static <T> T convert(Supplier<T> supplier, AbsException exception, Runnable finallyRunnable) {
        try {
            return supplier.execute();
        } catch (Exception e) {
            throw new BusinessException(exception);
        } finally {
            ExceptionUtils.execRunnable(finallyRunnable);
        }
    }

    /**
     * 捕获异常
     *
     * @param executor 执行任务
     */
    public static void convert(Executor executor) {
        ExceptionUtils.convert(executor, (Runnable) null);
    }

    /**
     * 捕获异常
     *
     * @param executor        执行任务
     * @param finallyRunnable 最终执行函数
     */
    public static void convert(Executor executor, Runnable finallyRunnable) {
        try {
            executor.execute();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            ExceptionUtils.execRunnable(finallyRunnable);
        }
    }

    /**
     * 转换异常
     *
     * @param executor 执行任务
     */
    public static void convert(Executor executor, String message) {
        ExceptionUtils.convert(executor, message, null);
    }

    /**
     * 转换异常
     *
     * @param executor        执行任务
     * @param finallyRunnable 最终执行函数
     */
    public static void convert(Executor executor, String message, Runnable finallyRunnable) {
        try {
            executor.execute();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e, message);
        } finally {
            ExceptionUtils.execRunnable(finallyRunnable);
        }
    }

    /**
     * 转换异常
     *
     * @param executor  执行任务
     * @param exception 异常原因
     */
    public static void convert(Executor executor, AbsException exception) {
        ExceptionUtils.convert(executor, exception, null);
    }

    /**
     * 转换异常
     *
     * @param executor        执行任务
     * @param exception       异常原因
     * @param finallyRunnable 最终执行函数
     */
    public static void convert(Executor executor, AbsException exception, Runnable finallyRunnable) {
        try {
            executor.execute();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(exception);
        } finally {
            ExceptionUtils.execRunnable(finallyRunnable);
        }
    }

    /**
     * 执行Runnable方法
     *
     * @param runnable Runnable函数
     */
    public static void execRunnable(Runnable runnable) {
        if (runnable != null) {
            runnable.run();
        }
    }

    public static class ExceptionExecutor<T> {
        /** 异常执行的方法 */
        private Function<Exception, T> catchFunction;
        /** 最后执行的方法 */
        private Runnable finallyRunnable;

        public ExceptionExecutor<T> capture(Function<Exception, T> function) {
            this.catchFunction = function;
            return this;
        }

        public ExceptionExecutor<T> last(Runnable runnable) {
            this.finallyRunnable = runnable;
            return this;
        }

        public T exec(Supplier<T> supplier) {
            try {
                return supplier.execute();
            } catch (Exception e) {
                if (catchFunction != null) {
                    return catchFunction.apply(e);
                } else {
                    throw new RuntimeException(e);
                }
            } finally {
                ExceptionUtils.execRunnable(finallyRunnable);
            }
        }
    }

    /**
     * 一个返回值的函数式接口
     *
     * @param <T>
     */
    @FunctionalInterface
    public interface Supplier<T> {
        /**
         * 执行函数
         *
         * @return 获取一个结果
         * @throws Exception
         */
        T execute() throws Exception;
    }

    /**
     * 无返回值的函数式接口
     */
    @FunctionalInterface
    public interface Executor {
        /**
         * 执行函数
         *
         * @throws Exception
         */
        void execute() throws Exception;
    }
}
