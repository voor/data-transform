package com.planetvoor;

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

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class DataTransformApplicationTests {

    private Resource output;

    @Value("${files.data.path}")
    private Resource data;
    @Value("${files.movie.path}")
    private Resource movie;
    @Value("${files.user.path}")
    private Resource user;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        output = new FileSystemResource(testFolder.newFile());
    }

    @Autowired
    DataTransformService dataTransformService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void thatReadUserInWorks() {
        dataTransformService.readUserIn(user);
    }

    @Test
    public void thatReadMovieInWorks() {
        dataTransformService.readMovieIn(movie);
    }

    @Test
    public void thatReadDataInWorks() {
        dataTransformService.readDataIn(data);
    }

    @Test
    public void thatDoAllWorks() throws IOException {
        dataTransformService.doAll(user, data, movie, output);
    }
}
