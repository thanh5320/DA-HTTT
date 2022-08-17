package com.hust.movie_review.service.template;


import com.hust.movie_review.data.request.dashboard.CreateDashboardRequest;
import com.hust.movie_review.data.request.dashboard.UpdateDashboardRequest;
import com.hust.movie_review.data.request.project.CreateProjectRequest;
import com.hust.movie_review.data.request.project.UpdateProjectRequest;
import com.hust.movie_review.data.response.dashboard.DashboardInfo;
import com.hust.movie_review.data.response.project.DetailProject;
import com.hust.movie_review.data.response.project.SimpleProject;

import java.util.List;

public interface IDashboardService {
    DashboardInfo create(CreateDashboardRequest request);
    DashboardInfo update(UpdateDashboardRequest request);
    DashboardInfo findById(int id, int userId);

    List<DashboardInfo> getAll(int userId);

    String deleteById(int dashboardId, int userId);
}
