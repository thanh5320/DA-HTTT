package com.hust.movie_review.data.request.dashboard;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class CreateDashboardRequest {
    @NotBlank(message = "name is not empty")
    String name;
    int userId;
}
