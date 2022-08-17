package com.hust.movie_review.data.response.project;

import com.hust.movie_review.models.MainKeyword;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class RuleInfo {
    int id;
    List<KeywordInfo> mainKeywords;
    List<KeywordInfo> subKeywords;
    List<KeywordInfo> excludeKeywords;
}
