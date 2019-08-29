package com.rice.base.es.service;

import com.rice.base.es.bean.FileMapping;

import java.io.IOException;

/**
 * @Description: TODO
 * @Author: ln
 * @Date: 2019/8/21 15:28
 * @Version: 1.0
 **/
public interface IIndexService {
    /**
     * @Description: 创建索引
     * @Author: ln
     * @Date: 2019/8/21 17:37
     * @Param [index：索引名称, mapping：索引配置(false)]
     * @Return boolean
     **/
    boolean createIndex(String index, FileMapping mapping) throws IOException;
    /**
     * @Description: 删除索引
     * 索引删除后下面的数据也会被删除
     * @Author: ln
     * @Date: 2019/8/21 17:38
     * @Param [index：索引名称]
     * @Return boolean
     **/
    boolean delIndex(String index) throws IOException;

    String putMapping(String index) throws IOException;

    String getMapping(String index) throws IOException;
}
