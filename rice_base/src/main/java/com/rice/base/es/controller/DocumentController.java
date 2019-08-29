package com.rice.base.es.controller;

import com.rice.base.es.bean.FileBean;
import com.rice.base.es.bean.FileBeanQuery;
import com.rice.base.es.service.IDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * @Description: 文档
 * @Author: ln
 * @Date: 2019/8/21 15:24
 * @Version: 1.0
 **/
@Controller
@RequestMapping("/es/document")
public class DocumentController {

    @Autowired
    private IDocumentService documentService;


    /**
     * @Description: 新建文档(若索引不存在则新建)
     * @Author: ln
     * @Date: 2019/8/21 17:49
     * @Param [index, fileBean]
     * @Return java.lang.String
     **/
    @RequestMapping("/putDocument")
    @ResponseBody
    public String putDocument(String index, FileBean fileBean) throws IOException {
        String result = documentService.putDocument(index, fileBean);
        return result;
    }
    /**
     * @Description: 删除文档
     * @Author: ln
     * @Date: 2019/8/21 17:49
     * @Param [index, id]
     * @Return java.lang.String
     **/
    @RequestMapping("/delDocument")
    @ResponseBody
    public String delDocument(String index, String id) throws IOException {
        String result = documentService.delDocument(index, id);
        return result;
    }
    /**
     * @Description: 获取文档
     * @Author: ln
     * @Date: 2019/8/21 17:49
     * @Param [index, fileBean]
     * @Return java.lang.String
     **/
    @RequestMapping("/getDocument")
    @ResponseBody
    public String getDocument(String index, String id) throws IOException {
        String result = documentService.getDocument(index, id);
        return result;
    }
    /**
     * @Description: 全局搜索（不分词）
     * @Author: ln
     * @Date: 2019/8/21 17:49
     * @Param [index, fileBean]
     * @Return java.lang.String
     **/
    @RequestMapping("/keywordSearch")
    @ResponseBody
    public String keywordSearch(String index, String value,
                                int current, int size) throws IOException {
        String result = documentService.keywordSearch(index, value, current, size);
        return result;
    }
    /**
     * @Description: 复合搜索
     * @Author: ln
     * @Date: 2019/8/21 17:49
     * @Param [index, fileBean]
     * @Return java.lang.String
     **/
    @RequestMapping("/multiSearch")
    @ResponseBody
    public String multiSearch(String index, FileBeanQuery query,
                              int current, int size) throws IOException, IllegalAccessException {
        String result = documentService.multiSearch(index, query, current, size);
        return result;
    }
    /**
     * @Description: 高亮搜索（注意QueryBuilders的查询方法）
     * @Author: ln
     * @Date: 2019/8/21 17:49
     * @Param [index, fileBean]
     * @Return java.lang.String
     **/
    @RequestMapping("/highlightSearch")
    @ResponseBody
    public String highlightSearch(String index, String value, int current, int size) throws IOException {
        String result = documentService.highlightSearch(index, value, current, size);
        return result;
    }
    /* 词语补全(只能根据前缀补全) */
    /**
     * @Description: 新建文档(若索引不存在则新建)
     * @Author: ln
     * @Date: 2019/8/21 17:49
     * @Param [index, fileBean]
     * @Return java.lang.String
     **/
    @RequestMapping("/suggestSearch")
    @ResponseBody
    public String suggestSearch(String index, String value) throws IOException {
        String result = documentService.suggestSearch(index, value);
        return result;
    }
    /**
     * @Description: 词语补全(只能根据前缀补全，分页)
     * @Author: ln
     * @Date: 2019/8/21 17:49
     * @Param [index：索引名称, current：当前页, size：每页条数]
     * @Return java.lang.String
     **/
    @RequestMapping("/searchAll")
    @ResponseBody
    public String searchAll(String index, int current, int size) throws IOException {
        String result = documentService.searchAll(index, current, size);
        return result;
    }
    /**
     * @Description: 查询文档总数
     * @Author: ln
     * @Date: 2019/8/21 17:49
     * @Param [index：索引名称]
     * @Return java.lang.String
     **/
    @RequestMapping("/countQuery")
    @ResponseBody
    public String countQuery(String index) throws IOException {
        String result = documentService.countQuery(index);
        return result;
    }
    /**
     * @Description: 嵌套查询（分页）
     * @Author: ln
     * @Date: 2019/8/21 17:51
     * @Param [index：索引名称, query：查询条件, current：当前页, size：每页条数]
     * @Return java.lang.String
     **/
    @RequestMapping("/nestedQuery")
    @ResponseBody
    public String nestedQuery(String index, FileBeanQuery query,
                              int current, int size) throws IOException, IllegalAccessException {
        String result = documentService.nestedQuery(index, query, current, size);
        return result;
    }

}
