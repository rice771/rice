package com.rice.base.es.bean;

import lombok.Data;

import java.util.List;

/**
 * @Author: ln
 * @Date: 2019/3/1 16:53
 * @Description: 索引的Mapping配置
 */
@Data
public class FileMapping {
   /**
    * 不分词字段
    **/
    private List<String> keywordField;
    /**
     * 分词字段
     **/
    private List<String> textField;
    /**
     * 自动补全字段
     **/
    private List<String> completionField;

}
