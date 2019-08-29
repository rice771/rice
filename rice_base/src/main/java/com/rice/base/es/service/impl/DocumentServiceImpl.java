package com.rice.base.es.service.impl;

import com.alibaba.fastjson.JSON;
import com.rice.base.es.bean.FileBean;
import com.rice.base.es.bean.FileBeanQuery;
import com.rice.base.es.service.IDocumentService;
import com.rice.base.util.AttachmentReader;
import org.apache.http.HttpHost;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: ln
 * @Date: 2019/8/21 17:45
 * @Version: 1.0
 **/
@Service
public class DocumentServiceImpl implements IDocumentService {

    RestHighLevelClient client;




    @Override
    public String putDocument(String index, FileBean fileBean) throws IOException {
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));

        File file = new File(fileBean.getFilePath());
        String content = AttachmentReader.reader(fileBean.getFilePath());
        fileBean.setContent(content);
        fileBean.setName(file.getName());
        Map<String, Object> map = new HashMap<>();
        map.put("fileBean", fileBean);

        IndexRequest indexRequest = new IndexRequest(index, "doc", fileBean.getId());
        indexRequest.source(JSON.toJSONString(map), XContentType.JSON);

        IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(response));
        client.close();
        return JSON.toJSONString(response.status());
    }

    @Override
    public String delDocument(String index, String id) throws IOException {
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));

        DeleteRequest request = new DeleteRequest(index,"doc", id );
        DeleteResponse deleteResponse = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(deleteResponse));
        client.close();
        return JSON.toJSONString(deleteResponse.status());
    }

    @Override
    public String getDocument(String index, String id) throws IOException {
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));

        GetRequest getRequest = new GetRequest(index,"doc", id );
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(getResponse));
        client.close();
        return JSON.toJSONString(getResponse);
    }

    @Override
    public String keywordSearch(String index, String value,
                                int current, int size) throws IOException {
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //支持全词搜索的字段有：keywordName，keywordAuthor"
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(value, "keywordName", "keywordAuthor"));
        searchSourceBuilder.from(current);
        searchSourceBuilder.size(size);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(searchResponse));
        //处理返回结果
        List<Map<String, Object>> result = dealResult(searchResponse.getHits());
        client.close();
        return JSON.toJSONString(result);
    }

    @Override
    public String multiSearch(String index, FileBeanQuery query,
                              int current, int size) throws IOException, IllegalAccessException {
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));
        MultiSearchRequest request = new MultiSearchRequest();

        for (Field field : query.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if(field.get(query) != null){
                SearchRequest searchRequest = new SearchRequest(index);
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
                searchSourceBuilder.query(QueryBuilders.nestedQuery("fileBean", QueryBuilders.boolQuery().must(
                        QueryBuilders.matchQuery("fileBean." + field.getName(), field.get(query))), ScoreMode.None));
                searchRequest.source(searchSourceBuilder);
                request.add(searchRequest);
            }
        }

        MultiSearchResponse response = client.msearch(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(response));
        //返回结果处理
        List<Map<String, Object>> result = new ArrayList<>();
        MultiSearchResponse.Item[] multiSearchResponses = response.getResponses();
        for (MultiSearchResponse.Item multiSearchRespons : multiSearchResponses) {
            SearchHits hits = multiSearchRespons.getResponse().getHits();
            for (SearchHit hit : hits.getHits()) {
                Map<String, Object> map = hit.getSourceAsMap();
                if(!result.contains(map)){
                    result.add(map);
                }
            }
        }
        client.close();
        return JSON.toJSONString(result);
    }

    @Override
    public String highlightSearch(String index, String value, int current, int size) throws IOException {
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //高亮，支持所有FileBean实体的字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        FileBean fileBean = new FileBean();
        String[] fieldNames = new String[fileBean.getClass().getDeclaredFields().length];
        int i = 0;
        for (Field f : fileBean.getClass().getDeclaredFields()) {
            HighlightBuilder.Field highlight = new HighlightBuilder.Field(f.getName());
            highlight.highlighterType("unified");
            highlightBuilder.field(highlight);
            fieldNames[i] = f.getName();
            i++;
        }
        //设置高亮样式
        highlightBuilder.preTags("<label style=\"color: red\">");
        highlightBuilder.postTags("</label>");
        //添加查询条件
        searchSourceBuilder.highlighter(highlightBuilder);
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(value, fieldNames));//搜索也支持所有FileBean实体的字段
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(searchResponse));
        //获取高亮字段
        List<Map<String, Object>> result = new ArrayList<>();
        SearchHits hits = searchResponse.getHits();
        for (SearchHit hit : hits.getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            for (String fieldName : fieldNames) {
                HighlightField highlight = highlightFields.get(fieldName);
                System.out.println(fieldName);
                if(highlight != null){
                    Text[] fragments = highlight.fragments();
                    String fragmentString = fragments[0].string();
                    System.out.println("高亮值：" + fragmentString);
                    Map<String, Object> map = hit.getSourceAsMap();
                    map.put(fieldName, fragmentString);
                    if(!result.contains(map)){
                        result.add(map);
                    }
                }
            }
        }
        client.close();
        return JSON.toJSONString(result);
    }

    @Override
    public String suggestSearch(String index, String value) throws IOException {
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //查询补全词语
        SuggestionBuilder completionName = SuggestBuilders.completionSuggestion("suggestName").text(value);
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("suggestName", completionName);
        SuggestionBuilder completionAuthor = SuggestBuilders.completionSuggestion("suggestAuthor").text(value);
        suggestBuilder.addSuggestion("suggestAuthor", completionAuthor);

        searchSourceBuilder.suggest(suggestBuilder);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(searchResponse));
        //处理返回结果
        Suggest suggest = searchResponse.getSuggest();
        //支持自动补全搜索的字段有suggestName，suggestAuthor
        CompletionSuggestion termSuggestion = suggest.getSuggestion("suggestName");
        CompletionSuggestion termSuggestionAuthor = suggest.getSuggestion("suggestAuthor");
        List<CompletionSuggestion.Entry> list = termSuggestion.getEntries();
        list.addAll(termSuggestionAuthor.getEntries());
        List<String> suggestList = new ArrayList<>();
        for (CompletionSuggestion.Entry entry : list) {
            for (CompletionSuggestion.Entry.Option option : entry) {
                String suggestText = option.getText().string();
                System.out.println("补全的词语：" + suggestText);
                if(!suggestList.contains(suggestText)){
                    suggestList.add(suggestText);
                }
            }
        }
        client.close();
        return JSON.toJSONString(suggestList);
    }

    @Override
    public String searchAll(String index, int current, int size) throws IOException {
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));
        SearchRequest searchRequest = new SearchRequest(index);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.from(current);
        searchSourceBuilder.size(size);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(searchResponse));
        //处理返回结果
        SearchHits hits = searchResponse.getHits();
        client.close();
        return JSON.toJSONString(hits);
    }


    @Override
    public String countQuery(String index) throws IOException {
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));

        CountRequest countRequest = new CountRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        countRequest.source(searchSourceBuilder);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.termQuery("user", "kimchy"));
        countRequest.source(sourceBuilder);

        CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);
        long count = countResponse.getCount();
        return count + "";

    }

    @Override
    public String nestedQuery(String index, FileBeanQuery query, int current, int size) throws IOException {
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));
        MultiSearchRequest request = new MultiSearchRequest();

        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //高亮
        InnerHitBuilder innerHitBuilder = new InnerHitBuilder();
        innerHitBuilder.addDocValueField("author");
        innerHitBuilder.addDocValueField("content");

        searchSourceBuilder.query(QueryBuilders.nestedQuery("fileBean",
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("fileBean.author", query.getAuthor()))
                        .must(QueryBuilders.matchQuery("fileBean.content", query.getContent())),
                ScoreMode.None).innerHit(innerHitBuilder));
        searchRequest.source(searchSourceBuilder);
        request.add(searchRequest);

        MultiSearchResponse response = client.msearch(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(response));
        //返回结果处理
        List<Map<String, Object>> result = new ArrayList<>();
        MultiSearchResponse.Item[] multiSearchResponses = response.getResponses();
        for (MultiSearchResponse.Item multiSearchRespons : multiSearchResponses) {
            SearchHits hits = multiSearchRespons.getResponse().getHits();
            for (SearchHit hit : hits.getHits()) {
                Map<String, Object> map = hit.getSourceAsMap();
                if(!result.contains(map)){
                    result.add(map);
                }
            }
        }
        client.close();
        return JSON.toJSONString(result);
    }

    private List<Map<String, Object>> dealResult(SearchHits hits){
        List<Map<String, Object>> result = new ArrayList<>();
        for (SearchHit hit : hits.getHits()) {
            Map<String, Object> map = hit.getSourceAsMap();
            result.add(map);
        }
        return result;
    }

}
