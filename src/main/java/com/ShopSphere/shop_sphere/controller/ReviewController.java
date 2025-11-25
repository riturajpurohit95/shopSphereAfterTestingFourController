
package com.ShopSphere.shop_sphere.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;

import com.ShopSphere.shop_sphere.dto.ReviewDto;
import com.ShopSphere.shop_sphere.model.Review;
import com.ShopSphere.shop_sphere.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    private Review dtoToEntity(ReviewDto dto) {
        Review r = new Review();
        r.setReviewId(dto.getReviewId());
        r.setUserId(dto.getUserId());
        r.setProductId(dto.getProductId());
        r.setRating(dto.getRating());
        r.setReviewText(dto.getReviewText());
        r.setStatus(dto.getStatus());
        r.setCreatedAt(dto.getCreatedAt()); // Optional; DB can set CURRENT_TIMESTAMP
        return r;
    }

    private ReviewDto entityToDto(Review r) {
        ReviewDto dto = new ReviewDto();
        dto.setReviewId(r.getReviewId());
        dto.setUserId(r.getUserId());
        dto.setProductId(r.getProductId());
        dto.setRating(r.getRating());
        dto.setReviewText(r.getReviewText());
        dto.setStatus(r.getStatus());
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }

    @PostMapping
    public int saveReview(@RequestBody ReviewDto dto) {
        return reviewService.saveReview(dtoToEntity(dto));
    }

    @GetMapping("/product/{productId}")
    public List<ReviewDto> getReviewsByProduct(@PathVariable int productId) {
        return reviewService.getReviewsByProduct(productId)
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/user/{userId}")
    public List<ReviewDto> getReviewsByUser(@PathVariable int userId) {
        return reviewService.getReviewsByUser(userId)
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{reviewId}/status/{status}")
    public String updateStatus(@PathVariable int reviewId, @PathVariable String status) {
        int rows = reviewService.updateReviewStatus(reviewId, status);
        return rows > 0 ? "Review status updated" : "No changes";
    }
}
