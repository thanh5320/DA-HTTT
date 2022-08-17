package com.hust.movie_review.service;

import com.hust.movie_review.common.Constant;
import com.hust.movie_review.config.exception.ApiException;
import com.hust.movie_review.data.request.chart.ChartStatsRequest;
import com.hust.movie_review.data.request.chart.CreateChartSettingRequest;
import com.hust.movie_review.data.request.chart.OrmRequest;
import com.hust.movie_review.data.request.chart.UpdateChartSettingRequest;
import com.hust.movie_review.data.response.chart.ChartInfo;
import com.hust.movie_review.models.*;
import com.hust.movie_review.repositories.*;
import com.hust.movie_review.service.template.IChartService;
import com.hust.movie_review.service.template.IProjectService;
import com.hust.movie_review.service.template.IStatisticService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.hust.movie_review.mapper.chart.ChartMapper.mapToChartInfo;

@Service
public class ChartServiceImpl implements IChartService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DashboardSettingRepository dashboardSettingRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ChartCategoryRepository chartCategoryRepository;

    @Autowired
    private ChartTypeRepository chartTypeRepository;

    @Autowired
    private ChartSettingRepository chartSettingRepository;

    @Autowired
    private IStatisticService statisticService;

    @Autowired
    private IProjectService projectService;

    @SneakyThrows
    @Override
    @Transactional
    public ChartInfo create(CreateChartSettingRequest request) {
        Optional<DashboardSetting> dashboard = dashboardSettingRepository.findById(request.getDashboardId());
        if(!dashboard.isPresent() || dashboard.get().getUser().getId() != request.getUserId())
            throw new ApiException("DashboardId it not exist!");
        Optional<ChartCategory> chartCategory = chartCategoryRepository.findById(request.getChartCategoryId());
        if(!chartCategory.isPresent())
            throw new ApiException("chartCategoryId is not exist");
        Optional<ChartType> chartType = chartTypeRepository.findById(request.getChartTypeId());
        if(!chartType.isPresent())
            throw new ApiException("chartTypeId is not exist");

        Optional<List<Project>> projects = projectRepository.findAllById(request.getProjectIds());
        if (!projects.isPresent() || ObjectUtils.isEmpty(projects.get()))
            throw new ApiException("ProjectIds is not exist");

        ChartSetting chartSetting = new ChartSetting()
                .setName(request.getName())
                .setDashboardSetting(dashboard.get())
                .setChartCategory(chartCategory.get())
                .setChartType(chartType.get())
                .setProjects(projects.get());

        ChartSetting result = chartSettingRepository.save(chartSetting);

        return mapToChartInfo(result);
    }

    @SneakyThrows
    @Override
    public ChartInfo update(UpdateChartSettingRequest request) {
        Optional<ChartSetting> chartSetting = chartSettingRepository.findById(request.getId());
        if(!chartSetting.isPresent()|| chartSetting.get().getDashboardSetting().getUser().getId()!=request.getUserId())
            throw new ApiException("Chart is not exist");

        Optional<ChartCategory> chartCategory = chartCategoryRepository.findById(request.getChartCategoryId());
        if(!chartCategory.isPresent())
            throw new ApiException("chartCategoryId is not exist");
        Optional<ChartType> chartType = chartTypeRepository.findById(request.getChartTypeId());
        if(!chartType.isPresent())
            throw new ApiException("chartTypeId is not exist");

        Optional<List<Project>> projects = projectRepository.findAllById(request.getProjectIds());
        if (!projects.isPresent() || ObjectUtils.isEmpty(projects.get()))
            throw new ApiException("ProjectIds is not exist");

        chartSetting.get()
                .setName(request.getName())
                .setChartCategory(chartCategory.get())
                .setChartType(chartType.get())
                .setProjects(projects.get());

        ChartSetting result = chartSettingRepository.save(chartSetting.get());

        return mapToChartInfo(result);
    }

    @SneakyThrows
    @Override
    public String deleteById(int chartId, int userId) {
        Optional<ChartSetting> chart = chartSettingRepository.findById(chartId);
        if(!chart.isPresent() || chart.get().getDashboardSetting().getUser().getId() != userId)
            throw new ApiException("Chart is not exist");
        chartSettingRepository.deleteById(chartId);
        return "success";
    }

    @SneakyThrows
    @Override
    public Object stats(ChartStatsRequest request) {
        switch (request.getChartCategoryId()) {
            case Constant.ChartCategory.SENTIMENT:
            case Constant.ChartCategory.DISTRIBUTION_OF_SENTIMENT:
                return statisticService.statisticSentimentByDays(mapChartStatsRequestToOrmRequest(request));

            case Constant.ChartCategory.NUMBER_POSTS:
                return statisticService.statisticSourceByDays(mapChartStatsRequestToOrmRequest(request));

//            case Constants.ChartCategory.TOP_DOMAIN_PAGE_GROUP:
//                Map<String, Object> resultTopDomainPageGroup = new HashMap<>();crawler
//                request.getPlatforms().forEach(platform -> getTopDomainPageGroup(ormRequest, resultTopDomainPageGroup, platform));
//                setDefaultTopDomainPageGroup(resultTopDomainPageGroup);
//                return resultTopDomainPageGroup;
            default:
                throw new ApiException("`chart_category` unsupported");
        }
    }

    public OrmRequest mapChartStatsRequestToOrmRequest(ChartStatsRequest request){
      OrmRequest ormRequest = new OrmRequest()
              .setDateFrom(request.getDateFrom())
              .setDateTo(request.getDateTo())
              .setRuleInfos(projectService.mapProjectIdsToRuleInfos(request.getProjectIds()));
      return ormRequest;
    }
}
