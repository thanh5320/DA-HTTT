package com.hust.movie_review.data.request.project;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class CreateRuleRequest {
    private List<String> mainKeywords;
    private List<String> subKeywords;
    private List<String> excludeKeywords;
}
