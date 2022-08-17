package com.hust.movie_review.data.response.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class KeywordInfo {
    int id;
    String value;
}
