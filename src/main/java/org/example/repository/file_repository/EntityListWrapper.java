package org.example.repository.file_repository;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "entityList")
public class EntityListWrapper<T> {

    private List<T> entities;

    @XmlElement(name = "entity")
    public List<T> getEntities() {
        return entities;
    }

    public void setEntities(List<T> entities) {
        this.entities = entities;
    }
}