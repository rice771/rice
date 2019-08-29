package com.rice.base.es.service.impl;

import com.alibaba.fastjson.JSON;
import com.rice.base.es.bean.FileMapping;
import com.rice.base.es.service.IIndexService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Description: 索引操作类
 * @Author: ln
 * @Date: 2019/8/21 17:35
 * @Version: 1.0
 **/
@Service
public class IndexServiceImpl implements IIndexService {

    RestHighLevelClient client;

    @Override
    public boolean createIndex(String index, FileMapping mapping) throws IOException {
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));
        CreateIndexRequest request = new CreateIndexRequest(index);

        //索引配置
        request.mapping("doc",
                "keywordName", "type=keyword", "keywordAuthor", "type=keyword",//关键字匹配
                "suggestName", "type=completion", "suggestAuthor", "type=completion",//自动补全搜索
                "fileBean", "type=nested");//把对象设置成嵌套对象，注意放对象的时候要以fileBean为键

        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        client.close();
        JSON.toJSONString(createIndexResponse);
//        TODO
        return false;
    }

    @Override
    public boolean delIndex(String index) throws IOException {
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));
        DeleteIndexRequest request = new DeleteIndexRequest(index);

        AcknowledgedResponse deleteIndexResponse = client.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(deleteIndexResponse));
        client.close();
        JSON.toJSONString(deleteIndexResponse.isAcknowledged());
        //        TODO
        return false;
    }


    @Override
    public String putMapping(String index) throws IOException {
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));
        PutMappingRequest request = new PutMappingRequest(index);
        request.type("doc");

        request.source("name", "type=text");
        request.source("author", "type=text");
        request.source("content", "type=text");
        request.source("filePath", "type=text");

        request.source("keywordName", "type=keyword");
        request.source("keywordAuthor", "type=keyword");

        request.source("suggestName", "type=completion");
        request.source("suggestAuthor", "type=completion");

        AcknowledgedResponse putMappingResponse = client.indices().putMapping(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(putMappingResponse));
        return JSON.toJSONString(putMappingResponse);
    }

    @Override
    public String getMapping(String index) throws IOException {
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));

        GetMappingsRequest request = new GetMappingsRequest();
        request.indices(index);
        request.types("doc");

        GetMappingsResponse getMappingResponse = client.indices().getMapping(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(getMappingResponse));
        return JSON.toJSONString(getMappingResponse);

    }
}
