package com.planetvoor.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author voor
 */
@Entity
@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UserEntity implements Serializable {
    @Id
    Long id;

    Long age;

    public enum Gender {
        MALE, FEMALE
    }

    @Enumerated(EnumType.STRING)
    Gender gender;

    String job;

    String zip;
}
