package org.logstashplugins.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FbReaction {
    int like = 0;

    int love = 0;

    int wow = 0;

    int haha = 0;

    int sorry = 0;

    int anger = 0;

    int support = 0;
}
