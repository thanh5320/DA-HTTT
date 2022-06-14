package org.logstashplugins.config.database;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Rule {
    List<String> mainKeywords;
    List<String> subKeywords;
    List<String> excludeKeywords;
}
