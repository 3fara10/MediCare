package org.example.service;

import org.example.domain.Patient;
import org.example.domain.raport_entities.PatientAppointmentInfo;
import org.example.domain.raport_entities.PatientLastAppointmentInfo;
import org.example.exceptions.*;

import java.util.List;

public interface IPatientService {
    /**
     * Adds a new patient after validating the input data.
     *
     * @param name the name of the patient
     * @param forename the forename of the patient
     * @param age the age of the patient
     * @throws EntityException if the patient data is invalid
     * @throws RepositoryException if a patient with the same ID already exists
     */
    void add(String name, String forename, int age) throws RepositoryException, EntityException;


    /**
     * Retrieves a patient by their ID.
     * @param id the unique identifier of the patient
     * @return the Patient object with the specified ID
     * @throws RepositoryException if no patient with the given ID exists
     */
    Patient get(int id) throws RepositoryException;


    Patient getPatientByNameForenameAndAge(String name, String forename, int age) throws RepositoryException;

    /**
     * Updates an existing patient's information.
     * @param id the unique identifier of the patient to update
     * @param newName the new name of the patient
     * @param newForename the new forename of the patient
     * @param newAge the new age of the patient
     * @throws EntityException if the updated patient data is invalid
     * @throws RepositoryException if the patient with the specified ID does not exist
     */
    void update(int id, String newName, String newForename, int newAge) throws RepositoryException, EntityException;


    /**
     * Deletes a patient by their ID.
     * @param id the unique identifier of the patient to delete
     * @throws RepositoryException if the patient with the specified ID does not exist
     */
    void delete(int id) throws RepositoryException;


    /**
     * Retrieves all patients from the repository.
     *
     * @return a list of all Patient objects
     */
    List<Patient> getAll() throws RepositoryException;


    List<PatientAppointmentInfo> getAppointmentsPerPatient() throws RepositoryException;

    List<PatientLastAppointmentInfo> getDaysSinceLastAppointment() throws RepositoryException;
}
