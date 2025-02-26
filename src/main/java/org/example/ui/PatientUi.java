package org.example.ui;

import org.example.domain.Patient;
import org.example.domain.raport_entities.MonthAppointmentsInfo;
import org.example.domain.raport_entities.PatientAppointmentInfo;
import org.example.domain.raport_entities.PatientLastAppointmentInfo;
import org.example.exceptions.*;
import org.example.service.IPatientService;
import org.example.utils.ui.UiUtils;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class PatientUi {
    private final IPatientService patientService;
    private final Scanner scanner;

    public PatientUi(IPatientService patientService) {
        this.patientService = patientService;
        this.scanner = new Scanner(System.in);
    }

    public void patientMenuRun() {
        while (true) {
            try{
                patientMenu();
                System.out.println("*Enter your option: ");
                int option = scanner.nextInt();
                scanner.nextLine();
                switch (option) {
                    case 1:
                        addPatient();
                        break;
                    case 2:
                        deletePatient();
                        break;
                    case 3:
                        updatePatient();
                        break;
                    case 4:
                        findPatient();
                        break;
                    case 5:
                        viewAllPatients();
                        break;
                    case 6:
                        getAppointmentsPerPatientRaport();
                        break;
                    case 7:
                        getDaysSinceLastAppointmentRaport();
                        break;
                    case 8:
                        return;
                    default:
                        System.out.println("Invalid option");
                }
            }catch(InputMismatchException e){
                System.out.println("Invalid option");
                scanner.nextLine();
            }
        }
    }

    private void patientMenu() {
        System.out.println("Patient Menu");
        System.out.println("1. Add Patient");
        System.out.println("2. Delete Patient");
        System.out.println("3. Update Patient");
        System.out.println("4. Find Patient");
        System.out.println("5. View All Patients");
        System.out.println("6. Raport:Get appointments per patient");
        System.out.println("7. Raport: Get days since last appointment per patient:");
        System.out.println("8. Back to main menu");

    }

    private void getAppointmentsPerPatientRaport() {
        try{
            List<PatientAppointmentInfo> raport = this.patientService.getAppointmentsPerPatient();
            if (raport.isEmpty()) {
                System.out.println("No Appointments found.");
            } else {
                System.out.println("The busiest appointments: ");
                for (PatientAppointmentInfo x : raport) {
                    System.out.println(x);
                }
            }
        } catch (RepositoryException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getDaysSinceLastAppointmentRaport() {
        try{
            List<PatientLastAppointmentInfo> raport = this.patientService.getDaysSinceLastAppointment();
            if (raport.isEmpty()) {
                System.out.println("No Appointments found.");
            } else {
                System.out.println("The appointments raport per month: ");
                for (PatientLastAppointmentInfo x : raport) {
                    System.out.println(x);
                }
            }
        } catch (RepositoryException e) {
            System.out.println(e.getMessage());
        }
    }

    private void findPatient() {
        while (true) {
            try {
                int id = UiUtils.readingInt("Patient id");
                Patient patient = this.patientService.get(id);
                System.out.println(patient.toString());
                break;
            } catch (RepositoryException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void updatePatient() {
        while (true) {
            try {
                int id = UiUtils.readingInt("Patient id");
                String newName = UiUtils.readingString("Patient name");
                String newForename = UiUtils.readingString("Pacient forename");
                int newAge = UiUtils.readingInt("Patient age");

                this.patientService.update(id, newName, newForename, newAge);
                break;

            } catch (EntityException | RepositoryException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void viewAllPatients() {
            try {
                List<Patient> patients = this.patientService.getAll();
                if (patients.isEmpty()) {
                    System.out.println("No patients found.");
                } else {
                    System.out.println("The patients: ");
                    for (Patient patient : patients) {
                        System.out.println(patient);
                    }
                }
            } catch (RepositoryException e) {
                System.out.println(e.getMessage());
            }
    }

    private void addPatient() {
        while (true) {
            try {

                String name = UiUtils.readingString("Patient name");
                String forename = UiUtils.readingString("Patient forename");
                int age = UiUtils.readingInt("Patient age");

                this.patientService.add(name, forename, age);
                break;
            } catch (EntityException | RepositoryException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void deletePatient() {
        while (true) {
            try {
                int id = UiUtils.readingInt("Patient id");
                this.patientService.delete(id);
                break;
            } catch (RepositoryException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
