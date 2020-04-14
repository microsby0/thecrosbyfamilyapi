package com.crosby.recipe.service;

import com.crosby.recipe.RecipeRepository;
import com.crosby.recipe.domain.dto.RecipeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public Optional<RecipeDTO> findById(Long id) {
        return recipeRepository.findById(id);
    }

    public RecipeDTO create(final RecipeDTO recipe) {
        Instant now = Instant.now();
        recipe.setCreatedAt(now);
        recipe.setLastUpdatedAt(now);
        recipe.getIngredients().forEach(i -> {
            i.setCreatedAt(now);
            i.setLastUpdatedAt(now);
        });
        return recipeRepository.save(recipe);
    }
}
