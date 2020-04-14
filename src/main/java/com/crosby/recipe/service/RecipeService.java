package com.crosby.recipe.service;

import com.crosby.recipe.RecipeRepository;
import com.crosby.recipe.domain.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public Optional<Recipe> findById(Long id) {
        return recipeRepository.findById(id);
    }

    public Recipe create(final Recipe recipe) {
        Timestamp now = Timestamp.from(Instant.now());
        recipe.setCreatedAt(now);
        recipe.setLastUpdatedAt(now);
        recipe.getIngredients().forEach(i -> {
            i.setCreatedAt(now);
            i.setLastUpdatedAt(now);
        });
        return recipeRepository.save(recipe);
    }
}
