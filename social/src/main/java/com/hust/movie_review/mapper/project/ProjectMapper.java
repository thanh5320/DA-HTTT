package com.hust.movie_review.mapper.project;

import com.hust.movie_review.data.response.project.DetailProject;
import com.hust.movie_review.data.response.project.KeywordInfo;
import com.hust.movie_review.data.response.project.RuleInfo;
import com.hust.movie_review.data.response.project.SimpleProject;
import com.hust.movie_review.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectMapper {
    public static SimpleProject mapToSimpleProject(Project project){
        return new SimpleProject()
                .setId(project.getId())
                .setName(project.getName())
                .setUserId(project.getUser().getId());
    }
    public static List<SimpleProject> mapListToSimpleProject(List<Project> projects){
        return Optional.ofNullable(projects).orElse(new ArrayList<>())
                .stream()
                .map(ProjectMapper::mapToSimpleProject)
                .collect(Collectors.toList());
    }

    public static DetailProject mapToDetailProject(Project project){
        return new DetailProject()
                .setId(project.getId())
                .setName(project.getName())
                .setUserId(project.getUser().getId())
                .setRules(mapListRuleToRuleInfo(project.getRules()));
    }

    public static List<DetailProject> mapListToDetailProject(List<Project> projects){
        return Optional.ofNullable(projects).orElse(new ArrayList<>())
                .stream()
                .map(ProjectMapper::mapToDetailProject)
                .collect(Collectors.toList());

    }

    public static List<RuleInfo> mapListRuleToRuleInfo(List<Rule> rules){
        return Optional.ofNullable(rules).orElse(new ArrayList<>())
                .stream()
                .map(ProjectMapper::mapRuleToRuleInfo)
                .collect(Collectors.toList());
    }

    public static RuleInfo mapRuleToRuleInfo(Rule rule){
        return new RuleInfo()
                .setId(rule.getId())
                .setMainKeywords(mapListMainKeywordToKeywordInfo(rule.getMainKeywords()))
                .setSubKeywords(mapListSubKeywordToKeywordInfo(rule.getSubKeywords()))
                .setExcludeKeywords(mapListExcludeKeywordToKeywordInfo(rule.getExcludeKeywords()));
    }

    public static KeywordInfo mapMainKeywordToKeywordInfo(MainKeyword mainKeyword){
        return new KeywordInfo()
                .setId(mainKeyword.getId())
                .setValue(mainKeyword.getValue());
    }

    public static KeywordInfo mapSubKeywordToKeywordInfo(SubKeyword subKeyword){
        return new KeywordInfo()
                .setId(subKeyword.getId())
                .setValue(subKeyword.getValue());
    }

    public static KeywordInfo mapExcludeKeywordToKeywordInfo(ExcludeKeyword excludeKeyword){
        return new KeywordInfo()
                .setId(excludeKeyword.getId())
                .setValue(excludeKeyword.getValue());
    }

    public static List<KeywordInfo> mapListMainKeywordToKeywordInfo(List<MainKeyword> mainKeywords){
        return Optional.ofNullable(mainKeywords).orElse(new ArrayList<>())
                .stream().map(ProjectMapper::mapMainKeywordToKeywordInfo)
                .collect(Collectors.toList());
    }
    public static List<KeywordInfo> mapListExcludeKeywordToKeywordInfo(List<ExcludeKeyword> excludeKeywords){
        return Optional.ofNullable(excludeKeywords).orElse(new ArrayList<>())
                .stream().map(ProjectMapper::mapExcludeKeywordToKeywordInfo)
                .collect(Collectors.toList());
    }
    public static List<KeywordInfo> mapListSubKeywordToKeywordInfo(List<SubKeyword> subKeywords){
        return Optional.ofNullable(subKeywords).orElse(new ArrayList<>())
                .stream().map(ProjectMapper::mapSubKeywordToKeywordInfo)
                .collect(Collectors.toList());
    }
}
