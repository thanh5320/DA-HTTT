package com.hust.movie_review.service;

import com.hust.movie_review.config.exception.ApiException;
import com.hust.movie_review.data.request.dashboard.CreateDashboardRequest;
import com.hust.movie_review.data.request.dashboard.UpdateDashboardRequest;
import com.hust.movie_review.data.response.dashboard.DashboardInfo;
import com.hust.movie_review.models.DashboardSetting;
import com.hust.movie_review.models.Project;
import com.hust.movie_review.models.User;
import com.hust.movie_review.repositories.DashboardSettingRepository;
import com.hust.movie_review.repositories.UserRepository;
import com.hust.movie_review.service.template.IChartService;
import com.hust.movie_review.service.template.IDashboardService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.hust.movie_review.mapper.dashboard.DashboardMapper.mapToDashboardInfo;
import static com.hust.movie_review.mapper.dashboard.DashboardMapper.mapToDashboardInfos;

@Service
public class DashboardServiceImpl implements IDashboardService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DashboardSettingRepository dashboardSettingRepository;

    @Autowired
    private IChartService chartService;

    @SneakyThrows
    @Override
    public DashboardInfo create(CreateDashboardRequest request) {
        Optional<User> user = userRepository.findById(request.getUserId());
        if(user.isPresent()){
            DashboardSetting dashboardSetting = new DashboardSetting()
                    .setName(request.getName())
                    .setUser(user.get());
            DashboardSetting result = dashboardSettingRepository.save(dashboardSetting);
            return mapToDashboardInfo(result);
        }else throw new ApiException("userId not exist");
    }

    @SneakyThrows
    @Override
    public DashboardInfo update(UpdateDashboardRequest request) {
        Optional<DashboardSetting> dashboardSetting = dashboardSettingRepository.findById(request.getId());

        if(!dashboardSetting.isPresent() || dashboardSetting.get().getUser().getId()!=request.getUserId())
            throw new ApiException("Dashboard is not found!");

        DashboardSetting result = dashboardSettingRepository.save(dashboardSetting.get().setName(request.getName()));

        return mapToDashboardInfo(result);
    }

    @SneakyThrows
    @Override
    public DashboardInfo findById(int id, int userId) {
        Optional<DashboardSetting> dashboardSetting = dashboardSettingRepository.findById(id);
        if(!dashboardSetting.isPresent() || dashboardSetting.get().getUser().getId()!=userId)
            throw new ApiException("Dashboard is not found!");
        return mapToDashboardInfo(dashboardSetting.get());
    }

    @SneakyThrows
    @Override
    public List<DashboardInfo> getAll(int userId) {
        Optional<List<DashboardSetting>> dashboardSettings = dashboardSettingRepository.findDashboardSettingByUserId(userId);
        if(!dashboardSettings.isPresent())
            throw new ApiException("Dashboard is not found!");

        return mapToDashboardInfos(dashboardSettings.get());
    }


    @SneakyThrows
    @Override
    public String deleteById(int dashboardId, int userId) {
        Optional<DashboardSetting> dashboard = dashboardSettingRepository.findById(dashboardId);
        if(!dashboard.isPresent() || dashboard.get().getUser().getId()!=userId){
            throw new ApiException("This dashboard does not exist");
        }

        Optional.ofNullable(dashboard.get().getChartSettings()).orElse(new ArrayList<>())
                .forEach(chart -> {
                    chartService.deleteById(chart.getId(), userId);
                });

        dashboardSettingRepository.delete(dashboard.get());
        return "success";
    }
}
