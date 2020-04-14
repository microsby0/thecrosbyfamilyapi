package com.crosby.recipe.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@JsonInclude(NON_NULL)
@ToString(callSuper = true)
@Entity
@Table(name = "recipe")
public class RecipeDTO extends BaseEntity {
    @NotNull
    private String name;
    private String category; // appetizer, entree, dessert, etc
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "recipeId")
    private Set<RecipeIngredientDTO> ingredients;
    private Integer cookTimeInMinutes;
    private Integer prepTimeInMinutes;
    private Integer servings;
}

