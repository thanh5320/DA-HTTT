package com.hust.movie_review.data.request.dashboard;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class UpdateDashboardRequest {
    @NotNull(message = "id is not empty")
    int id;
    @NotBlank(message = "name is not empty")
    String name;
    int userId;
}