package org.logstashplugins.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;
import org.logstashplugins.utils.jackson.DateDeSerializer;
import org.logstashplugins.utils.jackson.DateSerializer;

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

    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeSerializer.class)
    Date publishedTime;

    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeSerializer.class)
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

    private Set<Long> projectIds;
}
