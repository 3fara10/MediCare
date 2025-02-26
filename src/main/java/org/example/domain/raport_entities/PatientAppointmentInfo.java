package org.example.domain.raport_entities;

import org.example.domain.Entity;
import org.example.domain.Patient;

public class PatientAppointmentInfo extends Entity {

    private Patient patient; // Patient object, could include patient details
    private long appointmentCount; // Number of appointments for the patient

    // Constructor
    public PatientAppointmentInfo(Patient patient, long appointmentCount) {
        super();
        this.patient = patient;
        this.appointmentCount = appointmentCount;
    }

    // Getters and Setters
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public long getAppointmentCount() {
        return appointmentCount;
    }

    public void setAppointmentCount(long appointmentCount) {
        this.appointmentCount = appointmentCount;
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public String toString() {
        // Improved toString for better readability
        return "Patient: " + (patient != null ? patient.getName() + " " + patient.getForename() : "Unknown")
                + ", Age: " + (patient != null ? patient.getAge() : "Unknown")
                + ", Appointments Count: " + appointmentCount;
    }
}
