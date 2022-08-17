package com.hust.movie_review.repositories;

import com.hust.movie_review.models.ChartCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChartCategoryRepository extends JpaRepository<ChartCategory, Integer> {
}
