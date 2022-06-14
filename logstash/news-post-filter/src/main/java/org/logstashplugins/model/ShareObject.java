package org.logstashplugins.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShareObject {
    String domain;

    String title;

    String description;
}

