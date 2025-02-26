package org.example.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.xml.bind.annotation.*;

import javax.persistence.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents an entity with a unique identifier.
 */
@JsonTypeName("entity")
@XmlRootElement(name = "entity") // Marking this class as the root element for XML serialization
@XmlSeeAlso({Patient.class, Appointment.class})
@XmlType(propOrder = {"id"})
// This annotation is used to define subclasses to be serialized
@MappedSuperclass
public abstract class Entity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    protected int id;

    @Serial
    private static final long serialVersionUID = 1L;

    private static final IdGenerator idGenerator = IdGenerator.getInstance("C:\\Users\\anton\\IdeaProjects\\Lab3\\src\\main\\java\\org\\example\\entityId.txt");

    // Default constructor for JAXB
    public Entity() {
        this.id = idGenerator.generateID(); // Generate a new ID when an entity is created
    }

    // Getter for ID - this will be serialized as an XML attribute
    @XmlElement(name = "id") // Marks the id field as an XML attribute
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ID: " + id;
    }
}
