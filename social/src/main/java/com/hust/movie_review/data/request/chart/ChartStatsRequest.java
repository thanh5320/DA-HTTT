package com.hust.movie_review.data.request.chart;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hust.movie_review.utils.CustomDateDeSerializer;
import com.hust.movie_review.utils.CustomDateSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class ChartStatsRequest {
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeSerializer.class)
    @NotNull(message = "date from is not null")
    private Date dateFrom;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeSerializer.class)
    @NotNull(message = "date to is not null")
    private Date dateTo;

//    @NotNull(message = "chartTypeId is not null")
//    private int chartTypeId;
    @NotNull(message = "chartCategoryId is not null")
    private int chartCategoryId;

    @NotEmpty(message = "projectIds is not empty")
    List<Integer> projectIds;
}
