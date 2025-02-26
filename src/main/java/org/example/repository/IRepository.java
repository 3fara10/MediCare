package org.example.repository;

import org.example.domain.Entity;
import org.example.exceptions.RepositoryException;

import java.util.Collection;
import java.util.Iterator;

public interface IRepository<T extends Entity> {

    /**
     * Adds a new entity to the repository if it does not already exist.
     * @param entity the entity to be added
     * @throws RepositoryException if an entity with the same ID already exists in the repository
     */
    void add(T entity) throws RepositoryException;


    /**
     * Updates an existing entity from the repository.
     * @param entity the entity to be updated
     * @throws RepositoryException if the entity to be updated does not exist in the repository
     */

    void update(T entity) throws RepositoryException;

    /**
     * Deletes an existing entity from the repository.
     * @param entity the entity to be deleted
     * @throws RepositoryException if the entity to be deleted does not exist in the repository
     */
    void delete(T entity) throws RepositoryException;


    /**
     * @return a list of all entities
     */
    Collection<T> getAll() throws RepositoryException;


    /**
     * Retrieves an entity by its ID.
     * @param id the unique identifier of the entity to retrieve
     * @return the entity with the specified ID
     * @throws RepositoryException if no entity with the given ID exists in the repository
     */
    T getById(int id) throws RepositoryException;


    Iterator<T> iterator() throws RepositoryException;

}
