package org.example.service;

import org.example.domain.Appointment;
import org.example.domain.raport_entities.MonthAppointmentsInfo;
import org.example.exceptions.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IAppointmentService {

    /**
     * Adds a new appointment for a patient.
     * @param patientId         the ID of the patient for whom the appointment is being made
     * @param date              the date and time of the appointment
     * @param purposeAppointment the purpose of the appointment
     * @throws RuntimeException if the patient does not exist or if the appointment overlaps with an existing one
     */
    void add(int patientId, Date date, String purposeAppointment) throws RepositoryException,EntityException;

    /**
     * Retrieves an appointment by its unique identifier.
     * @param id the unique identifier of the appointment
     * @return the appointment with the specified ID
     * @throws RepositoryException if no appointment with the given ID exists
     */
    Appointment get(int id) throws RepositoryException;


    /**
     * Updates an existing appointment.
     * @param idAppointment      the ID of the appointment to be updated
     * @param idPatient         the new ID of the patient associated with the appointment
     * @param date              the new date and time of the appointment
     * @param purposeAppointment the new purpose of the appointment
     * @throws RepositoryException if the appointment or patient does not exist
     * @throws RepositoryException if the updated appointment is invalid
     */
    void update(int idAppointment, int idPatient, Date date, String purposeAppointment) throws RepositoryException, EntityException;


    /**
     * Deletes an appointment by its unique identifier.
     * @param id the ID of the appointment to be deleted
     * @throws RepositoryException if the appointment does not exist
     */
    void delete(int id) throws RepositoryException;


    /**
     * Retrieves all appointments.
     * @return a list of all appointments
     */
    List<Appointment> getAll() throws RepositoryException;

    List<MonthAppointmentsInfo> getAppointmentsPerMonth() throws RepositoryException;

    List<MonthAppointmentsInfo> getBusiestMonths() throws RepositoryException;

   Appointment findAppointment(int patientId, Date date, String purpose) throws RepositoryException;
}
