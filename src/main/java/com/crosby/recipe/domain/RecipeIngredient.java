package com.crosby.recipe.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "recipe_ingredient")
public class RecipeIngredient extends BaseEntity {
    private String name;
    private String unitOfMeasure;
    private Double quantity;
}
