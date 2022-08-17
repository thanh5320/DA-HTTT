package com.hust.movie_review.repositories;

import com.hust.movie_review.models.DashboardSetting;
import com.hust.movie_review.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DashboardSettingRepository extends JpaRepository<DashboardSetting, Integer> {
    Optional<List<DashboardSetting>> findDashboardSettingByUserId(int userId);
}
