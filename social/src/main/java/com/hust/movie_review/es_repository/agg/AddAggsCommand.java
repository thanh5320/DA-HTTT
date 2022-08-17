package com.hust.movie_review.es_repository.agg;


import com.hust.movie_review.common.Constant;
import com.hust.movie_review.data.request.chart.OrmRequest;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.LongBounds;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

public class AddAggsCommand {

    private static final Map<String, Command> AGGS;

    public static String convert(Date date) {
        Format formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String str = formatter.format(date);
        return str;
    }

    static {
        final Map<String, Command> players = new HashMap<>();
        players.put(Constant.AggregationType.STATISTIC_SENTIMENT_BY_DAYS, new Command() {

            @Override
            public SearchSourceBuilder addAggs(OrmRequest input, String type, SearchSourceBuilder builder) {
                builder.aggregation(
                        AggregationBuilders.dateHistogram("statistic_by_days")
                                .field("published_time")
                                .calendarInterval(DateHistogramInterval.DAY)
                                .extendedBounds(new LongBounds(convert(input.getDateFrom()), convert(input.getDateTo()))));
                return builder;
            }
        });

        players.put(Constant.AggregationType.STATISTIC_SOURCE_BY_DAYS, new Command() {

            @Override
            public SearchSourceBuilder addAggs(OrmRequest input, String type, SearchSourceBuilder builder) {
                List<BucketOrder> bucketOrderList = new ArrayList<>();
                bucketOrderList.add(BucketOrder.aggregation("_count", false));
                builder.aggregation(
                        AggregationBuilders.dateHistogram("statistic_by_days")
                                .field("published_time").calendarInterval(DateHistogramInterval.DAY)
                                .extendedBounds(new LongBounds(convert(input.getDateFrom()), convert(input.getDateTo())))
                                .subAggregation(AggregationBuilders.terms("statistic_source_by_day").field("source_id")
                                        .size(10).minDocCount(1).shardMinDocCount(0).showTermDocCountError(false).order(bucketOrderList)));
                return builder;
            }
        });
        AGGS = Collections.unmodifiableMap(players);
    }

    public static SearchSourceBuilder addAggs(OrmRequest input, String type, SearchSourceBuilder builder) {
        Command command = AGGS.get(type);
        return command.addAggs(input, type, builder);
    }
}
