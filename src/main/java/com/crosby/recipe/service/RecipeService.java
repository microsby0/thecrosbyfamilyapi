package com.crosby.recipe.service;

import com.crosby.recipe.persistence.domain.Recipe;
import com.crosby.recipe.persistence.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public Optional<Recipe> findById(final Long id) {
        return recipeRepository.findById(id);
    }

    public Recipe save(final Recipe recipe) {
        return recipeRepository.save(recipe);
    }
}
