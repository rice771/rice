package com.rice.base.common;

/**
 * @Description: 返回码
 * @Author: ln
 * @Date: 2019/8/22 14:16
 **/
public enum ResultCodeEnum {
    /**
     * 请求成功
     **/
    OK(9200, "成功"),
    /**
     * 用户发出的请求有错误，如参数类型、格式等错误
     **/
    Failure(9400, "失败");

    private final int code;
    private String message;

    ResultCodeEnum(int state, String message) {
        this.code = state;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return message;
    }

}
