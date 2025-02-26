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
import org.example.domain.Appointment;
import org.example.domain.Patient;
import org.example.domain.raport_entities.PatientAppointmentInfo;
import org.example.domain.raport_entities.PatientLastAppointmentInfo;
import org.example.utils.ReadingTypeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetDaysSinceLastAppointmentController {
    @FXML
    private TableView<PatientLastAppointmentInfo> dataTableView;
    List<PatientLastAppointmentInfo> lista;
    private final ObservableList<PatientLastAppointmentInfo> data = FXCollections.observableList(new ArrayList<>());
    @FXML
    public void init() {
        dataTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PatientLastAppointmentInfo>() {
            @Override
            public void changed(ObservableValue<? extends PatientLastAppointmentInfo> observableValue, PatientLastAppointmentInfo square, PatientLastAppointmentInfo t1) {
                ObservableList<PatientLastAppointmentInfo> changes = dataTableView.getSelectionModel().getSelectedItems();
                PatientLastAppointmentInfo selectedItem = changes.get(0);
            }
        });
        TableColumn<PatientLastAppointmentInfo, String> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(patient -> new SimpleStringProperty(Integer.toString(patient.getValue().getId())));

        TableColumn<PatientLastAppointmentInfo, String> idPatientColumn = new TableColumn<>("Id Patient");
        idPatientColumn.setCellValueFactory(patient -> new SimpleStringProperty(Integer.toString(patient.getValue().getPatient().getId())));

        TableColumn<PatientLastAppointmentInfo, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getPatient().getName()));

        TableColumn<PatientLastAppointmentInfo, String> forenameColumn = new TableColumn<>("Forename");
        forenameColumn.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getPatient().getName()));

        TableColumn<PatientLastAppointmentInfo, String> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(patient -> new SimpleStringProperty(Integer.toString(patient.getValue().getPatient().getAge())));

        TableColumn<PatientLastAppointmentInfo, String> dateColumn = new TableColumn<>("Date");
        ageColumn.setCellValueFactory(x -> new SimpleStringProperty(ReadingTypeUtils.writingDate((x.getValue().getLastAppointmentDate()))));

        TableColumn<PatientLastAppointmentInfo, String> daysLastAppointment = new TableColumn<>("Days since last appointment");
        daysLastAppointment.setCellValueFactory(x -> new SimpleStringProperty(Long.toString(x.getValue().getDaysSinceLastAppointment())));

        dataTableView.getColumns().add(idColumn);
        dataTableView.getColumns().add(idPatientColumn);
        dataTableView.getColumns().add(nameColumn);
        dataTableView.getColumns().add(forenameColumn);
        dataTableView.getColumns().add(ageColumn);
        dataTableView.getColumns().add(dateColumn);
        dataTableView.getColumns().add(daysLastAppointment);

        loadData();

    }
    public void setLista( List<PatientLastAppointmentInfo> lista) {
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
