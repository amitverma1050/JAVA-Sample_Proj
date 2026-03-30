package com.sampleProj.demo.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT r FROM Review r WHERE r.company.id = :companyId ORDER BY r.rating ASC")
    List<Review> findByCompanyIdOrderByRating(@Param("companyId") Long companyId);
    
    @Query("SELECT r FROM Review r WHERE r.company.id = :companyId ORDER BY r.rating DESC")
    List<Review> findByCompanyIdOrderByRatingDesc(@Param("companyId") Long companyId);
}
