package com.hust.movie_review.data.request.role;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
public class UpdateRequest {
    @NotNull(message = "ID không được bỏ trống")
    int id;

    @Size(max = 255, message = "Tên hiển thị vai trò không được quá 255 kí tự")
    @NotNull(message = "Tên hiển thị vai trò không được bỏ trống")
    String displayName;
}
