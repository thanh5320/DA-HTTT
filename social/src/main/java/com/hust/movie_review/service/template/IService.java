package com.hust.movie_review.service.template;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IService<T> {
    List<T> listing();

    List<T> listing(int page, int pageSize, String sortBy, Boolean desc);

    List<T> listing(Pageable pageable);

    T detail(int id);

    boolean delete(int id);
}
