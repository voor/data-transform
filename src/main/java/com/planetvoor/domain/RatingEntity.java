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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author voor
 */
@Data
@Entity(name = "rating")
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

    @Temporal(TemporalType.TIMESTAMP)
    Date time;
}
