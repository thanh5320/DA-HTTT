package com.hust.movie_review.es_repository;



import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.hust.movie_review.common.Constant.SDF_FORMAT;
import static com.hust.movie_review.es_repository.EsTimeUtils.buildArticleIndices;
import static com.hust.movie_review.utils.TimeUtils.convertDateToString;

public class EsQueryUtils {
    @Autowired
    private RestHighLevelClient client;

    public static  <T> TermsQueryBuilder buildTermsQueryBuilder(String key, List<T> values) {
        return QueryBuilders.termsQuery(key, values);
    }

    public static <T> TermQueryBuilder buildTermQueryBuilder(String key, T value) {
        return QueryBuilders.termQuery(key, value);
    }

    public static <T> MatchPhraseQueryBuilder buildMatchPhraseQuery(String key, T value) {
        return QueryBuilders.matchPhraseQuery(key, value);
    }

    public static <T> List<MatchPhraseQueryBuilder> buildMatchPhraseQuery(String key, List<T> values) {
        return values.stream()
                .map(value -> QueryBuilders.matchPhraseQuery(key, value))
                .collect(Collectors.toList());
    }

    public static RangeQueryBuilder buildRangeQueryBuilder(String key, Date from, Date to) {
        RangeQueryBuilder builder = QueryBuilders.rangeQuery(key);
        builder.format(SDF_FORMAT);
        builder.from(convertDateToString(from));
        builder.to(convertDateToString(to));
        return builder;
    }

    public static RangeQueryBuilder buildRangeQueryBuilder(String key, String from, String to) {
        RangeQueryBuilder builder = QueryBuilders.rangeQuery(key);
        builder.format(SDF_FORMAT);
        builder.from(from);
        builder.to(to);
        return builder;
    }

    public static <T extends QueryBuilder> void addBoolFilter(BoolQueryBuilder boolQueryBuilder, T condition) {
        if (boolQueryBuilder == null) boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter().add(condition);
    }

    public static <T extends QueryBuilder> void addBoolFilter(BoolQueryBuilder boolQueryBuilder, List<T> conditions) {
        if (boolQueryBuilder == null) boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter().addAll(conditions);
    }

    public static <T extends QueryBuilder> void addBoolMust(BoolQueryBuilder boolQueryBuilder, List<T> conditions) {
        if (boolQueryBuilder == null) boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must().addAll(conditions);
    }

    public static <T extends QueryBuilder> void addBoolMustNot(BoolQueryBuilder boolQueryBuilder, List<T> conditions) {
        if (boolQueryBuilder == null) boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.mustNot().addAll(conditions);
    }

    public static <T extends QueryBuilder> void addBoolShould(BoolQueryBuilder boolQueryBuilder, List<T> conditions) {
        if (boolQueryBuilder == null) boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should().addAll(conditions);
    }

    public static <T extends QueryBuilder> void addBoolMust(BoolQueryBuilder boolQueryBuilder, T condition) {
        if (boolQueryBuilder == null) boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must().add(condition);
    }

    public static <T extends QueryBuilder> void addBoolMustNot(BoolQueryBuilder boolQueryBuilder, T condition) {
        if (boolQueryBuilder == null) boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.mustNot().add(condition);
    }

    public static <T extends QueryBuilder> void addBoolShould(BoolQueryBuilder boolQueryBuilder, T condition) {
        if (boolQueryBuilder == null) boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should().add(condition);
    }

    public static <T extends QueryBuilder> SearchRequest buildDefaultSearchRequest(String index, T mainQuery, int size, int page, Date from, Date to, int secondTimeout) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.from(size * page);
        builder.size(size);
        builder.timeout(TimeValue.timeValueSeconds(secondTimeout));
        builder.query(mainQuery);

        SearchRequest searchRequest = new SearchRequest().indices(buildArticleIndices(from, to, index));
        searchRequest.searchType(SearchType.DFS_QUERY_THEN_FETCH);
        searchRequest.source(builder);
        searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());
        return searchRequest;
    }

    public static <T> BoolQueryBuilder boolShouldMatchPhraseQuery(String key, List<T> values) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should().addAll(buildMatchPhraseQuery(key, values));
        return boolQueryBuilder;
    }

//    @SneakyThrows
//    public SearchResponse search(SearchRequest searchRequest){
//        return client.search(searchRequest, RequestOptions.DEFAULT);
//    }
}

