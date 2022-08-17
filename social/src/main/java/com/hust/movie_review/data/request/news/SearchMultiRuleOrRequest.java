package com.hust.movie_review.data.request.news;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hust.movie_review.data.response.project.RuleInfo;
import com.hust.movie_review.utils.CustomDateDeSerializer;
import com.hust.movie_review.utils.CustomDateSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
@Accessors(chain = true)
public class SearchMultiRuleOrRequest {

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeSerializer.class)
    @NotNull(message = "date from is not null")
    private Date dateFrom;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeSerializer.class)
    @NotNull(message = "date to is not null")
    private Date dateTo;

    private List<Integer> sentiments;

    private List<String> keywords;

    private int size = 15;

    private int page = 0;

    private int userId;

    private List<String> domains;

    private List<Integer> projectIds;

    private List<RuleInfo> ruleInfos = new LinkedList<>();
}
