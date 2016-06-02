package com.planetvoor.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author voor
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Entry {

    private final RatingEntity rating;
    private final UserEntity user;
    private final MovieEntity movie;

    public List<String> fields() {
        List<Object> entry = new LinkedList<>();

        if (rating.getRating() > 3) {
            entry.add("1");
        }
        else {
            entry.add("0");
        }
        entry.add(rating.getRating());

        entry.add(user.getAge());
        int ageCategory;
        if (user.getAge() < 21) {
            ageCategory = 0;
        } else if (user.getAge() < 24) {
            ageCategory = 1;
        } else if (user.getAge() < 27) {
            ageCategory = 2;
        } else if (user.getAge() < 30) {
            ageCategory = 3;
        } else if (user.getAge() < 33) {
            ageCategory = 4;
        } else if (user.getAge() < 39) {
            ageCategory = 5;
        } else if (user.getAge() < 44) {
            ageCategory = 6;
        } else if (user.getAge() < 50) {
            ageCategory = 7;
        } else if (user.getAge() < 64) {
            ageCategory = 8;
        } else {
            ageCategory = 9;
        }
        entry.add(ageCategory);
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

        return entry.stream().map(x -> String.valueOf(x)).collect(Collectors.toList());
    }

    public static List<String> headers() {
        List<String> headers = new LinkedList<>();
        headers.add("RECOMMENDED");
        headers.add("RATING");
        headers.add("USER_AGE");
        headers.add("USER_AGE_CATEGORY");
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
        return headers;
    }
}
