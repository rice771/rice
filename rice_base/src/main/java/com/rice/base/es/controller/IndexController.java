package com.rice.base.es.controller;

import com.rice.base.common.JsonResult;
import com.rice.base.es.bean.FileMapping;
import com.rice.base.es.service.IIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * @Description: 索引
 * @Author: ln
 * @Date: 2019/8/21 15:24
 * @Version: 1.0
 **/
@Controller
@RequestMapping("/es/index")
public class IndexController {

    @Autowired
    private IIndexService indexService;

    /**
     * @Description: 创建索引
     * @Author: ln
     * @Date: 2019/8/21 15:26
     * @Param [index：索引名称, mapping：索引配置]
     * @Return java.lang.String
     **/
    @RequestMapping("/create")
    @ResponseBody
    public JsonResult createIndex(String index, FileMapping mapping) throws IOException {
        boolean result = indexService.createIndex(index, mapping);
        return result ? JsonResult.success() : JsonResult.failure();
    }
    /**
     * @Description: 删除索引
     * Elasticsearch的版本要与client的版本一致,spring的start版本6.4.3调试失败，6.6.1调试成功
     * @Author: ln
     * @Date: 2019/8/21 15:29
     * @Param [index]
     * @Return java.lang.String
     **/
    @RequestMapping("/delete")
    @ResponseBody
    public JsonResult delIndex(String index) throws IOException {
        boolean result = indexService.delIndex(index);
        return result ? JsonResult.success() : JsonResult.failure();
    }

    @RequestMapping("/putMapping")
    @ResponseBody
    public String putMapping(String index) throws IOException {
        String result = indexService.putMapping(index);
        return result;
    }

    @RequestMapping("/getMapping")
    @ResponseBody
    public String getMapping(String index) throws IOException {
        String result = indexService.getMapping(index);
        return result;
    }

}
