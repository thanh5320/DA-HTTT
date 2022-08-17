package com.hust.movie_review.data.request.role;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
public class StoreRequest {
    @Size(max = 255, message = "Tên hiển thị không được quá 255 ký tự")
    @NotEmpty(message = "Tên hiển thị vai trò không được bỏ trống")
    String displayName;

    @Size(max = 255, message = "Tên vai trò không được quá 255 ký tự")
    @NotEmpty(message = "Tên vai trò không được bỏ trống")
    String name;
}
