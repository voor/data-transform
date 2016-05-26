package com.planetvoor;

import com.planetvoor.domain.MovieEntity;
import com.planetvoor.domain.RatingEntity;
import com.planetvoor.domain.UserEntity;
import com.planetvoor.repository.MovieRepository;
import com.planetvoor.repository.RatingRepository;
import com.planetvoor.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories
@Slf4j
public class DataTransformApplication implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    RatingRepository ratingRepository;

    @Value("${files.data.path}")
    private Resource data;
    @Value("${files.movie.path}")
    private Resource movie;
    @Value("${files.user.path}")
    private Resource user;

    public static void main(String[] args) {
        SpringApplication.run(DataTransformApplication.class, args);
    }

    @Override
    public void run(String... params) throws Exception {
        List<String> options = Arrays.asList(params);

        if (options.contains("data")) {
            readDataIn();
        }

        if (options.contains("movie")) {
            readMovieIn();
        }

        if (options.contains("user")) {
            readUserIn();
        }
    }

    public void readUserIn() {
        try {
            CSVLooper.builder().file(user.getFile()).separator('|').line(x -> {
                UserEntity user = UserEntity.builder().id(Long.valueOf(x[0]))
                        .age(Long.valueOf(x[1]))
                        .gender("M".equals(x[2]) ? UserEntity.Gender.MALE : UserEntity.Gender.FEMALE)
                        .job(x[3])
                        .zip(x[4]).build();

                log.debug("Adding new user {}", user);

                userRepository.save(user);
            }).build().loop();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void readMovieIn() {
        try {

            DateTimeFormatter parser = DateTimeFormat.forPattern("dd-MMM-yyyy");
            CSVLooper.builder().file(movie.getFile()).separator('|').line(x -> {

                Date release = null;
                try {
                    release = parser.parseDateTime(x[2]).toDate();
                } catch (IllegalArgumentException e) {
                    log.error("Unable to parse date {} for {}", x[2], x[1]);
                }
                URL url = null;
                try {
                    url = URI.create(x[4]).toURL();
                } catch (Exception e) {
                    log.error("Unable to parse url {} for {}", x[4], x[1]);
                }
                MovieEntity movie = MovieEntity.builder().id(Long.valueOf(x[0]))
                        .title(x[1])
                        .release(release)
                        // x[3] empty
                        .url(url)
                        .unknown("1".equals(x[5]))
                        .action("1".equals(x[6]))
                        .adventure("1".equals(x[6]))
                        .animation("1".equals(x[6]))
                        .children("1".equals(x[6]))
                        .comedy("1".equals(x[6]))
                        .crime("1".equals(x[6]))
                        .documentary("1".equals(x[6]))
                        .drama("1".equals(x[6]))
                        .fantasy("1".equals(x[6]))
                        .filmnoir("1".equals(x[6]))
                        .horror("1".equals(x[6]))
                        .musical("1".equals(x[6]))
                        .mystery("1".equals(x[6]))
                        .romance("1".equals(x[6]))
                        .scifi("1".equals(x[6]))
                        .thriller("1".equals(x[6]))
                        .war("1".equals(x[6]))
                        .western("1".equals(x[6])).build();

                log.debug("Adding new movie {}", movie);

                this.movieRepository.save(movie);
            }).build().loop();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void readDataIn() {

            try {

                CSVLooper.builder().file(data.getFile()).separator('\t').line(x -> {

                    RatingEntity ratingEntity = RatingEntity.builder()
                            .userId(Long.valueOf(x[0]))
                            .movieId(Long.valueOf(x[1]))
                            .rating(Long.valueOf(x[2]))
                            .time(new Date(Long.valueOf(x[3])))
                            .build();

                    ratingRepository.save(ratingEntity);

                }).build().loop();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }

    }
}
