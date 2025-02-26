package org.example.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.xml.bind.annotation.*;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Represents an Appointment entity with patient, date, and purpose of the Appointment.
 * Each Appointment has a unique id inherited from Entity superclass.
 */
@JsonTypeName("appointment")
@XmlRootElement(name = "appointment")
@XmlType(propOrder = {"id", "patient", "date", "purposeAppointment"})
@javax.persistence.Entity
@Table(name="Appointment")
public class Appointment extends Entity implements Serializable {

    @ManyToOne
    @JoinColumn(name="patient_id", nullable = false)
    private Patient patient;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date",nullable = false)
    private Date date;

    @Column(name="purpose",length=100)
    private String purposeAppointment;

    @Serial
    private static final long serialVersionUID = 1L;

    // Default constructor for JAXB
    public Appointment() {}

    // Constructor to initialize fields
    public Appointment(Patient patient, Date date, String purposeAppointment) {
        super(); // Initialize unique id from the Entity class
        this.patient = patient;
        this.date = date;
        this.purposeAppointment = purposeAppointment;
    }

    public Appointment(int id, Patient patient, Date date, String purposeAppointment) {
        super(); // Initialize unique id from the Entity class
        this.id = id;
        this.patient = patient;
        this.date = date;
        this.purposeAppointment = purposeAppointment;
    }

    // Getter and Setter for Patient
    @XmlElement(name = "patient") // Marks the patient field as an XML element
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    // Getter and Setter for Purpose of Appointment
    @XmlElement(name = "purposeAppointment") // Marks the purposeAppointment field as an XML element
    public String getPurposeAppointment() {
        return purposeAppointment;
    }

    public void setPurposeAppointment(String purposeAppointment) {
        this.purposeAppointment = purposeAppointment;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // Optionally add @XmlAttribute for 'id' field if you want to serialize it as an attribute
    @XmlAttribute(name = "id") // Marks the id as an XML attribute instead of an element
    @Override
    public int getId() {
        return super.getId();
    }

    public Date getDate() {
        return date;
    }
    @Override
    public String toString() {
        return super.toString() + ", Patient: " + patient + ", Date: " + date + ", The purpose of the appointment: " + purposeAppointment;
    }
}
