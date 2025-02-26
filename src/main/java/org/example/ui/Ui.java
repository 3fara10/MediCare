package org.example.ui;

import org.example.service.IAppointmentService;
import org.example.service.IPatientService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Ui implements IUi {
    private final PatientUi patientUi;
    private final AppointmentUi appointmentUi;
    private final Scanner scanner;

    public Ui(IPatientService patientService, IAppointmentService appointmentService) {
        this.patientUi = new PatientUi(patientService);
        this.appointmentUi = new AppointmentUi(appointmentService);
        this.scanner = new Scanner(System.in);
    }


    //-----------------------------------------------------RUN MENU
    @Override
    public void run() {
        while (true) {
            try {
                mainMenu();
                System.out.println("*Enter your option: ");
                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        this.patientUi.patientMenuRun();
                        break;
                    case 2:
                        this.appointmentUi.appointmentMenuRun();
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Invalid option");
                }
            }catch(InputMismatchException e) {
                System.out.println("Invalid option");
                scanner.nextLine();
            }
        }
    }

    //------------------------------------------------------------MENU
    private void mainMenu() {
        System.out.println("Main Menu");
        System.out.println("1. Patient Menu");
        System.out.println("2. Appointment Menu");
        System.out.println("3. Exit");
    }
}
