package com.crosby.recipe;

import com.crosby.recipe.domain.dto.RecipeDTO;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<RecipeDTO, Long> {
}
