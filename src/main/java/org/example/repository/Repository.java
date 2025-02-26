package org.example.repository;

import org.example.domain.Entity;
import org.example.exceptions.DuplicateException;
import org.example.exceptions.NotExistException;
import org.example.exceptions.RepositoryException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
/**
    GenericRepository class provides a generic implementation of the repository interface.
 */
public class Repository<T extends Entity>implements IRepository<T> {
    protected List<T> entities=new ArrayList<T>();


    @Override
    public void add(T entity) throws RepositoryException {
        for (T e : this.entities) {
            if (e.getId() == entity.getId()) {
                throw new DuplicateException("The entity already exists");
            }
        }
        entities.add(entity);
    }


    @Override
    public void update(T entity) throws RepositoryException {
        for (int i = 0; i < this.entities.size(); i++) {
            T e = this.entities.get(i);
            if (e.getId() == entity.getId()) {
                entities.set(i, entity);
                return;
            }
        }
        throw new NotExistException("The entity does not exist");
    }


    @Override
    public void delete(T entity) throws RepositoryException {
        for (Entity e : this.entities) {
            if (e.getId() == entity.getId()) {
               entities.remove(entity);
               return;
            }
        }
        throw new NotExistException("The entity does not exist");
    }

    @Override
    public Collection<T> getAll() {
        return entities;
    }

    @Override
    public T getById(int id) throws RepositoryException {
        for (T e : this.entities) {
            if (e.getId() == id) {
                return e;
            }
        }
        throw new NotExistException("The entity does not exist");
    }

    @Override
    public Iterator<T> iterator() {
        return this.entities.iterator();
    }
}
