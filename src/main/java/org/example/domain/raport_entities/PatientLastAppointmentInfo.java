package org.example.domain.raport_entities;

import org.example.domain.Entity;
import org.example.domain.Patient;

import java.util.Date;

public class PatientLastAppointmentInfo extends Entity {
    private Patient patient;
    private Date lastAppointmentDate;
    private long daysSinceLastAppointment;

    public PatientLastAppointmentInfo(Patient patient, Date lastAppointmentDate, long daysSinceLastAppointment) {
        super();
        this.patient = patient;
        this.lastAppointmentDate = lastAppointmentDate;
        this.daysSinceLastAppointment = daysSinceLastAppointment;
    }

    public Patient getPatient() {
        return patient;
    }

    public Date getLastAppointmentDate() {
        return lastAppointmentDate;
    }

    public long getDaysSinceLastAppointment() {
        return daysSinceLastAppointment;
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public String toString() {
        return super.toString() + ", Name: " + this.patient.getName() + ", Forename: " + this.patient.getForename() + ", Age: " + this.patient.getAge() +", Last Appointment: " + lastAppointmentDate+ " ,Days Since Last Appointment: " + daysSinceLastAppointment;
    }
}