package com.crosby.recipe.persistence.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import java.sql.Timestamp;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(NON_NULL)
@JsonIgnoreProperties({"createdAt", "lastUpdatedAt"})
@Entity
public class Ingredient extends BaseDTO {
    @NotNull
    @Column(unique = true)
    private String name;

    @Builder
    public Ingredient(final Long id, final Timestamp createdAt, final Timestamp lastUpdatedAt, final String name) {
        super(id, createdAt, lastUpdatedAt);
        this.name = name;
    }
}
