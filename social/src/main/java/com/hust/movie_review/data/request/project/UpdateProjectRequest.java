package com.hust.movie_review.data.request.project;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Accessors(chain = true)
public class UpdateProjectRequest {
    @NotNull(message = "id is not empty")
    private int id;
    @NotBlank(message = "name is not empty")
    private String name;
    private int userId;
    @NotEmpty(message = "project needs at least one rule")
    private List<CreateRuleRequest> rules;
}
