package com.hust.movie_review.service;

import com.hust.movie_review.data.request.news.SearchMultiRuleOrRequest;
import com.hust.movie_review.data.request.news.ViewArticleRequest;
import com.hust.movie_review.data.response.news.XSearchResponse;
import com.hust.movie_review.es_repository.ArticleResponse;
import com.hust.movie_review.es_repository.IArticleRepository;
import com.hust.movie_review.service.template.IArticleService;
import com.hust.movie_review.service.template.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements IArticleService {
    @Autowired
    private IArticleRepository articleRepository;

    @Autowired
    private IProjectService projectService;

    @Override
    public XSearchResponse search(SearchMultiRuleOrRequest request) {
        request.setRuleInfos(projectService.mapProjectIdsToRuleInfos(request.getProjectIds()));
        return articleRepository.searchMultipleSampleRuleOr(request);
    }

    @Override
    public String view(ViewArticleRequest request) {
        return articleRepository.view(request);
    }
}
