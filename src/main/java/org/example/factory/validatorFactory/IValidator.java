package org.example.factory.validatorFactory;

import org.example.domain.Entity;
import org.example.exceptions.EntityException;

public interface IValidator<T extends Entity> {
    void validate(T entity) throws EntityException;
}
