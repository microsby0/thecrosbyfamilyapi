package com.crosby.recipe.service

import com.crosby.recipe.RecipeRepository
import com.crosby.recipe.domain.Recipe
import com.crosby.recipe.domain.RecipeIngredient
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
            !result.isPresent()
    }

    def "Create"() {
        given:
            def inputRecipe = new Recipe()
            inputRecipe.setIngredients(Collections.singleton(new RecipeIngredient()))
            def resultRecipe = new Recipe()
            resultRecipe.setId(1)
            recipeRepository.save(_ as Recipe) >> resultRecipe

        when:
            def result = recipeService.create(inputRecipe)

        then:
            result == resultRecipe
    }
}
