package org.logstashplugins.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;
import org.logstashplugins.utils.jackson.DateDeSerializer;
import org.logstashplugins.utils.jackson.DateSerializer;

import java.util.Date;

@Data
@Accessors(chain = true)
public class OnlineTvComment {
    private String id;

    private String postId;

    @JsonAlias(value = "created_time")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeSerializer.class)
    private Date publishedTime;

    private String content;

    private int likeCount;

    private int replyCount;

    private String username;

    private String userAvatar;
}
