package org.example.factory.textFactory;

import org.example.domain.Entity;

public interface IEntityFactory<T extends Entity> {
    //TEXT FACTORY
    String toString(T entity);
    T fromString(String string);

}
