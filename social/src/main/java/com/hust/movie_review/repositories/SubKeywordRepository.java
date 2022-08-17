package com.hust.movie_review.repositories;

import com.hust.movie_review.models.Rule;
import com.hust.movie_review.models.SubKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubKeywordRepository extends JpaRepository<SubKeyword, Integer> {
}
