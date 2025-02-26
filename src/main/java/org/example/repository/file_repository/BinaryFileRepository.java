package org.example.repository.file_repository;
import org.example.domain.Entity;
import org.example.exceptions.RepositoryException;

import java.io.*;
import java.util.List;

public class BinaryFileRepository<T extends Entity> extends AbstractFileRepository {
    public BinaryFileRepository(String fileName) {
        super(fileName);
        try {
            loadFile();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void saveFile() throws RepositoryException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(entities);
        } catch (IOException e) {
            throw new RepositoryException(e.getMessage(), e);
        }
    }

    @Override
    protected void loadFile() throws RepositoryException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))){
            this.entities = (List) ois.readObject();
        } catch (FileNotFoundException exc) {
        } catch (IOException | ClassNotFoundException exc) {
            throw new RepositoryException("Eroare la incarcarea fisierului", exc);
        }
    }
}
