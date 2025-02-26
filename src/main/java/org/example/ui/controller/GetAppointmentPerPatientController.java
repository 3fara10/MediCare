package org.example.ui.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.domain.Patient;
import org.example.domain.raport_entities.PatientAppointmentInfo;

import java.util.ArrayList;
import java.util.List;

public class GetAppointmentPerPatientController {
    @FXML
    private TableView<PatientAppointmentInfo> dataTableView;
    List<PatientAppointmentInfo> lista;
    private final ObservableList<PatientAppointmentInfo> data = FXCollections.observableList(new ArrayList<>());
    @FXML
    public void init() {
        dataTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PatientAppointmentInfo>() {
            @Override
            public void changed(ObservableValue<? extends PatientAppointmentInfo> observableValue, PatientAppointmentInfo square, PatientAppointmentInfo t1) {
                ObservableList<PatientAppointmentInfo> changes = dataTableView.getSelectionModel().getSelectedItems();
                PatientAppointmentInfo selectedItem = changes.get(0);
            }
        });
        TableColumn<PatientAppointmentInfo, String> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(patient -> new SimpleStringProperty(Integer.toString(patient.getValue().getId())));

        TableColumn<PatientAppointmentInfo, String> idPatientColumn = new TableColumn<>("Id Patient");
        idPatientColumn.setCellValueFactory(patient -> new SimpleStringProperty(Integer.toString(patient.getValue().getPatient().getId())));

        TableColumn<PatientAppointmentInfo, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getPatient().getName()));

        TableColumn<PatientAppointmentInfo, String> forenameColumn = new TableColumn<>("Forename");
        forenameColumn.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getPatient().getName()));

        TableColumn<PatientAppointmentInfo, String> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(patient -> new SimpleStringProperty(Integer.toString(patient.getValue().getPatient().getAge())));

        TableColumn<PatientAppointmentInfo, String> appointmentCount = new TableColumn<>("Appointment Count");
        appointmentCount.setCellValueFactory(x -> new SimpleStringProperty(Long.toString(x.getValue().getAppointmentCount())));

        dataTableView.getColumns().add(idColumn);
        dataTableView.getColumns().add(idPatientColumn);
        dataTableView.getColumns().add(nameColumn);
        dataTableView.getColumns().add(forenameColumn);
        dataTableView.getColumns().add(ageColumn);
        dataTableView.getColumns().add(appointmentCount);

        loadData();

    }
    public void setLista( List<PatientAppointmentInfo> lista) {
        this.lista=lista;
    }
    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message, ButtonType.OK).show();
    }
    private void loadData() {
        dataTableView.setItems(data);
        data.addAll(this.lista);
    }
}
