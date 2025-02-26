package org.example.repository.sql_repository;

import org.example.domain.Entity;
import org.example.exceptions.RepositoryException;
import org.example.repository.IRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.naming.Referenceable;
import java.util.Iterator;
import java.util.List;

public class SQLRepository<T extends Entity> implements IRepository<T> {

    private final SessionFactory sessionFactory;
    private final Class<T> entityClass;


    public SQLRepository(Class<T> entityClass, SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;

    }

    @Override
    public void add(T entity) throws RepositoryException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Error saving entity", e);
        }
    }

    @Override
    public void update(T entity) throws RepositoryException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Error updating entity", e);
        }
    }

    @Override
    public void delete(T entity) throws RepositoryException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.remove(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Error deleting entity", e);
        }
    }

    @Override
    public List<T> getAll() throws RepositoryException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            return session.createQuery("from " + entityClass.getName(), entityClass).getResultList();
        } catch (Exception e) {
            throw new RepositoryException("Error fetching all entities", e);
        }
    }

    @Override
    public T getById(int id) throws RepositoryException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            T entity = session.get(entityClass, id);
            if (entity == null) {
                throw new RepositoryException("Entity with ID " + id + " not found");
            }
            return entity;
        } catch (Exception e) {
            throw new RepositoryException("Error fetching entity by ID", e);
        }
    }


    @Override
    public Iterator<T> iterator() throws RepositoryException {
        List<T> entities = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            entities = session.createQuery("FROM " + entityClass.getName(), entityClass).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RepositoryException("Error fetching all entities", e);
        }


        return entities.iterator();
    }
}
