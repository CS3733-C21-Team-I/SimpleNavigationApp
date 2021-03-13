package edu.wpi.cs3733.c21.teamI.parking.reservations.view;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

public class ParkingReservationController {
    @FXML
    JFXButton clearBtn, bookBtn;
    @FXML
    JFXDatePicker entryDate, exitDate;
    @FXML
    JFXTimePicker entryTime, exitTime;
    @FXML
    JFXTextField contNum, plateNum;
    @FXML
    JFXCheckBox handiCheck;

    @FXML public void onClear(){
        entryDate.valueProperty().setValue(null);
        exitDate.valueProperty().setValue(null);
        exitTime.valueProperty().setValue(null);
        entryTime.valueProperty().setValue(null);
        contNum.clear();
        plateNum.clear();
        handiCheck.setSelected(false);
    }

    public void checkFinished() {
        bookBtn.setDisable(
                entryTime.valueProperty().getValue() == null
                || entryDate.valueProperty().getValue() == null
                || exitTime.valueProperty().getValue() == null
                || exitDate.valueProperty().getValue() == null
                        || contNum.getText() == null
                        || contNum.getText().trim().length() <= 0
                        || plateNum.getText() == null
                        || plateNum.getText().trim().length() <= 0);
    }

    public void initialize(){
        bookBtn.setDisable(false);
        entryTime.setOnAction(eh);
        entryDate.setOnAction(eh);
        exitTime.setOnAction(eh);
        exitDate.setOnAction(eh);
        contNum.setOnAction(eh);
        plateNum.setOnAction(eh);
        handiCheck.setOnAction(eh);
    }

    EventHandler<javafx.event.ActionEvent> eh =
            new EventHandler<javafx.event.ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    checkFinished();
                }
            };

}
