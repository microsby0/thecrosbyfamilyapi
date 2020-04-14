package com.crosby.recipe.controller;

import com.crosby.exception.domain.RecipeNotFoundException;
import com.crosby.recipe.domain.Recipe;
import com.crosby.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable final Long id) {
        Optional<Recipe> recipe = recipeService.findById(id);
        return ResponseEntity.ok(recipe.orElseThrow(() -> new RecipeNotFoundException(id)));
    }

    @PostMapping
    public ResponseEntity<Recipe> create(@RequestBody final Recipe recipe) {
        log.info("Creating recipe " + recipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.create(recipe));
    }
}
