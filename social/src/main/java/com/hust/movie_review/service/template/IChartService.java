package com.hust.movie_review.service.template;

import com.hust.movie_review.data.request.chart.ChartStatsRequest;
import com.hust.movie_review.data.request.chart.CreateChartSettingRequest;
import com.hust.movie_review.data.request.chart.UpdateChartSettingRequest;
import com.hust.movie_review.data.response.chart.ChartInfo;

public interface IChartService {
    public ChartInfo create(CreateChartSettingRequest request);
    public ChartInfo update(UpdateChartSettingRequest request);
    public String deleteById(int chartId, int userId);

    public Object stats(ChartStatsRequest request);
}
