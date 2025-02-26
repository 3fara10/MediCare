package org.example.repository.sql_repository.noSqlWrapper;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.example.domain.Appointment;
import org.example.domain.Patient;
import org.example.repository.IRepository;


public class NoSqlRepositoryFactory {

    private final MongoDatabase database;
    private final PatientFactory patientFactory; //nu imi place asta

    public NoSqlRepositoryFactory(String databasePath, String databaseName) {
        MongoClient mongoClient = MongoClients.create(databasePath);
        this.database = mongoClient.getDatabase(databaseName);
        this.patientFactory = new PatientFactory(database);
    }


    public PatientFactory createPatientRepository() {
        return patientFactory;
    }


    public AppointmentFactory createAppointmentRepository() {
        return new AppointmentFactory(database, patientFactory);
    }

    public IRepository createRepository(Class entityClass) {
        if (entityClass == Patient.class) {
            return createPatientRepository();
        } else if (entityClass == Appointment.class) {
            return createAppointmentRepository();
        } else {
            throw new IllegalArgumentException("No repository found for entity " + entityClass.getName());
        }
    }
}
