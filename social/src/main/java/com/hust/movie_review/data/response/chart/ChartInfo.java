package com.hust.movie_review.data.response.chart;

import com.hust.movie_review.data.response.dashboard.DashboardInfo;
import com.hust.movie_review.data.response.project.SimpleProject;
import com.hust.movie_review.models.ChartCategory;
import com.hust.movie_review.models.ChartType;
import com.hust.movie_review.models.Project;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Accessors(chain = true)
public class ChartInfo {
    private int id;
    private String name;
    private ChartType chartType;
    private ChartCategory chartCategory;
    private int dashboardId;
    private List<Integer> projectIds;
}
