package com.hust.movie_review.mapper.dashboard;

import com.hust.movie_review.data.response.dashboard.DashboardInfo;
import com.hust.movie_review.models.DashboardSetting;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hust.movie_review.mapper.chart.ChartMapper.mapToChartInfo;

public class DashboardMapper {
    public static DashboardInfo mapToDashboardInfo(DashboardSetting dashboardSetting){
        DashboardInfo dashboardInfo = new DashboardInfo()
                .setId(dashboardSetting.getId())
                .setName(dashboardSetting.getName())
                .setCharts(mapToChartInfo(dashboardSetting.getChartSettings()));

        return dashboardInfo;
    }

    public static List<DashboardInfo> mapToDashboardInfos(List<DashboardSetting> dashboardSettings){
        return Optional.ofNullable(dashboardSettings).orElse(new ArrayList<>())
                .stream()
                .map(DashboardMapper::mapToDashboardInfo)
                .collect(Collectors.toList());
    }
}
