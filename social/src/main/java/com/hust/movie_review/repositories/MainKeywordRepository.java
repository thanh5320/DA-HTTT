package com.hust.movie_review.repositories;

import com.hust.movie_review.models.MainKeyword;
import com.hust.movie_review.models.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainKeywordRepository extends JpaRepository<MainKeyword, Integer> {
}