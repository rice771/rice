package com.rice.base.es.bean;

import lombok.Data;

/**
 * @Author: ln
 * @Date: 2019/2/26 08:59
 * @Description:
 */
@Data
public class FileBeanQuery {
    /** 文件名称 */
    private String name;
    /** 作者名称 */
    private String author;
    /** 文件内容 */
    private String content;
    /** 文件路径 */
    private String filePath;

}
