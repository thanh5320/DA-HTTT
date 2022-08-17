package com.hust.movie_review.service;

import com.hust.movie_review.data.request.chart.OrmRequest;
import com.hust.movie_review.data.response.chart.SentimentStatistic;
import com.hust.movie_review.data.response.chart.TimeStatistic;
import com.hust.movie_review.es_repository.IArticleRepository;
import com.hust.movie_review.service.template.IStatisticService;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticServiceImpl implements IStatisticService {

    @Autowired
    private IArticleRepository articleRepository;

    @Override
    public List<SentimentStatistic> statisticSentimentByDays(OrmRequest queryInput) {
        return articleRepository.statisticSentimentByDays(queryInput);
    }

    @Override
    public List<TimeStatistic> statisticSourceByDays(OrmRequest queryInput) {
        return articleRepository.statisticSourceByDays(queryInput);
    }
}
