package com.example.demo.common;

public enum ResultCode {
    SUCCESS(200, "操作成功"),
    ERROR(500, "系统异常，请稍后重试"),
    PARAM_ERROR(400, "参数格式错误"),
    UNAUTHORIZED(401, "未登录或登录凭证过期"),
    FORBIDDEN(403, "无权限访问该资源"),
    DATA_NOT_FOUND(404, "请求的资源不存在"),

    // 登录注册专用
    USER_HAS_EXISTED(4001, "该用户名已被注册"),
    USER_NOT_EXIST(4002, "该用户不存在"),
    PASSWORD_ERROR(4003, "账号或密码错误");

    private final Integer code;
    private final String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}