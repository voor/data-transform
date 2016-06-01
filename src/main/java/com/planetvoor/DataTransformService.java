package com.planetvoor;

import com.opencsv.CSVWriter;
import com.planetvoor.domain.Entry;
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
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;

/**
 * @author voor
 */
@Service
@Slf4j
public class DataTransformService {

    private final UserRepository userRepository;

    private final MovieRepository movieRepository;

    private final RatingRepository ratingRepository;

    @Autowired
    public DataTransformService(UserRepository userRepository, MovieRepository movieRepository, RatingRepository ratingRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.ratingRepository = ratingRepository;
    }

    public void readUserIn(Resource input) {

        if (!input.isReadable()) {
            throw new RuntimeException("Resource is not readable.");
        }

        CSVLooper.builder().resource(input).separator('|').line(x -> {
            UserEntity user = UserEntity.builder().id(Long.valueOf(x[0])).age(Long.valueOf(x[1]))
                    .gender("M".equals(x[2]) ? UserEntity.Gender.MALE : UserEntity.Gender.FEMALE).job(x[3]).zip(x[4]).build();

            log.debug("Adding new user {}", user);

            userRepository.save(user);
        }).build().loop();

    }

    public void readMovieIn(Resource input) {

        if (!input.isReadable()) {
            throw new RuntimeException("Resource is not readable.");
        }

        DateTimeFormatter parser = DateTimeFormat.forPattern("dd-MMM-yyyy");
        CSVLooper.builder().resource(input).separator('|').line(x -> {

            log.info("Parsing line: {}", Arrays.asList(x));
            Date release = null;
            try {
                release = parser.parseDateTime(x[2]).toDate();
            }
            catch (IllegalArgumentException e) {
                log.error("Unable to parse date {} for {}", x[2], x[1]);
            }
            URL url = null;
            try {
                url = URI.create(x[4]).toURL();
            }
            catch (Exception e) {
                log.error("Unable to parse url {} for {}", x[4], x[1]);
            }
            MovieEntity movie = MovieEntity.builder().id(Long.valueOf(x[0])).title(x[1]).release(release)
                    // x[3] empty
                    .url(url).unknown("1".equals(x[5])).action("1".equals(x[6])).adventure("1".equals(x[7])).animation("1".equals(x[8]))
                    .children("1".equals(x[9])).comedy("1".equals(x[10])).crime("1".equals(x[11])).documentary("1".equals(x[12]))
                    .drama("1".equals(x[13])).fantasy("1".equals(x[14])).filmnoir("1".equals(x[15])).horror("1".equals(x[16]))
                    .musical("1".equals(x[17])).mystery("1".equals(x[18])).romance("1".equals(x[19])).scifi("1".equals(x[20]))
                    .thriller("1".equals(x[21])).war("1".equals(x[22])).western("1".equals(x[23])).build();

            log.debug("Adding new movie {}", movie);

            this.movieRepository.save(movie);
        }).build().loop();

    }

    public void readDataIn(Resource input) {

        if (!input.isReadable()) {
            throw new RuntimeException("Resource is not readable.");
        }

        CSVLooper.builder().resource(input).separator('\t').line(x -> {

            final Long userId = Long.valueOf(x[0]);
            final Long movieId = Long.valueOf(x[1]);
            RatingEntity ratingEntity = RatingEntity.builder().userId(userId).movieId(movieId).rating(Long.valueOf(x[2]))
                    .time(new Date(Long.valueOf(x[3]))).build();

            try {
                ratingRepository.save(ratingEntity);
            }
            catch (DataIntegrityViolationException e) {
                log.error("Attempted to add rating that was missing either a user ({}) or a movie ({})", userId, movieId);
                if (userRepository.findOne(userId) == null) {
                    log.error("No user id exists for {}", userId);
                }
                if (movieRepository.findOne(movieId) == null) {
                    log.error("No movie ud exists for {}", movieId);
                }
            }

        }).build().loop();

    }

    public void writeData(Resource output) throws IOException {

        if (output.getFile().exists()) {
            final File oldFile = new File(output.getFile().getAbsolutePath() + System.currentTimeMillis());
            output.getFile().renameTo(oldFile);
        }
        log.info("Writing out results to {}", output.getFile().getAbsolutePath());
        if (!output.getFile().getParentFile().canWrite()) {
            throw new RuntimeException("Can not write to provided location.");
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter(output.getFile()), ',')) {

            writer.writeNext(StringUtils.toStringArray(Entry.headers()));

            for (RatingEntity rating : ratingRepository.findAll()) {

                if (rating.getUserEntity() != null && rating.getMovieEntity() != null) {
                    writeEntry(writer, rating);
                }
            }

        }
        catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void readAll(Resource user, Resource data, Resource movie) {
        readUserIn(user);
        readMovieIn(movie);
        readDataIn(data);
    }

    public void doAll(Resource user, Resource data, Resource movie, Resource output) throws IOException {
        readAll(user, data, movie);
        writeData(output);
    }

    protected void writeEntry(CSVWriter writer, RatingEntity rating) {

        writer.writeNext(StringUtils
                .toStringArray(Entry.builder().rating(rating).user(rating.getUserEntity()).movie(rating.getMovieEntity()).build().fields()));
    }
}
