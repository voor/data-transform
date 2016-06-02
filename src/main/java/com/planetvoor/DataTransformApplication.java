package com.planetvoor;

import com.planetvoor.domain.QRatingEntity;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories
@Slf4j
public class DataTransformApplication implements CommandLineRunner {

    @Autowired
    DataTransformService dataTransformService;

    @Value("${files.data.path}")
    private Resource data;
    @Value("${files.movie.path}")
    private Resource movie;
    @Value("${files.user.path}")
    private Resource user;

    @Value("${files.output.path}")
    private Resource output;

    public static void main(String[] args) {
        SpringApplication.run(DataTransformApplication.class, args);
    }

    @Override
    public void run(String... params) throws Exception {
        List<String> options = Arrays.asList(params);

        if (options.contains("all")) {
            dataTransformService.doAll(user, data, movie, output);
        }

        if (options.contains("output")) {

            final Predicate predicate = QRatingEntity.ratingEntity.userEntity.age.between(7, 20);
            final String filename = "youngsters.csv";
            dataTransformService.writeData(output, filename, predicate);
        }

        if (options.contains("input")) {
            dataTransformService.readAll(user, data, movie);
        }

        if (options.contains("data")) {
            dataTransformService.readDataIn(data);
        }

        if (options.contains("movie")) {
            dataTransformService.readMovieIn(movie);
        }

        if (options.contains("user")) {
            dataTransformService.readUserIn(user);
        }
    }

}
