package com.crosby.recipe.persistence.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import java.sql.Timestamp;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(NON_NULL)
@JsonIgnoreProperties({"createdAt", "lastUpdatedAt"})
@Entity
public class RecipeIngredient extends BaseDTO {
    @ManyToOne
    @NotNull
    private Ingredient ingredient;
    @ManyToOne
    @NotNull
    private UnitOfMeasure unitOfMeasure;
    @NotNull
    private Double quantity;

    @Builder
    public RecipeIngredient(final Long id, final Timestamp createdAt, final Timestamp lastUpdatedAt,
                            @NotNull final Ingredient ingredient, @NotNull final UnitOfMeasure unitOfMeasure, @NotNull final Double quantity) {
        super(id, createdAt, lastUpdatedAt);
        this.ingredient = ingredient;
        this.unitOfMeasure = unitOfMeasure;
        this.quantity = quantity;
    }
}
