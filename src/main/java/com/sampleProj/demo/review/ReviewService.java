package com.sampleProj.demo.review;

import org.springframework.stereotype.Service;

import java.util.List;


public interface ReviewService {
    List<Review> getReviews(Long companyId);
    boolean addReview(Long companyId, Review review);
    Review getReview(Long reviewId);
    boolean updateReview(Long reviewId, Review updatedReview);
    boolean deleteReview(Long companyId, Long reviewId);

    // Sorted reviews methods
    List<Review> getReviewsSortedByRatingDesc(Long companyId);  // Best ratings first
    List<Review> getReviewsSortedByRating(Long companyId);      // Worst ratings first
}
