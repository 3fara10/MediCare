package org.example.factory.textFactory;

import org.example.domain.Entity;
import org.example.domain.Patient;
import org.example.exceptions.EntityException;
import org.example.factory.validatorFactory.IValidator;
import org.example.factory.validatorFactory.IValidatorFactory;
import org.example.factory.validatorFactory.ValidatorFactory;
import org.example.utils.ReadingTypeUtils;

public class PatientFactory implements IEntityFactory<Patient> {

    private static final IValidatorFactory<Entity> validatorFactory = new ValidatorFactory<>();

    @Override
    public String toString(Patient patient) {
        return patient.getId()+";"+patient.getName()+";"+patient.getForename()+";"+patient.getAge();
    }

    @Override
    public Patient fromString(String string) throws EntityException{
        String[] tokens = ReadingTypeUtils.readingStringArray(string);
        int id= ReadingTypeUtils.readingInt(tokens[0]);
        String name= ReadingTypeUtils.readingString(tokens[1]);
        String forename= ReadingTypeUtils.readingString(tokens[2]);
        int age= ReadingTypeUtils.readingInt(tokens[3]);

        Patient patient = new Patient(id, name, forename, age);
        IValidator<Patient> patientValidator = validatorFactory.getValidator(Patient.class);
        patientValidator.validate(patient);

        return patient;
    }


}
