package com.hust.movie_review.es_repository;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hust.movie_review.utils.CustomDateDeSerializer;
import com.hust.movie_review.utils.CustomDateSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.*;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties({"@timestamp", "@version"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleResponse {
    private String indexName;

    private String id;

    String rawUrl;

    String url;

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

    List<String> videoSource = new LinkedList<>();

    List<String> videoSources = new LinkedList<>();

    List<String> imageSources = new LinkedList<>();

    List<String> audioSource = new ArrayList<>();

    List<String> imageSource = new LinkedList<>();

    @JsonAlias(value = "tags")
    List<String> tag = new LinkedList<>();

    private int sentiment = 0;


    @JsonProperty(value = "source_id_level_1")
    private int sourceIdLevel1;


    private List<Integer> positiveOrganizations = new LinkedList<>();

    private List<Integer> neutralOrganizations = new LinkedList<>();

    private List<Integer> negativeOrganizations = new LinkedList<>();

    private Integer articleType;

    private String postId = null;

    private String commentId = null;

    private String replyId = null;

    private Long likeCount;

    private Long dislikeCount;

    private Long viewCount;

    private Long shareCount;

    private Long commentCount;

    private Long replyCount;

    private Long reachCount;


    private String authorId;

    private String wallId;


    private String authorNickname;

    private Integer authorYearOfBirth;

    private Integer authorGender;

    private String wallDisplayName;

    private Integer location;

    private List<Integer> matchedTopics = new LinkedList<>();

    private List<Integer> matchedOrganizations = new LinkedList<>();

    private int similarMaster;

    private String similarGroupId;

    private String masterPublishedTime;

    private List<String> newsTag;

    private List<String> youtubeTag;

    private List<String> hashtag;

    private List<String> youtubeCategory;

    private Integer videoDuration;


//    private OnlineTvMeta onlineTvMeta;

    private String mediaType;

    private Boolean isVietnamese;

    private Set<Long> projectIds;

    private Set<Long> categoryIds;

    private Boolean isUpdated = false;

    private Set<String> matchKeywords;
}
