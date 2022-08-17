package com.hust.movie_review.repositories;

import com.hust.movie_review.models.ChartType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChartTypeRepository extends JpaRepository<ChartType, Integer> {
}
