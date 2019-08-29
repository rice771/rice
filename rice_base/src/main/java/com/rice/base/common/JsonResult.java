package com.rice.base.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 通用返回结果
 * @Author: ln
 * @Date: 2019/8/21 17:01
 **/
@Data
public class JsonResult implements Serializable {
    /**
     * 返回状态码
     */
    private int code;
    /**
     * 返回消息
     */
    private String msg;
    /**
     * 返回数据
     */
    private Object data;
    /**
     * @Description: 成功
     * @Author: ln
     * @Date: 2019/8/21 17:31
     * @Param []
     * @Return com.demo.elasticsearch.common.JsonResult
     **/
    public static JsonResult success() {
        return new JsonResult(ResultCodeEnum.OK, RiceConstants.JSON_BACK_SUCCESS);
    }
    /**
     * @Description: 成功，带数据
     * @Author: ln
     * @Date: 2019/8/21 17:32
     * @Param [data：返回数据]
     * @Return com.demo.elasticsearch.common.JsonResult
     **/
    public static JsonResult success(Object data) {
        return new JsonResult(ResultCodeEnum.OK, RiceConstants.JSON_BACK_SUCCESS, data);
    }
    /**
     * @Description: 成功，带提示信息和数据
     * @Author: ln
     * @Date: 2019/8/21 17:32
     * @Param [msg：提示信息, data：返回数据]
     * @Return com.demo.elasticsearch.common.JsonResult
     **/
    public static JsonResult success(String msg, Object data) {
        return new JsonResult(ResultCodeEnum.OK, msg, data);
    }
    /**
     * @Description: 失败
     * @Author: ln
     * @Date: 2019/8/21 17:33
     * @Param []
     * @Return com.demo.elasticsearch.common.JsonResult
     **/
    public static JsonResult failure() {
        return new JsonResult(ResultCodeEnum.Failure, RiceConstants.JSON_BACK_FAILURE);
    }
    /**
     * @Description: 失败，带提示信息
     * @Author: ln
     * @Date: 2019/8/21 17:33
     * @Param [msg：错误提示信息]
     * @Return com.demo.elasticsearch.common.JsonResult
     **/
    public static JsonResult failure(String msg) {
        return new JsonResult(ResultCodeEnum.Failure, msg);
    }
    /**
     * @Description: 失败，带提示信息和数据
     * @Author: ln
     * @Date: 2019/8/21 17:33
     * @Param [msg：错误提示信息, data：返回数据]
     * @Return com.demo.elasticsearch.common.JsonResult
     **/
    public static JsonResult failure(String msg, Object data) {
        return new JsonResult(ResultCodeEnum.Failure, msg, data);
    }
    /**
     * @Description: 失败。带自定义错误编码和提示信息
     * @Author: ln
     * @Date: 2019/8/21 17:34
     * @Param [code：带自定义错误编码, msg：错误提示信息]
     * @Return com.demo.elasticsearch.common.JsonResult
     **/
    public static JsonResult failure(int code, String msg) {
        return new JsonResult(code, msg);
    }
    /**
     * 构造函数
     */
    public JsonResult() {}

    public JsonResult(ResultCodeEnum code, String msg) {
        this.code = code.getCode();
        this.msg = msg;
        this.data = null;
    }

    public JsonResult(ResultCodeEnum code, String msg, Object data) {
        this.code = code.getCode();
        this.msg = msg;
        this.data = data;
    }

    public JsonResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

}
