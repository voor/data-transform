package com.planetvoor;

import com.opencsv.CSVReader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author voor
 */
@Slf4j
@AllArgsConstructor
@Builder(toBuilder = true)
public class CSVLooper {

    private final File file;

    private final char separator;

    private final ParseOnLine line;

    public void loop() {

        try (CSVReader reader = new CSVReader(new FileReader(file), separator)) {
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
