package com.crosby.recipe.service

import com.crosby.exception.domain.RecipeNotFoundException
import com.crosby.recipe.persistence.repositories.RecipeRepository
import com.crosby.recipe.persistence.domain.Recipe
import com.crosby.recipe.persistence.domain.RecipeIngredient
import spock.lang.Specification

class RecipeServiceTest extends Specification {
    RecipeService recipeService
    RecipeRepository recipeRepository = Mock()

    void setup() {
        recipeService = new RecipeService(recipeRepository)
    }

    def "FindById"() {
        given:
            def expectedRecipe = new Recipe()
            recipeRepository.findById(1) >> Optional.of(expectedRecipe)

        when:
            def result = recipeService.findById(1)

        then:
            result.isPresent()
            result.get() == expectedRecipe
    }

    def "FindById not found"() {
        given:
            recipeRepository.findById(1) >> Optional.empty()

        when:
            def result = recipeService.findById(1)

        then:
            result.isEmpty()
    }

    def "Create"() {
        given:
            def inputRecipe = Recipe.builder().ingredients(Collections.singleton(RecipeIngredient.builder().build())).build()
            def resultRecipe = Recipe.builder().id(1L).build()
            recipeRepository.save(_ as Recipe) >> resultRecipe

        when:
            def result = recipeService.save(inputRecipe)

        then:
            result == resultRecipe
    }
}
