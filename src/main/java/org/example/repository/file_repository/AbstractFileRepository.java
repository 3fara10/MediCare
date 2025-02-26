package org.example.repository.file_repository;

import org.example.domain.Entity;
import org.example.exceptions.RepositoryException;
import org.example.repository.Repository;

import java.util.Collection;

public abstract class AbstractFileRepository<T extends Entity> extends Repository<T> {
    protected String fileName;

    public AbstractFileRepository(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Adds a new entity to the repository if it does not already exist.
     * @param entity the entity to be added
     * @throws RepositoryException if an entity with the same ID already exists in the repository
     */
    @Override
    public void add(T entity) throws RepositoryException {
        super.add(entity);
        saveFile();
    }

    /**
     * Deletes an existing entity from the repository.
     * @param entity the entity to be deleted
     * @throws RepositoryException if the entity to be deleted does not exist in the repository
     */
    @Override
    public void delete(T entity) throws RepositoryException {
        super.delete(entity);
        saveFile();
    }

    /**
     * Updates an existing entity from the repository.
     * @param entity the entity to be updated
     * @throws RepositoryException if the entity to be updated does not exist in the repository
     */
    @Override
    public void update(T entity) throws RepositoryException{
        super.update(entity);
        saveFile();
    }

    @Override
    public Collection<T> getAll() {
        return super.getAll();
    }

    /**
     * Retrieves an entity by its ID.
     * @param id the unique identifier of the entity to retrieve
     * @return the entity with the specified ID
     * @throws RepositoryException if no entity with the given ID exists in the repository
     */
     public T getById(int id) throws RepositoryException{
         return super.getById(id);
     }


    /**
     * Non-abstract subclasses will have to implement this method. There are many exceptions that can appear when
     * working with files/databases.
     *
     * @throws RepositoryException
     */
    protected abstract void saveFile() throws RepositoryException;

    /**
     * Non-abstract subclasses will have to implement this method. There are many exceptions that can appear when
     * working with files/databases.
     *
     * @throws RepositoryException
     */
    protected abstract void loadFile() throws RepositoryException;
}
