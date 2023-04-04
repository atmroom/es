package com.atguigu.es;

import com.alibaba.fastjson2.JSON;
import com.atguigu.es.mapper.HotelMapper;
import com.atguigu.es.pojo.Hotel;
import com.atguigu.es.pojo.HotelDoc;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@SpringBootTest
class EsApplicationTests {
    @Autowired
    private RestHighLevelClient client;
    @Resource
    private HotelMapper hotelMapper;

    @Test
    void test() throws IOException {
        GetRequest request = new GetRequest("hotel").id("433576");
        GetResponse documentFields = client.get(request, RequestOptions.DEFAULT);
        String json = documentFields.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
        System.out.println(hotelDoc);
//        System.out.println(json);
    }

    @Test
    void testMybatis() {
        List<Hotel> list = hotelMapper.list();
        System.out.println(list);
    }

    @Test
    void testMatchAll() throws IOException {

        SearchRequest request = new SearchRequest("hotel");
        request.source().query(QueryBuilders.matchAllQuery());
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        SearchHits searchHits = search.getHits();
        long total = searchHits.getTotalHits().value;
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
            System.out.println(hotelDoc);
        }
    }

    @Test
    void testMatch() throws IOException {

        SearchRequest request = new SearchRequest("hotel");
        request.source().query(QueryBuilders.matchQuery("all", "如家"));
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        SearchHits searchHits = search.getHits();
        long total = searchHits.getTotalHits().value;
        System.out.println(total);
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
            System.out.println(hotelDoc);
        }
    }

    @Test
    void testTerm() throws IOException {

        SearchRequest request = new SearchRequest("hotel");
        request.source().query(QueryBuilders.termQuery("city", "上海"));
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        SearchHits searchHits = search.getHits();
        long total = searchHits.getTotalHits().value;
        System.out.println(total);
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
            System.out.println(hotelDoc);
        }
    }

    @Test
    void testRange() throws IOException {

        SearchRequest request = new SearchRequest("hotel");
        request.source().query(QueryBuilders.rangeQuery("price").gt(100).lt(400));
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        SearchHits searchHits = search.getHits();
        long total = searchHits.getTotalHits().value;
        System.out.println(total);
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
            System.out.println(hotelDoc);
        }
    }

    @Test
    void testBool() throws IOException {

        SearchRequest request = new SearchRequest("hotel");
        request.source().query(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("all", "如家")).filter(QueryBuilders.rangeQuery("price").gt(200)));
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        SearchHits searchHits = search.getHits();
        long total = searchHits.getTotalHits().value;
        System.out.println(total);
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
            System.out.println(hotelDoc);
        }
    }

    @Test
    void testMatchSort() throws IOException {

        SearchRequest request = new SearchRequest("hotel");
        request.source().query(QueryBuilders.matchAllQuery()).sort("price", SortOrder.ASC).from(10).size(20);
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        SearchHits searchHits = search.getHits();
        long total = searchHits.getTotalHits().value;
        System.out.println(total);
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
            System.out.println(hotelDoc);
        }
    }

    @Test
    void testHighLight() throws IOException {

        SearchRequest request = new SearchRequest("hotel");
        request.source().query(QueryBuilders.matchQuery("all", "如家")).highlighter(new HighlightBuilder().field("name").requireFieldMatch(false));
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        SearchHits searchHits = search.getHits();
        long total = searchHits.getTotalHits().value;
        System.out.println(total);
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            HighlightField name = hit.getHighlightFields().get("name");
            String string = name.getFragments()[0].string();
            HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
            hotelDoc.setName(string);
            System.out.println(hotelDoc);
        }
    }

    @Test
    void testBulkRequest() throws IOException {
        List<Hotel> list = hotelMapper.list();
        BulkRequest request = new BulkRequest();
        list.stream()
                .map(HotelDoc::new)
                .forEach(hotel -> request
                        .add(new IndexRequest("hotel").id(hotel.getId().toString()).source(JSON.toJSONString(hotel), XContentType.JSON)));
        client.bulk(request, RequestOptions.DEFAULT);
    }

}
