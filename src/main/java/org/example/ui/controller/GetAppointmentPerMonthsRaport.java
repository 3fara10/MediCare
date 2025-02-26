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
import org.example.domain.raport_entities.MonthAppointmentsInfo;

import java.util.ArrayList;
import java.util.List;

public class GetAppointmentPerMonthsRaport {
    @FXML
    private TableView<MonthAppointmentsInfo> dataTableView;
    List<MonthAppointmentsInfo> lista;
    private final ObservableList<MonthAppointmentsInfo> data = FXCollections.observableList(new ArrayList<>());
    @FXML
    public void init() {
        dataTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MonthAppointmentsInfo>() {
            @Override
            public void changed(ObservableValue<? extends MonthAppointmentsInfo> observableValue, MonthAppointmentsInfo square, MonthAppointmentsInfo t1) {
                ObservableList<MonthAppointmentsInfo> changes = dataTableView.getSelectionModel().getSelectedItems();
                MonthAppointmentsInfo selectedItem = changes.get(0);
            }
        });

        TableColumn<MonthAppointmentsInfo, String> yearMonthColum = new TableColumn<>("Year and month");
        yearMonthColum.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getYearMonth().toString()));

        TableColumn<MonthAppointmentsInfo, String> appointmentCount = new TableColumn<>("Appointment Count");
        appointmentCount.setCellValueFactory(x -> new SimpleStringProperty(Long.toString(x.getValue().getAppointmentCount())));

        dataTableView.getColumns().add(yearMonthColum);
        dataTableView.getColumns().add(appointmentCount);

        loadData();

    }
    public void setLista( List<MonthAppointmentsInfo> lista) {
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
