package org.logstashplugins.model.sentiment;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SentimentInfo {
    int sentiment;
    List<String> rules;
}
