package com.universe.common.exception;


public class BusinessException extends RuntimeException {
    /**
     * 错误码
     */
    private final int code;

    /**
     * 构造方法
     * @param code 错误码
     * @param message 错误消息
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造方法（使用默认错误码）
     * @param message 错误消息
     */
    public BusinessException(String message) {
        this(500, message);
    }

    /**
     * 常用业务异常快捷方法
     */
    public static BusinessException notFound() {
        return new BusinessException(404, "资源不存在");
    }

    public static BusinessException invalidParam(String message) {
        return new BusinessException(400, message);
    }
}
