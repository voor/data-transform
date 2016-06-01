package com.planetvoor;

import com.planetvoor.repository.MovieRepository;
import com.planetvoor.repository.RatingRepository;
import com.planetvoor.repository.UserRepository;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class DataTransformApplicationTests {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    @Autowired
    UserRepository userRepository;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    DataTransformService dataTransformService;
    private Resource output;
    @Value("${files.data.path}")
    private Resource data;
    @Value("${files.movie.path}")
    private Resource movie;
    @Value("${files.user.path}")
    private Resource user;

    @Before
    public void setUp() throws IOException {
        output = new FileSystemResource(testFolder.newFile());
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void thatReadUserInWorks() {
        dataTransformService.readUserIn(user);
        assertEquals(4, userRepository.count());
    }

    @Test
    public void thatReadMovieInWorks() {
        dataTransformService.readMovieIn(movie);
        assertEquals(4, movieRepository.count());
    }

    @Test
    public void thatReadDataInWorks() {
        dataTransformService.readDataIn(data);
        // Nothing should work, since our user and movie repo are empty.
        assertEquals(0, ratingRepository.count());
    }

    @Test
    public void thatDoAllWorks() throws IOException {
        dataTransformService.doAll(user, data, movie, output);

        assertEquals(4, userRepository.count());
        assertEquals(4, movieRepository.count());
        assertEquals(5, ratingRepository.count());

        String result = IOUtils.toString(output.getInputStream(), Charset.defaultCharset());

        assertTrue(result.contains("Get Shorty"));

    }
}
