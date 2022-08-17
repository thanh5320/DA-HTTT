package com.hust.movie_review.es_repository.agg;

import com.hust.movie_review.data.request.chart.OrmRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public interface Command {
    SearchSourceBuilder addAggs(OrmRequest input, String type, SearchSourceBuilder builder);
}
