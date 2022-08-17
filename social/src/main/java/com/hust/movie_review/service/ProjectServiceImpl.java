package com.hust.movie_review.service;

import com.hust.movie_review.config.exception.ApiException;
import com.hust.movie_review.data.request.project.CreateProjectRequest;
import com.hust.movie_review.data.request.project.CreateRuleRequest;
import com.hust.movie_review.data.request.project.UpdateProjectRequest;
import com.hust.movie_review.data.response.project.DetailProject;
import com.hust.movie_review.data.response.project.RuleInfo;
import com.hust.movie_review.data.response.project.SimpleProject;
import com.hust.movie_review.mapper.project.ProjectMapper;
import com.hust.movie_review.models.*;
import com.hust.movie_review.repositories.*;
import com.hust.movie_review.service.template.IProjectService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.hust.movie_review.mapper.project.ProjectMapper.*;

@Service
public class ProjectServiceImpl implements IProjectService {
    private final UserRepository userRepository;

    private final RuleRepository ruleRepository;

    private final ProjectRepository projectRepository;

    private final MainKeywordRepository mainKeywordRepository;

    private final SubKeywordRepository subKeywordRepository;

    private final ExcludeKeywordRepository excludeKeywordRepository;

    public ProjectServiceImpl(UserRepository userRepository,
                              RuleRepository ruleRepository,
                              ProjectRepository projectRepository,
                              MainKeywordRepository mainKeywordRepository,
                              SubKeywordRepository subKeywordRepository,
                              ExcludeKeywordRepository excludeKeywordRepository) {
        this.userRepository = userRepository;
        this.ruleRepository = ruleRepository;
        this.projectRepository = projectRepository;
        this.mainKeywordRepository = mainKeywordRepository;
        this.subKeywordRepository = subKeywordRepository;
        this.excludeKeywordRepository = excludeKeywordRepository;
    }

    @SneakyThrows
    @Override
    @Transactional
    public SimpleProject create(CreateProjectRequest request) {
        for (CreateRuleRequest rule : request.getRules()) {
            checkConditionRule(rule);
        }

        User user = userRepository.findById(request.getUserId()).get();
        Project project = new Project()
                .setName(request.getName())
                .setUser(user);

        Project projectResult = projectRepository.save(project);

        request.getRules().forEach(ruleRequest -> {
            saveRule(projectResult, ruleRequest);
        });
        return mapToSimpleProject(projectResult);
    }

    private void saveRule(Project projectResult, CreateRuleRequest ruleRequest) {
        Rule rule = new Rule().setProject(projectResult);
        Rule ruleResult = ruleRepository.save(rule);
        Optional.ofNullable(ruleRequest.getMainKeywords()).orElse(new ArrayList<>())
                .forEach(text -> {
                    MainKeyword mainKeyword = new MainKeyword()
                            .setValue(text)
                            .setRule(ruleResult);
                    mainKeywordRepository.save(mainKeyword);
                });

        Optional.ofNullable(ruleRequest.getSubKeywords()).orElse(new ArrayList<>())
                .forEach(text -> {
                    SubKeyword subKeyword = new SubKeyword()
                            .setValue(text)
                            .setRule(ruleResult);
                    subKeywordRepository.save(subKeyword);
                });

        Optional.ofNullable(ruleRequest.getExcludeKeywords()).orElse(new ArrayList<>())
                .forEach(text -> {
                    ExcludeKeyword excludeKeyword = new ExcludeKeyword()
                            .setValue(text)
                            .setRule(ruleResult);
                    excludeKeywordRepository.save(excludeKeyword);
                });
    }

    private void checkConditionRule(CreateRuleRequest rule) throws ApiException {
        if (ObjectUtils.isEmpty(rule.getMainKeywords())
                && ObjectUtils.isEmpty(rule.getSubKeywords())
                && ObjectUtils.isEmpty(rule.getExcludeKeywords()))
            throw new ApiException("mainKeyword, subKeyword, ExcludeKeyword are all empty");
    }

    @SneakyThrows
    @Override
    @Transactional
    public SimpleProject update(UpdateProjectRequest request) {
        Optional<Project> entity = projectRepository.findById(request.getId());
        if (!entity.isPresent() || entity.get().getUser().getId() != request.getUserId()) {
            throw new ApiException("Project is not exist");
        }

        for (CreateRuleRequest rule : request.getRules()) {
            checkConditionRule(rule);
        }

        Optional.ofNullable(entity.get().getRules()).orElse(new ArrayList<>())
                .forEach(rule -> {
                    deleteRule(rule);
                });

        projectRepository.save(entity.get().setName(request.getName()));

        request.getRules().forEach(ruleRequest -> {
            saveRule(entity.get(), ruleRequest);
        });
        return mapToSimpleProject(entity.get());
    }


    @SneakyThrows
    @Override
    public DetailProject findById(int id, int userId) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            if (project.get().getUser().getId() == userId) {
                return mapToDetailProject(project.get());
            }
        }
        throw new ApiException("This project does not exist");
    }


    @Override
    public List<DetailProject> getAll(int userId) {
        Optional<List<Project>> projectsByUserId = projectRepository.findProjectsByUserId(userId);
        if (projectsByUserId.isPresent()) return mapListToDetailProject(projectsByUserId.get());
        return null;
    }

    @SneakyThrows
    @Override
    public String deleteById(int projectId, int userId) {
        Optional<Project> project = projectRepository.findById(projectId);
        if (!project.isPresent() || project.get().getUser().getId() != userId) {
            throw new ApiException("This project does not exist");
        }

        Optional.ofNullable(project.get().getRules()).orElse(new ArrayList<>())
                .forEach(rule -> {
                    deleteRule(rule);
                });

        projectRepository.delete(project.get());
        return "success";
    }

    @Override
    public List<RuleInfo> mapProjectIdsToRuleInfos(List<Integer> projectIds) {
        Optional<List<Project>> projects = projectRepository.findAllById(projectIds);
        List<RuleInfo> results = new ArrayList<>();
        if (projects.isPresent()) {
            projects.get().stream()
                    .map(Project::getRules)
                    .map(ProjectMapper::mapListRuleToRuleInfo)
                    .forEach(results::addAll);
        }
        return results;
    }

    private void deleteRule(Rule rule) {
        Optional.ofNullable(rule.getMainKeywords()).orElse(new ArrayList<>())
                .forEach(keyword -> {
                    mainKeywordRepository.deleteById(keyword.getId());
                });
        Optional.ofNullable(rule.getSubKeywords()).orElse(new ArrayList<>())
                .forEach(keyword -> {
                    subKeywordRepository.deleteById(keyword.getId());
                });

        Optional.ofNullable(rule.getExcludeKeywords()).orElse(new ArrayList<>())
                .forEach(keyword -> {
                    excludeKeywordRepository.deleteById(keyword.getId());
                });

        ruleRepository.deleteById(rule.getId());
    }
}
