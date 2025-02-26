package org.example.repository.sql_repository.noSqlWrapper;

import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.domain.Patient;
import org.example.exceptions.RepositoryException;
import org.example.repository.sql_repository.NoSqlRepository;

public class PatientFactory extends NoSqlRepository<Patient> {

    public PatientFactory(MongoDatabase database) {
        super(database, Patient.class);
    }

    @Override
    public Document toDocument(Patient entity) {
        return new Document("_id", entity.getId())
                .append("name", entity.getName())
                .append("forename", entity.getForename())
                .append("age", entity.getAge());
    }

    @Override
    public Patient toEntity(Document document) {
        int id = document.getInteger("_id");
        String name = document.getString("name");
        String forename = document.getString("forename");
        int age = document.getInteger("age");
        return new Patient(id, name, forename, age);
    }

    public Patient getbyId(int id) throws RepositoryException {
        return super.getById(id);
    }
}
