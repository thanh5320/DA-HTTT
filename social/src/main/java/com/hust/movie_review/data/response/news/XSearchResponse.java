package com.hust.movie_review.data.response.news;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

@Data
@Accessors(chain = true)
public class XSearchResponse {
    long total = 0;
    int count = 0;
    List hits = new LinkedList();
    private Object info;
}
