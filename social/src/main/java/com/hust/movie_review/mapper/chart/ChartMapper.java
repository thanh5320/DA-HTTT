package com.hust.movie_review.mapper.chart;

import com.hust.movie_review.data.response.chart.ChartInfo;
import com.hust.movie_review.models.ChartSetting;
import com.hust.movie_review.models.DashboardSetting;
import com.hust.movie_review.models.Project;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChartMapper {
    public static ChartInfo mapToChartInfo(ChartSetting chartSetting){
        ChartInfo chartInfo = new ChartInfo()
                .setId(chartSetting.getId())
                .setName(chartSetting.getName())
                .setChartCategory(chartSetting.getChartCategory())
                .setChartType(chartSetting.getChartType())
                .setDashboardId(chartSetting.getDashboardSetting().getId())
                .setProjectIds(Optional.ofNullable(chartSetting.getProjects()).orElse(new ArrayList<>())
                        .stream()
                        .map(Project::getId).collect(Collectors.toList()));

        return chartInfo;
    }

    public static List<ChartInfo> mapToChartInfo(List<ChartSetting> chartSettings){
        return Optional.ofNullable(chartSettings).orElse(new ArrayList<>())
                .stream()
                .map(ChartMapper::mapToChartInfo)
                .collect(Collectors.toList());

    }
}
