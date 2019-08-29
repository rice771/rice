package com.rice.base.es.bean;

import lombok.Data;

/**
 * @Author: ln
 * @Date: 2019/2/26 08:59
 * @Description:
 */
@Data
public class FileBean {
    //text支持分词搜索的字段有：name，author，content，filePath
    //keyword支持不分词搜索的字段有：name，author
    //suggest支持自动补全搜索的字段有：name，author
    /** 主键id */
    private String id;
    /** 文件名称 */
    private String name;
    /** 作者名称 */
    private String author;
    /** 文件内容 */
    private String content;
    /** 文件路径 */
    private String filePath;

    //不分词搜索
    public String getKeywordName() {
        return this.name;
    }
    public String getKeywordAuthor() {
        return this.author;
    }
    //自动补全
    public String getSuggestName() {
        return this.name;
    }
    public String getSuggestAuthor() {
        return this.author;
    }
    //复合搜索
    public String getMultiName() {
        return this.name;
    }
    public String getMultiAuthor() {
        return this.author;
    }
    public String getMultiContent() {
        return this.content;
    }

}
