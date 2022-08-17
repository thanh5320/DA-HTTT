package com.hust.movie_review.service.template;

import com.hust.movie_review.data.request.news.SearchMultiRuleOrRequest;
import com.hust.movie_review.data.request.news.ViewArticleRequest;
import com.hust.movie_review.data.response.news.XSearchResponse;
import com.hust.movie_review.es_repository.ArticleResponse;
import com.hust.movie_review.es_repository.IArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IArticleService {
    XSearchResponse search(SearchMultiRuleOrRequest request);
    String view(ViewArticleRequest request);
}
