package com.universe.common;

import lombok.Data;

import java.io.Serializable;
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer code;
    private String message;
    private T data;
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
    // 无参构造器
    public Result() {}

    // 全参构造器
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 手动生成setter方法（解决编译错误的核心）
    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    // 工具方法（保持不变）
    public static <T> Result<T> success(T data) {
        Result<T> response = new Result<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(data);
        return response;
    }

    public static Result<?> ok(String message) {
        Result<?> r = new Result<>();
        r.setCode(200);
        r.setMessage(message);
        return r;
    }

    public static <T> Result<T> error(int code, String message) {
        Result<T> response = new Result<>();
        response.setCode(code);
        response.setMessage(message);
        response.setData(null); // 注意：原代码中response.data未初始化，这里改为null更合理
        return response;
    }
}