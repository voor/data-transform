package com.planetvoor.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.net.URL;
import java.util.Date;

/**
 * @author voor
 */
@Entity
@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MovieEntity implements Serializable {

    @Id
    Long id;

    String title;

    Date release;

    URL url;

    Boolean unknown;

    Boolean action;

    Boolean adventure;

    Boolean animation;

    Boolean children;

    Boolean comedy;

    Boolean crime;

    Boolean documentary;

    Boolean drama;

    Boolean fantasy;

    Boolean filmnoir;

    Boolean horror;

    Boolean musical;

    Boolean mystery;

    Boolean romance;

    Boolean scifi;

    Boolean thriller;

    Boolean war;

    Boolean western;
}
