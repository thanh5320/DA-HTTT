package com.hust.movie_review.service.template;

import com.hust.movie_review.data.request.chart.OrmRequest;
import com.hust.movie_review.data.response.chart.SentimentStatistic;
import com.hust.movie_review.data.response.chart.TimeStatistic;

import java.util.List;

public interface IStatisticService {
    List<SentimentStatistic> statisticSentimentByDays(OrmRequest queryInput);
    List<TimeStatistic> statisticSourceByDays(OrmRequest queryInput);
}
