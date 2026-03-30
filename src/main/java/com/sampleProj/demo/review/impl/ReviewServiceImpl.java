package com.sampleProj.demo.review.impl;
import com.sampleProj.demo.companies.Company;
import com.sampleProj.demo.companies.CompanyService;
import com.sampleProj.demo.review.Review;
import com.sampleProj.demo.review.ReviewRepository;
import com.sampleProj.demo.review.ReviewService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final CompanyService companyService;
    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, CompanyService companyService) {
        this.reviewRepository = reviewRepository;
        this.companyService = companyService;
    }

    @Override
    public List<Review> getReviews(Long companyId) {
        return reviewRepository.findByCompanyId(companyId);
    }

    @Override
    public boolean addReview(Long companyId, Review review) {
        Company company = companyService.findById(companyId);
        if(company != null){
            review.setCompany(company);
            reviewRepository.save(review);
            return true;
        }
        return false;
    }

    @Override
    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    @Override
    public boolean updateReview(Long reviewId, Review updatedReview) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if(review != null) {
            review.setTitle(updatedReview.getTitle());
            review.setDescription(updatedReview.getDescription());
            review.setRating(updatedReview.getRating());
            // Company is NOT updated - it remains the same
            reviewRepository.save(review);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteReview(Long companyId, Long reviewId) {
        Company company = companyService.findById(companyId);
        if(company != null && reviewRepository.existsById(reviewId)) {
            Review review = reviewRepository.findById(reviewId).orElse(null);
            company.getReviews().remove(review);
            review.setCompany(null);
            companyService.updateCompany(companyId, company);
            reviewRepository.deleteById(reviewId);
            return true;
        }
        return false;
    }

    @Override
    public List<Review> getReviewsSortedByRatingDesc(Long companyId) {
        return reviewRepository.findByCompanyIdOrderByRatingDesc(companyId);
    }

    @Override
    public List<Review> getReviewsSortedByRating(Long companyId) {

        return reviewRepository.findByCompanyIdOrderByRating(companyId);
    }
}
