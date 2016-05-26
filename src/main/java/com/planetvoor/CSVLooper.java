package com.planetvoor;

import com.opencsv.CSVReader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author voor
 */
@Slf4j
@AllArgsConstructor
@Builder(toBuilder = true)
public class CSVLooper {

    private final Resource resource;

    private final char separator;

    private final ParseOnLine line;

    public void loop() {

        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()), separator)) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {

                line.parse(nextLine);
            }

        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
