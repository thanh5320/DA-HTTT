package org.logstashplugins.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;
import org.logstashplugins.utils.jackson.DateDeSerializer;
import org.logstashplugins.utils.jackson.DateSerializer;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class OnlineTvMeta {
    private String originalTitle;

    private String vietnameseTitle;

    private List<String> director;

    private List<String> actor;

    private int minAge;

    private int avgDuration;

    private List<String> thumbnail;

    private List<String> movieTag;

    private String movieType;

    private String country;

    private int totalEpisode;

    private int currentEpisode;

    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeSerializer.class)
    private Date releaseTime;

    private List<OnlineTvComment> comments;
}
