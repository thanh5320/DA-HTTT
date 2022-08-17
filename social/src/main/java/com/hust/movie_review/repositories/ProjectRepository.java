package com.hust.movie_review.repositories;

import com.hust.movie_review.models.Project;
import com.hust.movie_review.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    Optional<List<Project>> findProjectsByUserId(int userId);

    @Query("SELECT p FROM Project p WHERE p.id in ?1")
    Optional<List<Project>> findAllById(List<Integer> ids);
}
