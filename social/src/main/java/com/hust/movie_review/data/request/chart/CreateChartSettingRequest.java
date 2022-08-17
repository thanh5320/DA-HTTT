package com.hust.movie_review.data.request.chart;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Accessors(chain = true)
public class CreateChartSettingRequest {
    @NotBlank(message = "name is not null")
    private String name;
    @NotNull(message = "chart_type_id is not null")
    private int chartTypeId;
    @NotNull(message = "chart_category_id is not null")
    private int chartCategoryId;
    @NotNull(message = "dashboard_id is not null")
    private int dashboardId;
    private int userId;
    @NotEmpty(message = "projectIds is not null")
    private List<Integer> projectIds;
}
