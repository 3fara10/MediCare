package org.example.repository.sql_repository.noSqlWrapper;

import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.domain.Appointment;
import org.example.domain.Patient;
import org.example.exceptions.RepositoryException;
import org.example.repository.sql_repository.NoSqlRepository;

import java.util.Date;

public class AppointmentFactory extends NoSqlRepository<Appointment> {

    private final PatientFactory patientNoSqlRepository;

    public AppointmentFactory(MongoDatabase database, PatientFactory patientNoSqlRepository) {
        super(database, Appointment.class);
        this.patientNoSqlRepository = patientNoSqlRepository;
    }

    @Override
    protected Document toDocument(Appointment appointment) {
        return new Document("_id", appointment.getId())
                .append("date", appointment.getDate())
                .append("purpose", appointment.getPurposeAppointment())
                .append("patient_id", appointment.getPatient().getId());
    }

    @Override
    protected Appointment toEntity(Document document) {
        try {
            int id = document.getInteger("_id");
            Date date = document.getDate("date");
            String purpose = document.getString("purpose");
            int patientId = document.getInteger("patient_id");
            Patient patient = this.patientNoSqlRepository.getbyId(patientId);

            return new Appointment(id,patient,date,purpose);
        }catch (RepositoryException e){
            throw new RuntimeException();
        }
    }
}
