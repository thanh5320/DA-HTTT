package com.hust.movie_review.data.response.project;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class DetailProject {
    int id;
    String name;
    int userId;
    List<RuleInfo> rules;
}
