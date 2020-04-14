package com.crosby.recipe.controller

import com.crosby.recipe.domain.Recipe
import com.crosby.recipe.domain.RecipeIngredient
import com.crosby.recipe.it.AbstractIT
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

import javax.sql.DataSource
import java.sql.Connection
import java.sql.Timestamp
import java.time.Instant

class RecipeControllerIT extends AbstractIT {
    TestRestTemplate testRestTemplate
    Connection connection
    @Autowired
    DataSource embeddedDataSource

    def setup() {
        connection = embeddedDataSource.getConnection()
        testRestTemplate = new TestRestTemplate()
    }

    def cleanup() {
        connection.createStatement().executeUpdate("delete from recipe_ingredient")
        connection.createStatement().executeUpdate("delete from recipe")
    }

    def "lookup recipe"() {
        given:
            connection.createStatement().executeUpdate("insert into recipe (id, created_at, last_updated_at, name) values (1, '2011-12-03T10:15:30Z', '2011-12-03T10:15:30Z', 'Testipe')")
            connection.createStatement().executeUpdate("insert into recipe_ingredient (id, created_at, last_updated_at, name, quantity, unit_of_measure, recipe_id) values (1, '2011-12-03T10:15:30Z', '2011-12-03T10:15:30Z', 'Apple', 2.5, 'core', 1)")
        when:
            def response = testRestTemplate.getForEntity("http://localhost:8090/recipe/1", Recipe.class)

        then:
            def expectedRecipe = buildRecipe()
            response.getStatusCode() == HttpStatus.OK
            response.getBody() == expectedRecipe
    }

    def "lookup recipe not found"() {
        when:
            def response = testRestTemplate.getForEntity("http://localhost:8090/recipe/1", String.class)

        then:
            response.getStatusCode() == HttpStatus.NOT_FOUND
    }

    def "create recipe"() {
        given:
            def requestBody = createNewRecipeRequest()
            def headers = new HttpHeaders()
            headers.setContentType(MediaType.APPLICATION_JSON)
            def request = new HttpEntity<>(requestBody, headers)
        when:
            def response = testRestTemplate.postForEntity("http://localhost:8090/recipe", request, Recipe.class)

        then:
            def result = connection.createStatement().executeQuery("select count(*) as count from recipe")
            result.next()
            result.getInt("count") == 1
            response.getStatusCode() == HttpStatus.CREATED
            def responseBody = response.getBody()
            responseBody.getId() == 1
            responseBody.getCategory() == "entree"
            responseBody.getName() == "Test Recipe"
        and:
            def ingredients = responseBody.getIngredients()
            ingredients.find {it.getName() == "apple" && it.getUnitOfMeasure() == "each" && it.getQuantity() == 3} != null
            ingredients.find {it.getName() == "banana" && it.getUnitOfMeasure() == "half" && it.getQuantity() == 2.5} != null
    }

    def buildRecipe() {
        def time = '2011-12-03T15:15:30Z'
        def instantTime = Timestamp.from(Instant.parse(time))
        def expectedIngredient = new RecipeIngredient()
        expectedIngredient.setId(1)
        expectedIngredient.setCreatedAt(instantTime)
        expectedIngredient.setLastUpdatedAt(instantTime)
        expectedIngredient.setName("Apple")
        expectedIngredient.setQuantity(2.5)
        expectedIngredient.setUnitOfMeasure("core")
        def expectedRecipe = new Recipe()
        expectedRecipe.setId(1)
        expectedRecipe.setCreatedAt(instantTime)
        expectedRecipe.setLastUpdatedAt(instantTime)
        expectedRecipe.setName('Testipe')
        expectedRecipe.setIngredients(Collections.singleton(expectedIngredient))
        return expectedRecipe
    }

    def createNewRecipeRequest() {
        """
            {
                "name": "Test Recipe",
                "category": "entree",
                "ingredients": [
                    {
                        "name": "apple",
                        "unitOfMeasure": "each",
                        "quantity": 3
                    },
                    {
                        "name": "banana",
                        "unitOfMeasure": "half",
                        "quantity": 2.5
                    }
                ]
            }
        """
    }

}
