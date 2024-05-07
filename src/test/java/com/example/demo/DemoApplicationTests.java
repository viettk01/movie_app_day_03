package com.example.demo;

import com.example.demo.entity.Movies;
import com.example.demo.entity.model.enums.MovieType;
import com.example.demo.repository.MovieRepository;
import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
private MovieRepository movieRepository;

    @Test
    void contextLoads() {
        Random random = new Random();
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();

        for (int i = 0; i < 100; i++) {
            String name = faker.book().title();
            Movies movies = Movies.builder()
                    .name(name)
                    .slug(slugify.slugify(name))
                    .description(faker.lorem().paragraph())
                    .poster("https://placehold.co/600x400/png" + String.valueOf(name.charAt(0)).toUpperCase())
                    .releaseYear(faker.number().numberBetween(2021, 2024))
                    .rating(faker.number().randomDouble(1,1,10))
                    .type(MovieType.values()[random.nextInt(MovieType.values().length)])
                    .status(faker.bool().bool())
                    .trailer("https://www.youtube.com/embed/EzFXDvC-EwM?si=jA8PGFzhKNPyM6Nc")
                    .createdAt(LocalDate.now())
                    .updateAt(LocalDate.now())
                    .build();
            movieRepository.save(movies);
        }
    }

    @Test
    void test_movie_query() {
        List<Movies> movies = movieRepository.findAll();
        System.out.println("Sum movie: " + movies.size());
        //select * from movies where id (1,2,3)
        List movieById = movieRepository.findAllById(List.of(1, 2, 3));
        System.out.println("Sum movie by list id: " + movieById.size());
        //select * from movies where id = 1
        Movies movie = movieRepository.findById(1).orElse(null);
        System.out.println("movie" + movie);
        // update
        movie.setName("Death");
        movieRepository.save(movie);
        //sort
        List<Movies> movieSort = movieRepository.findByType(MovieType.PHIM_BO, Sort.by("name", "rating").descending());
    }

    @Test
    void test_pagination() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Movies> page = movieRepository.findByStatus(true, pageRequest);
        System.out.println("Total pages: " + page.getTotalPages());
        System.out.println("Total elements: " + page.getTotalElements());
        System.out.println("Content of page: " + page.getContent());
    }
}
