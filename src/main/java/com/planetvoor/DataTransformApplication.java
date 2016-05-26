package com.planetvoor;

import com.opencsv.CSVWriter;
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
import org.springframework.util.StringUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Value("${files.output.path}")
    private Resource output;

    public static void main(String[] args) {
        SpringApplication.run(DataTransformApplication.class, args);
    }

    @Override
    public void run(String... params) throws Exception {
        List<String> options = Arrays.asList(params);

        if (options.contains("all")) {
            doAll();
        }

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

        CSVLooper.builder().resource(user).separator('|').line(x -> {
            UserEntity user = UserEntity.builder().id(Long.valueOf(x[0]))
                    .age(Long.valueOf(x[1]))
                    .gender("M".equals(x[2]) ? UserEntity.Gender.MALE : UserEntity.Gender.FEMALE)
                    .job(x[3])
                    .zip(x[4]).build();

            log.debug("Adding new user {}", user);

            userRepository.save(user);
        }).build().loop();

    }

    public void readMovieIn() {


        DateTimeFormatter parser = DateTimeFormat.forPattern("dd-MMM-yyyy");
        CSVLooper.builder().resource(movie).separator('|').line(x -> {

            log.info("Parsing line: {}",Arrays.asList(x));
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
                    .adventure("1".equals(x[7]))
                    .animation("1".equals(x[8]))
                    .children("1".equals(x[9]))
                    .comedy("1".equals(x[10]))
                    .crime("1".equals(x[11]))
                    .documentary("1".equals(x[12]))
                    .drama("1".equals(x[13]))
                    .fantasy("1".equals(x[14]))
                    .filmnoir("1".equals(x[15]))
                    .horror("1".equals(x[16]))
                    .musical("1".equals(x[17]))
                    .mystery("1".equals(x[18]))
                    .romance("1".equals(x[19]))
                    .scifi("1".equals(x[20]))
                    .thriller("1".equals(x[21]))
                    .war("1".equals(x[22]))
                    .western("1".equals(x[23])).build();

            log.debug("Adding new movie {}", movie);

            this.movieRepository.save(movie);
        }).build().loop();

    }

    public void readDataIn() {


        CSVLooper.builder().resource(data).separator('\t').line(x -> {

            RatingEntity ratingEntity = RatingEntity.builder()
                    .userId(Long.valueOf(x[0]))
                    .movieId(Long.valueOf(x[1]))
                    .rating(Long.valueOf(x[2]))
                    .time(new Date(Long.valueOf(x[3])))
                    .build();

            ratingRepository.save(ratingEntity);

        }).build().loop();


    }

    public void writeData() throws IOException {

        if (output.getFile().exists()) {
            throw new RuntimeException("File already exists, please remove it or rename it.");
        }
        log.info("Writing out results to {}", output.getFile().getAbsolutePath());
        if (!output.getFile().getParentFile().canWrite()) {
            throw new RuntimeException("Can not write to provided location.");
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter(output.getFile()), ',')) {

            List<String> headers = new LinkedList<>();
            headers.add("RECOMMENDED");
            headers.add("RATING");
            headers.add("USER_AGE");
            headers.add("USER_MALE");
            headers.add("USER_JOB");
            headers.add("USER_ZIP");
            headers.add("MOVIE_NAME");
            headers.add("unknown");
            headers.add("action");
            headers.add("adventure");
            headers.add("animation");
            headers.add("children");
            headers.add("comedy");
            headers.add("crime");
            headers.add("documentary");
            headers.add("drama");
            headers.add("fantasy");
            headers.add("filmnoir");
            headers.add("horror");
            headers.add("musical");
            headers.add("mystery");
            headers.add("romance");
            headers.add("scifi");
            headers.add("thriller");
            headers.add("war");
            headers.add("western");

            writer.writeNext(StringUtils.toStringArray(headers));

            for (RatingEntity rating : ratingRepository.findAll()) {
                List<Object> entry = new LinkedList<>();

                Long rate = rating.getRating();
                if (rate > 3) {
                    entry.add("1");
                } else {
                    entry.add("0");
                }
                entry.add(rating.getRating());

                UserEntity user = userRepository.findOne(rating.getUserId());
                MovieEntity movie = movieRepository.findOne(rating.getMovieId());


                entry.add(user.getAge());
                entry.add(UserEntity.Gender.MALE.equals(user.getGender()) ? "1" : "0");
                entry.add(user.getJob());
                entry.add(user.getZip());
                entry.add(movie.getTitle());
                entry.add(movie.getUnknown() ? "1" : "0");
                entry.add(movie.getAction() ? "1" : "0");
                entry.add(movie.getAdventure() ? "1" : "0");
                entry.add(movie.getAnimation() ? "1" : "0");
                entry.add(movie.getChildren() ? "1" : "0");
                entry.add(movie.getComedy() ? "1" : "0");
                entry.add(movie.getCrime() ? "1" : "0");
                entry.add(movie.getDocumentary() ? "1" : "0");
                entry.add(movie.getDrama() ? "1" : "0");
                entry.add(movie.getFantasy() ? "1" : "0");
                entry.add(movie.getFilmnoir() ? "1" : "0");
                entry.add(movie.getHorror() ? "1" : "0");
                entry.add(movie.getMusical() ? "1" : "0");
                entry.add(movie.getMystery() ? "1" : "0");
                entry.add(movie.getRomance() ? "1" : "0");
                entry.add(movie.getScifi() ? "1" : "0");
                entry.add(movie.getThriller() ? "1" : "0");
                entry.add(movie.getWar() ? "1" : "0");
                entry.add(movie.getWestern() ? "1" : "0");

                writer.writeNext(StringUtils.toStringArray(entry.stream().map(x -> String.valueOf(x)).collect(Collectors.toList())));
            }

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }


    public void readAll() {
        readUserIn();
        readDataIn();
        readMovieIn();


    }

    public void doAll() throws IOException {
        readAll();
        writeData();
    }
}
