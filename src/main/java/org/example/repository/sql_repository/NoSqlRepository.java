package org.example.repository.sql_repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.domain.Entity;
import org.example.exceptions.RepositoryException;
import org.example.repository.IRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static com.mongodb.client.model.Filters.eq;

public abstract class NoSqlRepository<T extends Entity> implements IRepository<T> {

    protected final MongoCollection<Document> collection;

    public NoSqlRepository(MongoDatabase database, Class<T> className) {
        this.collection = database.getCollection(className.getSimpleName().toLowerCase() + "s");
    }

    @Override
    public void add(T entity) throws RepositoryException {
        if (getById(entity.getId()) != null) {
            throw new RepositoryException("Entity with ID " + entity.getId() + " already exists.");
        }

        try {
            Document document = toDocument(entity);
            collection.insertOne(document);
        } catch (Exception e) {
            throw new RepositoryException("Error while adding entity: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(T entity) throws RepositoryException {
        try {
            Document document = toDocument(entity);
            if (collection.replaceOne(eq("_id", entity.getId()), document).getMatchedCount() == 0) {
                throw new RepositoryException("Entity with ID " + entity.getId() + " does not exist.");
            }
        } catch (Exception e) {
            throw new RepositoryException("Error while updating entity: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(T entity) throws RepositoryException {
        try {
            if (collection.deleteOne(eq("_id", entity.getId())).getDeletedCount() == 0) {
                throw new RepositoryException("Entity with ID " + entity.getId() + " does not exist.");
            }
        } catch (Exception e) {
            throw new RepositoryException("Error while deleting entity: " + e.getMessage(), e);
        }
    }

    @Override
    public Collection<T> getAll() throws RepositoryException {
        try {
            Collection<T> entities = new ArrayList<>();
            for (Document document : collection.find()) {
                entities.add(toEntity(document)); // Convert each document to an entity
            }
            return entities;
        } catch (Exception e) {
            throw new RepositoryException("Error while retrieving all entities: " + e.getMessage(), e);
        }
    }

    @Override
    public T getById(int id) throws RepositoryException {
        try {
            Document document = collection.find(eq("_id", id)).first();
            if (document == null) {
                return null;
            }
            return toEntity(document); // Convert the document to an entity
        } catch (Exception e) {
            throw new RepositoryException("Error while retrieving entity with ID " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Iterator<T> iterator() throws RepositoryException {
        return getAll().iterator();
    }

    // Abstract methods for converting between entity and MongoDB document
    protected abstract Document toDocument(T entity);

    protected abstract T toEntity(Document document);
}
