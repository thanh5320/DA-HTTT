package com.hust.movie_review.es_repository;

import com.hust.movie_review.data.request.chart.OrmRequest;
import com.hust.movie_review.data.request.news.SearchMultiRuleOrRequest;
import com.hust.movie_review.data.request.news.ViewArticleRequest;
import com.hust.movie_review.data.response.chart.SentimentStatistic;
import com.hust.movie_review.data.response.chart.TimeStatistic;
import com.hust.movie_review.data.response.news.XSearchResponse;
import com.hust.movie_review.models.Article;

import java.util.List;

public interface IArticleRepository {
    XSearchResponse searchMultipleSampleRuleOr(SearchMultiRuleOrRequest request);
    List<SentimentStatistic> statisticSentimentByDays(OrmRequest queryInput);
    List<TimeStatistic> statisticSourceByDays(OrmRequest queryInput);

    String view(ViewArticleRequest request);
}
