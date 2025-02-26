package org.example.ui.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.domain.Patient;
import org.example.domain.raport_entities.PatientLastAppointmentInfo;
import org.example.exceptions.RepositoryException;
import org.example.service.IPatientService;

import java.io.IOException;
import java.util.ArrayList;

public class PatientMenuController {
    @FXML
    private TableView<Patient> patientTableView;
    public Button addButton;
    public Button deleteButton;
    public Button updateButton;
    public Button findButton;
    public Button getDaysLastAppointmentRaportButton;
    public Button getAppointmentPerPatientRaportButton;
    public Button backMainMenuButton;
    public TextField idTextField;
    public TextField nameTextField;
    public TextField forenameTextField;
    public TextField ageTextField;


    //Non fxml objects
    private IPatientService patientService;
    private Stage mainStage;
    private ObservableList<Patient> data = FXCollections.observableList(new ArrayList<>());

    public void init() {
        patientTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Patient>() {
            @Override
            public void changed(ObservableValue<? extends Patient> observableValue, Patient square, Patient t1) {
                ObservableList<Patient> changes = patientTableView.getSelectionModel().getSelectedItems();
                Patient selectedItem = changes.get(0);

                idTextField.setText(Integer.toString(selectedItem.getId()));
                nameTextField.setText(selectedItem.getName());
                forenameTextField.setText(selectedItem.getForename());
                ageTextField.setText(Integer.toString(selectedItem.getAge()));

            }
        });

        TableColumn<Patient, String> idColumn = new TableColumn<>("Id");
        idColumn.setPrefWidth(121.42856788635254);
        idColumn.setCellValueFactory(patient -> new SimpleStringProperty(Integer.toString(patient.getValue().getId())));

        TableColumn<Patient, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setPrefWidth(221.42852783203125);
        nameColumn.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getName()));

        TableColumn<Patient, String> forenameColumn = new TableColumn<>("Forename");
        forenameColumn.setPrefWidth(200.85711669921875);
        forenameColumn.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getForename()));

        TableColumn<Patient, String> ageColumn = new TableColumn<>("Age");
        ageColumn.setPrefWidth(111.42852783203125);
        ageColumn.setCellValueFactory(patient -> new SimpleStringProperty(Integer.toString(patient.getValue().getAge())));


        patientTableView.getColumns().add(idColumn);
        patientTableView.getColumns().add(nameColumn);
        patientTableView.getColumns().add(forenameColumn);
        patientTableView.getColumns().add(ageColumn);

        loadPatientData();
    }

    public void addEvent(ActionEvent actionEvent) {
        try {
            idTextField.setDisable(true);
            String name = nameTextField.getText();
            String forename = forenameTextField.getText();
            int age = Integer.parseInt(ageTextField.getText());

            patientService.add(name, forename, age);
            data.add(patientService.getPatientByNameForenameAndAge(name,forename,age));
            idTextField.setDisable(false);
        } catch (NumberFormatException e) {
            showError("Invalid input");
        } catch (RepositoryException e) {
            showError(e.getMessage());
        }finally {

            clearTextFields();
        }
    }

    public void deleteEvent(ActionEvent actionEvent) {
        //probleme la delete
        try {
            Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();
            if (selectedPatient == null) {
                throw new RuntimeException("There is no patient selected");
            }
            patientService.delete(selectedPatient.getId());  // Delete via service
            data.remove(selectedPatient);  // Remove from the ObservableList
        } catch (NumberFormatException e) {
            showError("Invalid input");
        } catch (RepositoryException |RuntimeException e) {
            showError(e.getMessage());
        }finally {
            clearTextFields();
        }
    }

    public void updateEvent(ActionEvent actionEvent) {
        try {
            Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();
            if (selectedPatient == null) {
                throw new RuntimeException("There is no patient selected");
            }
            selectedPatient.setName(nameTextField.getText());
            selectedPatient.setForename(forenameTextField.getText());
            selectedPatient.setAge(Integer.parseInt(ageTextField.getText()));
            patientService.update(selectedPatient.getId(),nameTextField.getText(),forenameTextField.getText(),Integer.parseInt(ageTextField.getText()));  // Update via service
            patientTableView.refresh(); // Remove from the ObservableList
        } catch (NumberFormatException e) {
            showError("Invalid input");
        } catch (RepositoryException |RuntimeException e) {
            showError(e.getMessage());
        }finally {
            clearTextFields();
        }
    }

    public void findEvent(ActionEvent actionEvent) {
        try {
            // Get the ID from the TextField
            String idText = idTextField.getText();

            // Check if the ID is a valid integer
            int patientId = Integer.parseInt(idText);

            // Use the service to fetch the patient by ID
            Patient patient = patientService.get(patientId); // Assuming this method exists in your service

            if (patient != null) {
                // If the patient is found, select the patient in the table
                selectPatientInTable(patient);

                // Populate the text fields with the patient's details
                nameTextField.setText(patient.getName());
                forenameTextField.setText(patient.getForename());
                ageTextField.setText(Integer.toString(patient.getAge()));
            } else {
                // Show an error message if the patient was not found
                showError("Patient not found!");
            }
        } catch (NumberFormatException e) {
            // If ID is not a valid number, show an error message
            showError("Invalid ID format!");
        } catch (RepositoryException e) {
            // Handle repository exceptions (e.g., if something goes wrong in fetching the data)
            showError(e.getMessage());
        }
    }

    public void getDaysSinceLastAppointmentEvent(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/laborator04/GetDaysSinceLastAppointment.fxml"));

            // Load the FXML
            Parent root = loader.load();

            // Get the controller instance from FXMLLoader
            GetDaysSinceLastAppointmentController x = loader.getController();
            x.setLista(this.patientService.getDaysSinceLastAppointment());
            x.init();
            Stage newStage = new Stage();
            newStage.setTitle("Get the days since the last appointment per Patient Raport");
            newStage.setScene(new Scene(root, 848, 527));
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace(); // Handle exception appropriately
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    public void getAppointmentPerPatientEvent(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/laborator04/GetAppointmentPerPatient.fxml"));

            // Load the FXML
            Parent root = loader.load();

            // Get the controller instance from FXMLLoader
            GetAppointmentPerPatientController x = loader.getController();
            x.setLista(this.patientService.getAppointmentsPerPatient());
            x.init();
            Stage newStage = new Stage();
            newStage.setTitle("Get the appointments per Patient Raport");
            newStage.setScene(new Scene(root, 848, 527));
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace(); // Handle exception appropriately
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }


    public void backToMainEvent(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/laborator04/MainMenu.fxml"));
            Parent root = loader.load();

            MainMenuController mainMenuController = loader.getController();

            mainMenuController.setMainStage(mainStage);

            Scene scene = new Scene(root, 800, 600);
            mainStage.setScene(scene);

            mainStage.show();
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while loading the main menu.", ButtonType.OK).show();
        }
    }
    public void setPatientService(IPatientService patientService) {
        this.patientService = patientService;
    }

    private void loadPatientData() {
        try {
            patientTableView.setItems(data);
            data.addAll(this.patientService.getAll());
        } catch (RepositoryException e) {
            showError(e.getMessage());
        }
    }
    private void clearTextFields() {
        idTextField.clear();
        nameTextField.clear();
        forenameTextField.clear();
        ageTextField.clear();
    }

    // Show error alert
    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message, ButtonType.OK).show();
    }

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    private void selectPatientInTable(Patient patient) {
        for (Patient p : patientTableView.getItems()) {
            if (p.getId() == patient.getId()) {
                patientTableView.getSelectionModel().select(p);
                break;
            }
        }
    }
}

