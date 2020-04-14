package com.crosby.recipe.controller

import com.crosby.exception.domain.RecipeNotFoundException
import com.crosby.recipe.domain.Recipe
import com.crosby.recipe.service.RecipeService
import org.springframework.http.HttpStatus
import spock.lang.Specification

class RecipeControllerTest extends Specification {

    RecipeController recipeController

    RecipeService recipeService = Mock()

    def setup() {
        recipeController = new RecipeController(recipeService)
    }

    def "get recipe success"() {
        given:
            def expectedRecipe = new Recipe()
            recipeService.findById(1) >> Optional.of(expectedRecipe)

        when:
            def response = recipeController.getRecipe(1)

        then:
            response.getStatusCode() == HttpStatus.OK
            response.getBody() == expectedRecipe
    }

    def "get recipe not found"() {
        given:
            recipeService.findById(1) >> Optional.empty()

        when:
            recipeController.getRecipe(1)

        then:
            thrown(RecipeNotFoundException)
    }

    def "create recipe success"() {
        given:
            def expectedRecipe = new Recipe()
            expectedRecipe.setId(2)
            def inputRecipe = new Recipe()
            recipeService.create(inputRecipe) >> expectedRecipe

        when:
            def response = recipeController.create(inputRecipe)

        then:
            response.getStatusCode() == HttpStatus.CREATED
            response.getBody() == expectedRecipe
    }
}
