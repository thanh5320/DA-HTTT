package com.hust.movie_review.data.request.news;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class ViewArticleRequest {
    @NotBlank(message = "articleId is not null")
    private String articleId;
    @NotBlank(message = "indexName is not null")
    private String indexName;
}
