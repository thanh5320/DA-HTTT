package org.logstashplugins.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;
import org.logstashplugins.utils.jackson.DateDeSerializer;
import org.logstashplugins.utils.jackson.DateSerializer;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {
    private String id;

    private String url;

    private String domain;

    private int sourceId;

    private int sourceIdLevel1;

    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeSerializer.class)
    private Date firstCrawledTime = new Date();

    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeSerializer.class)
    private Date publishedTime = new Date();

    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeSerializer.class)
    private Date postPublishedTime = new Date();

    private String content;

    private int sentiment = 0;

    private String postId;

    private String commentId;

    @JsonProperty(value = "reply_id")
    private String replyId;

    private Long likeCount;

    private Long dislikeCount;

    private Long replyCount;

    private String authorId;

    private String authorDisplayName;

    @JsonProperty(value = "matched_topic")
    private List<Integer> matchedTopics = new LinkedList<>();

    @JsonProperty(value = "matched_organization")
    private List<Integer> matchedOrganizations = new LinkedList<>();

    @JsonProperty(value = "positive_organization")
    private List<Integer> positiveOrganizations = new LinkedList<>();

    @JsonProperty(value = "neutral_organization")
    private List<Integer> neutralOrganizations = new LinkedList<>();

    @JsonProperty(value = "negative_organization")
    private List<Integer> negativeOrganizations = new LinkedList<>();
}
