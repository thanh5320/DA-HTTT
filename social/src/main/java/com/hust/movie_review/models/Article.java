package com.hust.movie_review.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hust.movie_review.utils.CustomDateDeSerializer;
import com.hust.movie_review.utils.CustomDateSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)
public class Article {
    private String id;

    String rawUrl;

    int sourceId;

    String domain;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeSerializer.class)
    Date publishedTime;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeSerializer.class)
    Date firstCrawledTime;

    String title;

    String summary;

    String content;

    String authorDisplayName;

    List<String> shareContent = new LinkedList<>();

    List<String> videoSources = new LinkedList<>();

    List<String> imageSources = new LinkedList<>();

    @JsonAlias(value = "tags")
    List<String> tag = new LinkedList<>();

    private int sentiment = 0;
}
