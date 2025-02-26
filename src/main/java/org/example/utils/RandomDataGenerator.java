package org.example.utils;

import com.github.javafaker.Faker;
import org.example.domain.Appointment;
import org.example.domain.Patient;
import org.example.exceptions.RepositoryException;
import org.example.repository.IRepository;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class RandomDataGenerator {
    private final Faker faker;
    private final Random random;

    public RandomDataGenerator() {
        this.faker = new Faker();
        this.random = new Random();
    }

    public void generatePatientData(IRepository<Patient> patientIRepository,int counter){
        try {
            for (int i = 0; i < counter; i++) {
                String name = faker.name().lastName();
                String forename = faker.name().firstName();
                int age = random.nextInt(100);
                Patient patient = new Patient(name, forename, age);
                patientIRepository.add(patient);
            }
        }catch (RepositoryException e) {
        }
    }

    public void generateAppointmentData(IRepository<Patient> patientIRepository,IRepository<Appointment> appointmentIRepository,int counter){
        try {
            List<Patient> patients = (List<Patient>) patientIRepository.getAll();
            if (!patients.isEmpty()) {
                for (int i = 0; i < counter; i++) {
                    Date appointmentDate = faker.date().future(30, java.util.concurrent.TimeUnit.DAYS);
                    String purpose = faker.lorem().sentence();

                    Patient randomPatient = patients.get(random.nextInt(patients.size()));
                    Appointment appointment = new Appointment(randomPatient, appointmentDate, purpose);
                    appointmentIRepository.add(appointment);
                }
            }
        }catch (RepositoryException e) {
        }
    }
}
