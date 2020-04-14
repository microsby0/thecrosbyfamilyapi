package com.crosby.recipe.domain.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "recipe_ingredient")
public class RecipeIngredientDTO extends BaseEntity {
    private String name;
    private String unitOfMeasure;
    private Double quantity;
}
