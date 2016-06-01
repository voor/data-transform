package com.planetvoor.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.Date;

/**
 * @author voor
 */
@Data
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RatingEntity {

    @Id
    @GeneratedValue
    Long id;

    Long userId;

    Long movieId;

    @OneToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    UserEntity userEntity;

    @OneToOne
    @JoinColumn(name = "movieId", insertable = false, updatable = false)
    MovieEntity movieEntity;

    Long rating;

    Date time;
}
