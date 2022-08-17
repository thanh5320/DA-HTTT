package com.hust.movie_review.data.response.chart;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hust.movie_review.utils.CustomDateDeSerializer;
import com.hust.movie_review.utils.CustomDateSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class SentimentStatistic {
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeSerializer.class)
    private Date date;

    private int topicId=0;

    private long totalCount=0;

    private long positiveCount=0;

    private long neutralCount=0;

    private long negativeCount=0;
}
