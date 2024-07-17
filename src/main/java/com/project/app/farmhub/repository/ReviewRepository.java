package com.project.app.farmhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.project.app.farmhub.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, String>, JpaSpecificationExecutor<Review> {

}