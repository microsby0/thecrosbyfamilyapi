package com.crosby.recipe.controller

import com.crosby.recipe.it.AbstractIT
import com.crosby.recipe.persistence.domain.Ingredient
import com.crosby.recipe.persistence.domain.Recipe
import com.crosby.recipe.persistence.domain.RecipeIngredient
import com.crosby.recipe.persistence.domain.UnitOfMeasure
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
        connection.createStatement().executeUpdate("delete from ingredient")
        connection.createStatement().executeUpdate("delete from unit_of_measure")
        connection.createStatement().executeUpdate("delete from recipe")
    }

    def "lookup recipe"() {
        given:
            def time = '2011-12-03T10:15:30Z'
            connection.createStatement().executeUpdate("insert into recipe (id, created_at, last_updated_at, name) values (1, '${time}', '${time}', 'Testipe')")
            connection.createStatement().executeUpdate("INSERT INTO ingredient (id, created_at, last_updated_at, name) VALUES (1, '${time}', '${time}', 'Apple')")
            connection.createStatement().executeUpdate("INSERT INTO unit_of_measure (id, created_at, last_updated_at, unit) VALUES (1, '${time}', '${time}', 'core')")
            connection.createStatement().executeUpdate("INSERT INTO recipe_ingredient (id, created_at, last_updated_at, quantity, ingredient_id, unit_of_measure_id, recipe_id) " +
                    "                                   VALUES (1, '${time}', '${time}', 2.5, 1, 1, 1);")
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

        and:
            def time = '2011-12-03T10:15:30Z'
            connection.createStatement().executeUpdate("INSERT INTO ingredient (id, created_at, name) VALUES (1, '${time}', 'apple')")
            connection.createStatement().executeUpdate("INSERT INTO ingredient (id, created_at, name) VALUES (2, '${time}', 'banana')")
            connection.createStatement().executeUpdate("INSERT INTO unit_of_measure (id, created_at, unit) VALUES (1, '${time}', 'each')")
            connection.createStatement().executeUpdate("INSERT INTO unit_of_measure (id, created_at, unit) VALUES (2, '${time}', 'half')")

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
            ingredients.find { it.getIngredient().getName() == "apple" && it.getUnitOfMeasure().getUnit() == "each" && it.getQuantity() == 3 } != null
            ingredients.find { it.getIngredient().getName() == "banana" && it.getUnitOfMeasure().getUnit() == "half" && it.getQuantity() == 2.5 } != null
    }

    def buildRecipe() {
        def time = '2011-12-03T15:15:30Z'
        def instantTime = Timestamp.from(Instant.parse(time))
        def expectedIngredient = RecipeIngredient.builder()
                .id(1)
                .ingredient(Ingredient.builder().id(1).name("Apple").build())
                .unitOfMeasure(UnitOfMeasure.builder().id(1).unit("core").build())
                .quantity(2.5)
                .build()
        def expectedRecipe = Recipe.builder()
                .id(1)
                .createdAt(instantTime)
                .lastUpdatedAt(instantTime)
                .name("Testipe")
                .ingredients(Collections.singleton(expectedIngredient))
                .build()
        return expectedRecipe
    }

    def createNewRecipeRequest() {
        """
            {
                "name": "Test Recipe",
                "category": "entree",
                "ingredients": [
                    {
                        "ingredient": {
                            "id": 1,
                            "name": "apple"
                        },
                        "unitOfMeasure": {
                            "id": 1,
                            "unit": "each"
                        },
                        "quantity": 3
                    },
                    {
                        "ingredient": {
                            "id": 2,
                            "name": "banana"
                        },
                        "unitOfMeasure": {
                            "id": 2,
                            "unit": "half"
                        },
                        "quantity": 2.5
                    }
                ]
            }
        """
    }

}
