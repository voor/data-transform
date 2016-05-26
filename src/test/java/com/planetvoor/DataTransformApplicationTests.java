package com.planetvoor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

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

    @Test
    public void readDataIn() {
        dataTransformApplication.readDataIn();
    }

    @Test
    public void doAll() throws IOException {
        dataTransformApplication.doAll();
    }
}