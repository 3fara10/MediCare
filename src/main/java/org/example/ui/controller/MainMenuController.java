package org.example.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.example.MainMenu;
import org.example.domain.Appointment;
import org.example.domain.Patient;
import org.example.repository.IRepository;
import org.example.service.*;

import java.io.IOException;

public class MainMenuController {
    private IPatientService patientService;
    private IAppointmentService appointmentService;
    private Stage mainStage;

    @FXML
    private void initialize() {
        IRepository<Patient> patientIRepository = MainMenu.getRepository(Patient.class);
        IRepository<Appointment> appointmentIRepository = MainMenu.getRepository(Appointment.class);
        this.patientService = new PatientService(patientIRepository, appointmentIRepository);
        this.appointmentService = new AppointmentService(appointmentIRepository, patientIRepository);

    }

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    @FXML
    private void handlePatientMenu(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();

        // Load the FXML and get the controller
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/laborator04/PatientMenu.fxml"));

            // Load the FXML
            Parent root = loader.load();

            // Get the controller instance from FXMLLoader
            PatientMenuController patientMenuController = loader.getController();
            patientMenuController.setPatientService(patientService);
            patientMenuController.init();
            patientMenuController.setMainStage(stage);
            // Check if controller is null (this is for debugging)

            // Pass the patientService to the controller


            // Create a new stage and set the scene
            Stage newStage = new Stage();
            newStage.setTitle("Patient Menu");
            newStage.setScene(new Scene(root, 848, 527));
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace(); // Handle exception appropriately
        }
    }

    @FXML
    private void handleAppointmentMenu(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();

        // Load the FXML and get the controller
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/laborator04/AppointmentMenu.fxml"));

            // Load the FXML
            Parent root = loader.load();

            // Get the controller instance from FXMLLoader
            AppointmentMenuController appointmentMenuController = loader.getController();
            appointmentMenuController.setAppointmentService(appointmentService,patientService);
            appointmentMenuController.init();
            appointmentMenuController.setMainStage(stage);
            Stage newStage = new Stage();
            newStage.setTitle("Appointment Menu");
            newStage.setScene(new Scene(root, 848, 527));
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace(); // Handle exception appropriately
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        // Close the application
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void openWindow(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/laborator04/" + fxmlFile));
            AnchorPane root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
