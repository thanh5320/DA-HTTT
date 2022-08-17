package com.hust.movie_review.data.response.dashboard;


import com.hust.movie_review.data.response.chart.ChartInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class DashboardInfo {
    int id;
    String name;
    List<ChartInfo> charts;
}
