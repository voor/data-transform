package com.planetvoor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author voor
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DataTransformIT {

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


    @Test
    public void thatDoAllWorks() throws IOException {
        dataTransformService.doAll(user, data, movie, output);
    }
}
