package com.planetvoor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataTransformApplicationTests {

    @Autowired
    DataTransformApplication dataTransformApplication;

	@Test
	public void contextLoads() {
	}

	@Test
	public void readUserIn() {
        dataTransformApplication.readUserIn();
    }

    @Test
    public void readMovieIn() {
        dataTransformApplication.readMovieIn();
    }
}
