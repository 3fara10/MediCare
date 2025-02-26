package org.example.repository.file_repository;

import org.example.domain.Entity;
import org.example.exceptions.EntityException;
import org.example.exceptions.RepositoryException;
import org.example.factory.textFactory.EntityFactory;
import org.example.factory.textFactory.IEntityFactory;

import java.io.*;

public class TextFileRepository<T extends Entity> extends AbstractFileRepository {
    protected IEntityFactory<T> factory;

    public TextFileRepository(String fileName, Class<T> entityClass) {
        super(fileName);
        this.factory = EntityFactory.getFactory(entityClass);
        try {
            loadFile();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void saveFile() throws RepositoryException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.fileName))) {
            for (var entity : this.entities) {
                String asString=factory.toString((T) entity);
                bw.write(asString);
                bw.newLine();
            }
        } catch(EntityException | IOException e){
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    protected void loadFile() throws RepositoryException {
        try (BufferedReader br = new BufferedReader(new FileReader(this.fileName))) {
            String line = br.readLine();
            while (line != null) {
                entities.add(factory.fromString(line));
                line = br.readLine();
            }
        } catch (EntityException | IOException e) {
            throw new RepositoryException("Eroare la citirea fisierului", e);
        }
    }
}

