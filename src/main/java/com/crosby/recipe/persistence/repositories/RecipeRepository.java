package com.crosby.recipe.persistence.repositories;

import org.springframework.data.repository.CrudRepository;
import com.crosby.recipe.persistence.domain.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
