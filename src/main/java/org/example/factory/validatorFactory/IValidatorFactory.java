package org.example.factory.validatorFactory;

import org.example.domain.Entity;
import org.example.exceptions.EntityException;

public interface IValidatorFactory<T extends Entity> {
    // ERROR in classic form: Type parameter 'T' is not within its bound; should extend 'domain. Entity'

    <T extends Entity> IValidator<T> getValidator(Class<T> entityClass) throws EntityException;
}
