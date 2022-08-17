package com.hust.movie_review.data.request.chart;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Accessors(chain = true)
public class UpdateChartSettingRequest {
    @NotNull(message = "chartId is not null")
    private int id;
    @NotBlank(message = "name is not null")
    private String name;
    @NotNull(message = "chartTypeId is not null")
    private int chartTypeId;
    @NotNull(message = "chartCategoryId is not null")
    private int chartCategoryId;
    @NotNull(message = "projectIds is not null")
    private List<Integer> projectIds;
    private int userId;
}
