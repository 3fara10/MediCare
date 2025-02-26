package org.example.factory.validatorFactory;

import org.example.domain.Appointment;
import org.example.exceptions.EntityException;
import org.example.exceptions.ValidationException;


/***
 * The PacientValidator class is responsible for validating that a Patient has valid data.
 */
public class AppointmentValidator implements IValidator<Appointment> {

    /**
     * @param appointment is the instance we what to validate
     * @throws ValidationException if :The patient doesn't have a valid id or doesn't exist.The purpose of the appointment doesn't only contain letters and spaces.The appointment date doesn't exist, or it is in the past.
     */
    @Override
    public void validate(Appointment appointment) throws EntityException {
        if (appointment.getPatient() == null || appointment.getPatient().getId() <= 0) {
            throw new ValidationException("Invalid patient associated with the appointment.");
        }

//        if (appointment.getPurposeAppointment().matches("^[a-zA-Z\\s]+$") == false) {
//            throw new ValidationException("The purpose Appointment should contain only letters and spaces.");
//        }

        if (appointment.getDate() == null) {
            throw new ValidationException("Appointment date cannot be null.");
        }

    }
}