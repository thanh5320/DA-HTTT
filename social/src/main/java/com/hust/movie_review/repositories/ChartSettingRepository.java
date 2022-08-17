package com.hust.movie_review.repositories;

import com.hust.movie_review.models.ChartSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChartSettingRepository extends JpaRepository<ChartSetting, Integer> {
}
