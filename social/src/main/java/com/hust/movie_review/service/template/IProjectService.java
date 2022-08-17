package com.hust.movie_review.service.template;

import com.hust.movie_review.data.request.project.CreateProjectRequest;
import com.hust.movie_review.data.request.project.UpdateProjectRequest;
import com.hust.movie_review.data.response.project.DetailProject;
import com.hust.movie_review.data.response.project.RuleInfo;
import com.hust.movie_review.data.response.project.SimpleProject;
import com.hust.movie_review.models.Project;
import com.hust.movie_review.models.User;

import java.util.List;

public interface IProjectService {
    SimpleProject create(CreateProjectRequest request);
    SimpleProject update(UpdateProjectRequest request);
    DetailProject findById(int id, int userId);

    List<DetailProject> getAll(int userId);

    String deleteById(int projectId, int userId);

    List<RuleInfo> mapProjectIdsToRuleInfos(List<Integer> projectIds);
}
