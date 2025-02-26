package org.example.ui.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.domain.Appointment;
import org.example.exceptions.RepositoryException;
import org.example.service.IAppointmentService;
import org.example.service.IPatientService;
import org.example.utils.ReadingTypeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AppointmentMenuController {
    public TableView<Appointment> appointmentTableView;
    public TextField idTextField;
    public TextField idPatientTextField;
    public TextField dateTextField;
    public TextField purposeTextField;
    public Button addButton;
    public Button deleteButton;
    public Button updateButton;
    public Button findButton;
    public Button backMainMenuButton;
    public Button getAppointmentsPerMonth;
    public Button getBusiestMonthsEvent;

    //Non fxml objects
    private IAppointmentService appointmentService;
    private IPatientService patientService;
    private Stage mainStage;
    private ObservableList<Appointment> data = FXCollections.observableList(new ArrayList<>());

    public void init() {
        appointmentTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Appointment>() {
            @Override
            public void changed(ObservableValue<? extends Appointment> observableValue, Appointment square, Appointment t1) {
                ObservableList<Appointment> changes = appointmentTableView.getSelectionModel().getSelectedItems();
                Appointment selectedItem = changes.get(0);

                idTextField.setText(Integer.toString(selectedItem.getId()));
                idPatientTextField.setText(Integer.toString(selectedItem.getPatient().getId()));
                dateTextField.setText(ReadingTypeUtils.writingDate(selectedItem.getDate()));
                purposeTextField.setText(selectedItem.getPurposeAppointment());
            }
        });

        TableColumn<Appointment, String> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(x -> new SimpleStringProperty(Integer.toString(x.getValue().getId())));

        TableColumn<Appointment, String> idPatientColumn = new TableColumn<>("Id Patient");
        idPatientColumn.setCellValueFactory(x -> new SimpleStringProperty(Integer.toString(x.getValue().getPatient().getId())));

        TableColumn<Appointment, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getPatient().getForename()));

        TableColumn<Appointment, String> forenameColumn = new TableColumn<>("Forename");
        forenameColumn.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getPatient().getForename()));

        TableColumn<Appointment, String> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(x -> new SimpleStringProperty(Integer.toString(x.getValue().getPatient().getAge())));

        TableColumn<Appointment, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(x -> new SimpleStringProperty(ReadingTypeUtils.writingDate((x.getValue().getDate()))));

        TableColumn<Appointment, String> purposeColumn = new TableColumn<>("Purpose");
        purposeColumn.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getPurposeAppointment()));

        appointmentTableView.getColumns().add(idColumn);
        appointmentTableView.getColumns().add(idPatientColumn);
        appointmentTableView.getColumns().add(nameColumn);
        appointmentTableView.getColumns().add(forenameColumn);
        appointmentTableView.getColumns().add(ageColumn);
        appointmentTableView.getColumns().add(dateColumn);
        appointmentTableView.getColumns().add(purposeColumn);

        loadPatientData();
    }

    public void addEvent(ActionEvent actionEvent) {
        try {
            idTextField.setDisable(true);
            int idPatient = Integer.parseInt(idPatientTextField.getText());
            Date date=ReadingTypeUtils.readingDate(dateTextField.getText());
            String purpose = purposeTextField.getText();
            appointmentService.add(idPatient,date,purpose);
            data.add(appointmentService.findAppointment(idPatient,date,purpose));
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
            Appointment selected = appointmentTableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                throw new RuntimeException("There is no appointment selected");
            }
            appointmentService.delete(selected.getId());  // Delete via service
            data.remove(selected);  // Remove from the ObservableList
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
            Appointment selected = appointmentTableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                throw new RuntimeException("There is no appointment selected");
            }

            selected.setPatient(this.patientService.get(Integer.parseInt(idPatientTextField.getText())));
            selected.setDate(ReadingTypeUtils.readingDate(dateTextField.getText()));
            selected.setPurposeAppointment(purposeTextField.getText());
            this.appointmentService.update(selected.getId(),Integer.parseInt(idPatientTextField.getText()),ReadingTypeUtils.readingDate(dateTextField.getText()),purposeTextField.getText());  // Update via service
            appointmentTableView.refresh(); // Remove from the ObservableList
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
            String idText = idTextField.getText();
            int appointmentID = Integer.parseInt(idText);

            // Use the service to fetch the patient by ID
            Appointment appointment = appointmentService.get(appointmentID); // Assuming this method exists in your service

            if (appointment != null) {
                // If the patient is found, select the patient in the table
                selectAppointmentinTable(appointment);

                idTextField.setText(Integer.toString(appointment.getId()));
                idTextField.setText(Integer.toString(appointment.getPatient().getId()));
                dateTextField.setText(ReadingTypeUtils.writingDate(appointment.getDate()));
                purposeTextField.setText(appointment.getPurposeAppointment());
            } else {
                // Show an error message if the patient was not found
                showError("Appointment not found!");
            }
        } catch (NumberFormatException e) {
            // If ID is not a valid number, show an error message
            showError("Invalid ID format!");
        } catch (RepositoryException e) {
            // Handle repository exceptions (e.g., if something goes wrong in fetching the data)
            showError(e.getMessage());
        }
    }

    public void getAppointmentsPerMonthEvent(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/laborator04/GetAppointmentsPerMonths.fxml"));

            // Load the FXML
            Parent root = loader.load();

            // Get the controller instance from FXMLLoader
            GetAppointmentPerMonthsRaport x = loader.getController();
            x.setLista(this.appointmentService.getAppointmentsPerMonth());
            x.init();
            Stage newStage = new Stage();
            newStage.setTitle("Get appointments per month Raport");
            newStage.setScene(new Scene(root, 848, 527));
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace(); // Handle exception appropriately
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    public void getBusiestMonthsEvent(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/laborator04/GetBusiestMonths.fxml"));

            // Load the FXML
            Parent root = loader.load();

            // Get the controller instance from FXMLLoader
            GetAppointmentPerMonthsRaport x = loader.getController();
            x.setLista(this.appointmentService.getBusiestMonths());
            x.init();
            Stage newStage = new Stage();
            newStage.setTitle("Get the busiest months Raport");
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
    public void setAppointmentService(IAppointmentService appointmentService,IPatientService patientService) {
        this.appointmentService = appointmentService;
        this.patientService=patientService;
    }

    private void loadPatientData() {
        try {
            appointmentTableView.setItems(data);
            data.addAll(this.appointmentService.getAll());
        } catch (RepositoryException e) {
            showError(e.getMessage());
        }
    }
    private void clearTextFields() {
        idTextField.clear();
        idPatientTextField.clear();
        dateTextField.clear();
        purposeTextField.clear();
    }

    // Show error alert
    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message, ButtonType.OK).show();
    }

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    private void selectAppointmentinTable(Appointment patient) {
        for (Appointment p : appointmentTableView.getItems()) {
            if (p.getId() == patient.getId()) {
                appointmentTableView.getSelectionModel().select(p);
                break;
            }
        }
    }
}
