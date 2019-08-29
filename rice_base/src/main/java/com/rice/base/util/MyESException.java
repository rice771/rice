package com.rice.base.util;

/**
 * @Author: ln
 * @Date: 2019/3/1 16:39
 * @Description: TODO：自定义异常类（想统一关闭Elasticsearch的client）
 */
public class MyESException extends Exception {
    MyESException(String s) {
        super(s);
    }

}
