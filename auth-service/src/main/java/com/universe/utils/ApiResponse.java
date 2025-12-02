package com.universe.utils;

import lombok.Data;

@Data
public class ApiResponse<T> {

    /** 业务状态码：200=成功，其他=失败 */
    private Integer code;

    /** 提示信息 */
    private String message;

    /** 返回数据 */
    private T data;

    /** 是否成功（便于前端使用） */
    private boolean success;

    // ========== 构造器 ==========
    private ApiResponse(Integer code, String message, T data, boolean success) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
    }

    // ========== 成功返回 ==========

    /** 成功，无数据 */
    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(200, "success", null, true);
    }

    /** 成功，附带数据 */
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(200, "success", data, true);
    }

    /** 成功，自定义消息与数据 */
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(200, message, data, true);
    }

    // ========== 失败返回 ==========

    /** 失败，默认错误 */
    public static <T> ApiResponse<T> error() {
        return new ApiResponse<>(500, "error", null, false);
    }

    /** 失败，自定义消息 */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(500, message, null, false);
    }

    /** 失败，自定义状态码、消息 */
    public static <T> ApiResponse<T> error(Integer code, String message) {
        return new ApiResponse<>(code, message, null, false);
    }

    /** 失败，自定义状态码、消息、数据 */
    public static <T> ApiResponse<T> error(Integer code, String message, T data) {
        return new ApiResponse<>(code, message, data, false);
    }
}
