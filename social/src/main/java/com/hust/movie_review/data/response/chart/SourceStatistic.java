package com.hust.movie_review.data.response.chart;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SourceStatistic {
    private int sourceId = -1;

    private long totalCount=0;

    private long positiveCount=0;

    private long neutralCount=0;

    private long negativeCount=0;

    private Object data;
}
