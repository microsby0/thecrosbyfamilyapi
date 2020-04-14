package com.crosby.exception.domain;

import javax.persistence.EntityNotFoundException;

public class RecipeNotFoundException extends EntityNotFoundException {
    public RecipeNotFoundException(final Long id) {
        super("Recipe with id " + id + " not found");
    }
}
