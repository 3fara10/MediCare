package org.example.factory.validatorFactory;

import org.example.domain.Appointment;
import org.example.domain.Entity;
import org.example.domain.Patient;
import org.example.exceptions.EntityException;
import org.example.exceptions.ValidationException;

import java.util.HashMap;
import java.util.Map;

public class ValidatorFactory<T extends Entity> implements IValidatorFactory<T> {

    protected static final Map<Class<?>, IValidator<?>> validators = new HashMap<>();

    static {
        validators.put(Patient.class, new PatientValidator());
        validators.put(Appointment.class, new AppointmentValidator());
    }

    @Override
    public <T extends Entity> IValidator<T> getValidator(Class<T> entityClass) throws EntityException {
        IValidator<T> validator = (IValidator<T>) validators.get(entityClass);
        if (validator == null) {
            throw new ValidationException("No validator found for entity: " + entityClass.getName());
        }
        //play attention here
        return validator;
    }
}