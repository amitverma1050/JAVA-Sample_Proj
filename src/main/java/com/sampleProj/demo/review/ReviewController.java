package com.sampleProj.demo.review;

import com.sampleProj.demo.job.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("companies/{companyId}")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService, JobService jobService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<Review>> getAllReviews(@PathVariable Long companyId){
        return new ResponseEntity<>(reviewService.getReviews(companyId),  HttpStatus.OK);
    }

    @PostMapping("/reviews")
    public ResponseEntity<String> createReview(@RequestBody Review review, @PathVariable Long companyId){
        boolean isAdded = reviewService.addReview(companyId, review);
        if (isAdded) {
            return new ResponseEntity<>("Review added successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid Request", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> getReview(@PathVariable Long reviewId) {
        Review review = reviewService.getReview(reviewId);

        if(review != null) {
            return new ResponseEntity<>(review, HttpStatus.OK);
        }
        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable Long reviewId, @RequestBody Review review) {
        boolean isReviewUpdated = reviewService.updateReview(reviewId, review);
        if (isReviewUpdated) {
            return new ResponseEntity<>("Review updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Review not updated", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long companyId, @PathVariable Long reviewId) {
        boolean isReviewDeleted = reviewService.deleteReview(companyId, reviewId);
        if (isReviewDeleted) {
            return new ResponseEntity<>("Review deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Review not deleted", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/reviews/sorted-by-rating-desc")
    public ResponseEntity<List<Review>> getAllReviewsSortedByRatingDesc(@PathVariable Long companyId){
        return new ResponseEntity<>(reviewService.getReviewsSortedByRatingDesc(companyId),  HttpStatus.OK);
    }

    @GetMapping("/reviews/sorted-by-rating")
    public ResponseEntity<List<Review>> getAllReviewsSortedByRating(@PathVariable Long companyId){
        return new ResponseEntity<>(reviewService.getReviewsSortedByRating(companyId),  HttpStatus.OK);
    }
}
