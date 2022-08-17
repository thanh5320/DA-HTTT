package com.hust.movie_review.repositories;

import com.hust.movie_review.models.ExcludeKeyword;
import com.hust.movie_review.models.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExcludeKeywordRepository extends JpaRepository<ExcludeKeyword, Integer> {
}