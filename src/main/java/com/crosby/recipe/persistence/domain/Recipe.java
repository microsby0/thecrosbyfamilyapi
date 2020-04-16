package com.crosby.recipe.persistence.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(NON_NULL)
@ToString(callSuper = true)
@Entity
public class Recipe extends BaseDTO {
    @NotNull
    @Column(unique = true)
    private String name;
    private String category; // appetizer, entree, dessert, etc
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "recipe_id")
    @NotNull
    private Set<RecipeIngredient> ingredients;
    private Integer cookTimeInMinutes;
    private Integer prepTimeInMinutes;
    private Integer servings;

    @Builder
    public Recipe(final Long id, final Timestamp createdAt, final Timestamp lastUpdatedAt, @NotNull final String name,
                  final String category, @NotNull final Set<RecipeIngredient> ingredients, final Integer cookTimeInMinutes,
                  final Integer prepTimeInMinutes, final Integer servings) {
        super(id, createdAt, lastUpdatedAt);
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
        this.cookTimeInMinutes = cookTimeInMinutes;
        this.prepTimeInMinutes = prepTimeInMinutes;
        this.servings = servings;
    }
}

