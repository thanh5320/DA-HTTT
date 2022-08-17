package com.hust.movie_review.controllers;

import com.hust.movie_review.data.request.news.SearchMultiRuleOrRequest;
import com.hust.movie_review.data.request.news.ViewArticleRequest;
import com.hust.movie_review.data.response.DfResponse;
import com.hust.movie_review.service.template.IArticleService;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/news")
public class NewsController {
    @Autowired
    private IArticleService articleService;

    @PostMapping(value = "/search")
    public ResponseEntity<?> search(@RequestBody SearchMultiRuleOrRequest request){
        return DfResponse.okEntity(articleService.search(request));
    }

    @PostMapping(value = "/view-article-html")
    public ResponseEntity<?> view(@RequestBody @Valid ViewArticleRequest request){
        return DfResponse.okEntity(articleService.view(request));
    }
}
