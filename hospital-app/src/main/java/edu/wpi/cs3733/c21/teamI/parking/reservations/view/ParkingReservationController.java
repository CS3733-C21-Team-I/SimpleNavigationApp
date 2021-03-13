package edu.wpi.cs3733.c21.teamI.parking.reservations.view;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamI.database.ParkingPeripheralServerManager;
import edu.wpi.cs3733.c21.teamI.parking.reservations.ParkingCustomer;
import edu.wpi.cs3733.c21.teamI.parking.reservations.ParkingReservation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ParkingReservationController {
  @FXML JFXButton clearBtn, bookBtn;
  @FXML JFXDatePicker entryDate, exitDate;
  @FXML JFXTimePicker entryTime, exitTime;
  @FXML JFXTextField contNum, plateNum;
  @FXML JFXCheckBox handiCheck;
  @FXML
  private Label parkingSlot, start, end, price, barCode, ticketID;

  @FXML
  public void onClear() {
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

  public void initialize() {
    bookBtn.setDisable(false);
    entryTime.setOnAction(eh);
    entryDate.setOnAction(eh);
    exitTime.setOnAction(eh);
    exitDate.setOnAction(eh);
    contNum.setOnAction(eh);
    plateNum.setOnAction(eh);
    handiCheck.setOnAction(eh);
  }

  public void redrawTicket(ParkingReservation res) {
    parkingSlot.setText(res.getSlotCode());
    start.setText(String.valueOf(res.getStartTimestamp()));
    end.setText(String.valueOf(res.getEndTimestamp()));
//    price.setText(res.);
    ticketID.setText(String.valueOf(res.getId()));
  }

  public void submit() {
    System.out.println("Reservation submitted");
    Timestamp startTimestamp = Timestamp.valueOf(entryDate.getValue().atTime(entryTime.getValue()));
    Timestamp exitTimestamp = Timestamp.valueOf(exitDate.getValue().atTime(exitTime.getValue()));

    ParkingCustomer customer = ParkingPeripheralServerManager.getInstance().createNewCustomer(plateNum.getText(), false, contNum.getText());

    ParkingReservation reservation = ParkingPeripheralServerManager.getInstance().createNewReservation(customer, startTimestamp, exitTimestamp);

    redrawTicket(reservation);
  }

  EventHandler<javafx.event.ActionEvent> eh =
      new EventHandler<javafx.event.ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          checkFinished();
        }
      };
}
