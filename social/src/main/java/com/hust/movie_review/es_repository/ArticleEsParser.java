package com.hust.movie_review.es_repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.hust.movie_review.data.request.news.SearchMultiRuleOrRequest;
import com.hust.movie_review.data.response.news.XSearchResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ArticleEsParser {
    private ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

    @SneakyThrows
    public XSearchResponse getSearchResponse(SearchMultiRuleOrRequest input, SearchHits searchHits) {
        long total = searchHits.getTotalHits().value;
        int count = searchHits.getHits().length;
        List ormArticles = getSearchResultList(input, searchHits.getHits());
        XSearchResponse xSearchResponse = new XSearchResponse()
                .setTotal(total)
                .setCount(count)
                .setHits(ormArticles);

        return xSearchResponse;
    }


    @SneakyThrows
    private List getSearchResultList(SearchMultiRuleOrRequest input, SearchHit[] searchHitArr) {
        List<ArticleResponse> articleResponses = new ArrayList<>();
        for (SearchHit searchHit : searchHitArr) {
            String source = searchHit.getSourceAsString();
            String index = searchHit.getIndex();
            ArticleResponse articleResponse = this.objectMapper.readValue(source, ArticleResponse.class);
            articleResponse.setIndexName(index);
            articleResponses.add(articleResponse);
        }
        return articleResponses;
    }
}

