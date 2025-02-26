package org.example.service;

import org.example.domain.Appointment;
import org.example.domain.Patient;
import org.example.domain.raport_entities.PatientAppointmentInfo;
import org.example.domain.raport_entities.PatientLastAppointmentInfo;
import org.example.exceptions.*;
import org.example.factory.validatorFactory.IValidator;
import org.example.factory.validatorFactory.IValidatorFactory;
import org.example.factory.validatorFactory.ValidatorFactory;
import org.example.repository.IRepository;
import org.example.utils.RandomDataGenerator;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PatientService implements IPatientService {

    private final IRepository<Patient> patientIRepository;
    private final IRepository<Appointment> appointmentIRepository;
    private final IValidatorFactory validatorFactory=new ValidatorFactory();
    /**
     * The constructor for the service class
     *
     * @param patientIRepository     the repository to manage Patients
     * @param appointmentIRepository the repository to manage Appointments
     */
    public PatientService(IRepository<Patient> patientIRepository, IRepository<Appointment> appointmentIRepository) {
        this.patientIRepository = patientIRepository;
        this.appointmentIRepository = appointmentIRepository;
        this.generateDataifNeeded();
    }

    /***
     * Add a patient in the Repository
     * @param age : The age of the patient
     * @param forename: The forename of the patient
     * @param name: The name of the patient
     * @throws ValidationException if the name,forename or age is not good.
     * @throws DuplicateException if the patient already exists.
     */
    @Override
    public void add(String name, String forename, int age) throws ValidationException, RepositoryException {
        IValidator<Patient> validator=validatorFactory.getValidator(Patient.class);
        Patient patient = new Patient(name, forename, age);
        validator.validate(patient);
        this.validateEntityExist(patient);
        this.patientIRepository.add(patient);
    }

    /**
     * Returns the patient with the id.
     *
     * @param id : The id of the patient
     */
    @Override
    public Patient get(int id) throws RepositoryException {
        return patientIRepository.getById(id);
    }

    @Override
    public Patient getPatientByNameForenameAndAge(String name, String forename, int age) throws RepositoryException {
        Optional<Patient> patient = this.patientIRepository.getAll().stream()
                .filter(p -> p.getName().equalsIgnoreCase(name) &&
                        p.getForename().equalsIgnoreCase(forename) &&
                        p.getAge() == age)
                .findFirst();

        return patient.orElse(null);
    }
    /**
     * @param id           : The id of the patient.
     * @param newName:     The new name of the patient
     * @param newForename: The new forename of the patient
     * @param newAge:      The new age of the patient
     * @throws ValidationException if the name,forename or age is not good.
     * @throws NotExistException  if the patient doesn't exist.
     */
    @Override
    public void update(int id, String newName, String newForename, int newAge) throws EntityException, RepositoryException {

        Patient patient = this.patientIRepository.getById(id);
        patient.setName(newName);
        patient.setForename(newForename);
        patient.setAge(newAge);
        IValidator<Patient> validator=validatorFactory.getValidator(Patient.class);
        validator.validate(patient);
        this.validateEntityExist(patient);
        this.patientIRepository.update(patient);

        for (Appointment appointment : appointmentIRepository.getAll()) {
            if (appointment.getPatient().getId() == id) {
                appointment.setPatient(patient);
                appointmentIRepository.update(appointment);
            }
        }
    }

    /**Delete a patient from the repository.
     * @param id : The id of the patient.
     * @throws NotExistException if the patient doesn't exist
     */
    @Override
    public void delete(int id) throws RepositoryException {
        Iterator<Appointment> iterator = appointmentIRepository.iterator();

        while (iterator.hasNext()) {
            Appointment appointment = iterator.next();
            if (appointment.getPatient().getId() == id) {
                appointmentIRepository.delete(appointment);
            }
        }
        this.patientIRepository.delete(this.patientIRepository.getById(id));
    }

    @Override
    public List<Patient> getAll() throws RepositoryException {
        return (List<Patient>) patientIRepository.getAll();
    }

    //raports:
    @Override
    public List<PatientAppointmentInfo> getAppointmentsPerPatient() throws RepositoryException {
        try {
            Map<Patient, Long> appointmentsPerPatient = this.appointmentIRepository.getAll().stream()
                    .collect(Collectors.groupingBy(
                            Appointment::getPatient,
                            Collectors.counting()
                    ));


            return appointmentsPerPatient.entrySet().stream()
                    .map(entry -> new PatientAppointmentInfo(entry.getKey(), entry.getValue()))
                    .sorted((info1, info2) -> Long.compare(info2.getAppointmentCount(), info1.getAppointmentCount()))
                    .collect(Collectors.toList());

        } catch (RepositoryException e) {
            throw new RepositoryException("Error retrieving appointments: " + e.getMessage());
        }
    }

    @Override
    public List<PatientLastAppointmentInfo> getDaysSinceLastAppointment() throws RepositoryException {
        try {
            LocalDate today = LocalDate.now();

            return this.appointmentIRepository.getAll().stream()
                    .collect(Collectors.groupingBy(
                            Appointment::getPatient,
                            Collectors.maxBy((a1, a2) -> a1.getDate().compareTo(a2.getDate()))
                    ))
                    .entrySet().stream()
                    .map(entry -> {
                        Patient patient = entry.getKey();
                        java.util.Date lastAppointmentDateAsDate = entry.getValue().get().getDate();
                        LocalDate lastAppointmentDate = lastAppointmentDateAsDate.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();


                        ZonedDateTime zonedDateTime = lastAppointmentDate.atStartOfDay(ZoneId.systemDefault());
                        java.util.Date lastAppointmentDateConverted = java.util.Date.from(zonedDateTime.toInstant());

                        long daysSinceLastAppointment = Math.abs(
                                Duration.between(lastAppointmentDate.atStartOfDay(), today.atStartOfDay()).toDays()
                        );

                        return new PatientLastAppointmentInfo(patient, lastAppointmentDateConverted, daysSinceLastAppointment);
                    })
                    .sorted((info1, info2) -> Long.compare(info2.getDaysSinceLastAppointment(), info1.getDaysSinceLastAppointment()))
                    .collect(Collectors.toList());

        } catch (RepositoryException e) {
            throw new RepositoryException("Error retrieving days since last appointment: " + e.getMessage());
        }
    }



    //private functions
    private void validateEntityExist(Patient patient) throws RepositoryException {
        Iterator<Patient> iterator = patientIRepository.iterator();

        while (iterator.hasNext()) {
            Patient newPatient = iterator.next();
            if (patient.getAge() == newPatient.getAge() && patient.getName().equals(newPatient.getName()) && patient.getForename().equals(newPatient.getForename()) && patient.getId() != newPatient.getId()) {
                throw new RepositoryException("This entity already exist with different id.");
            }
        }
    }

    private void generateDataifNeeded(){
        RandomDataGenerator dataGenerator = new RandomDataGenerator();
        try {
            if (this.patientIRepository.getAll().isEmpty() || this.patientIRepository.getAll().size()<100) {
                dataGenerator.generatePatientData(this.patientIRepository, 100);
            }
        }catch (RepositoryException e){}
    }
}
