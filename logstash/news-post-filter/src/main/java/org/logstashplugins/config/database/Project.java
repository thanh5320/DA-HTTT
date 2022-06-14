package org.logstashplugins.config.database;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class Project {
    Long id;
    List<Rule> rules = new ArrayList<>();
}
