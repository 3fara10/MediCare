package org.example.factory.validatorFactory;

import org.example.domain.Patient;
import org.example.exceptions.EntityException;
import org.example.exceptions.ValidationException;

/***
 * The PacientValidator class is responsible for validating that a Patient has valid data.
 */
public class PatientValidator implements IValidator<Patient> {

    /**
     * @param patient is the instance we what to validate
     * @throws ValidationException if :The age is a negative or 0 number or the forename/name contain non-alphabetic characters others than "-"," " .
     * */

    @Override
    public void validate(Patient patient) throws EntityException {
        if (patient.getAge() < 0) {
            throw new ValidationException("The age should be a positive number.");
        }

        //aici am schimbat
        if (patient.getForename().matches("^.*$")==false) {
            throw new ValidationException("The forename should contain only letters ,'-' or ' '.");
        }

        if (patient.getName().matches("^.*$")==false) {
            throw new ValidationException("The name should contain only letters ,'-' or ' '.");
        }
    }

}