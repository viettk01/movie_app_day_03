package com.example.demo.servies.impl;

import com.example.demo.entity.Movie;
import com.example.demo.entity.Reviews;
import com.example.demo.entity.User;
import com.example.demo.model.request.UpsertReviewRequest;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.ReviewReponsitory;
import com.example.demo.repository.UserReponsitory;
import com.example.demo.servies.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    ReviewReponsitory reviewReponsitory;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    UserReponsitory userReponsitory;

    @Override
    public List<Reviews> findByMovieIdOrderByDateDesc(Integer movieId) {
        return reviewReponsitory.findByMovieIdOrderByCreateAtDesc(movieId);
    }


    //Validate thong tin: content, rating, ... su dung thu vien Validation
    @Override
    public Reviews createReview(UpsertReviewRequest request) {
        //TODO: fix userId. ve sau userId chinh la user dang login
        Integer userId = 4;
        User user = userReponsitory.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //kiem tra movie co ton tai khong
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        //Tao reviews
        Reviews review = Reviews.builder()
                .content(request.getContent())
                .rating(request.getRating())
                .createAt(LocalDate.now())
                .updateAt(LocalDate.now())
                .movie(movie)
                .user(user)
                .build();

        reviewReponsitory.save(review);

        return review;
    }

    @Override
    public Reviews updateReview(Integer id, UpsertReviewRequest request) {
        Reviews reviews = reviewReponsitory.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        //TODO: fix userId. ve sau userId chinh la user dang login
        Integer userId = 4;
        User user = userReponsitory.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //kiem tra movie co ton tai khong
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        //Kiem tra review co thuoc user hay khong
        if (!reviews.getUser().getId().equals(userId)) {
            throw new RuntimeException("User not authorized");
        }

        //Kiem tra review nay co thuoc Movie hay khong
        if (!reviews.getMovie().getId().equals(movie.getId())) {
            throw new RuntimeException("Movie not authorized");
        }

        //Cap nhat review
        reviews.setContent(request.getContent());
        reviews.setRating(request.getRating());
        reviews.setUpdateAt(LocalDate.now());

        reviewReponsitory.save(reviews);
        return reviews;
    }

    @Override
    public void deleteReview(Integer id) {
        Reviews reviews = reviewReponsitory.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        //TODO: fix userId. ve sau userId chinh la user dang login
        Integer userId = 4;
        User user = userReponsitory.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //Kiem tra review co thuoc user hay khong
        if (!reviews.getUser().getId().equals(userId)) {
            throw new RuntimeException("User not authorized");
        }

        reviewReponsitory.delete(reviews);
    }

    @Override
    public Reviews getReview(Integer id) {
        Reviews review = reviewReponsitory.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        //TODO: fix userId. ve sau userId chinh la user dang login
        Integer userId = 4;
        User user = userReponsitory.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //Kiem tra review co thuoc user hay khong
        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("User not authorized");
        }

        return review;
    }
}
