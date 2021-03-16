package edu.wpi.cs3733.c21.teamI.parking.reservations.view;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamI.database.ParkingPeripheralServerManager;
import edu.wpi.cs3733.c21.teamI.parking.reservations.ParkingCustomer;
import edu.wpi.cs3733.c21.teamI.parking.reservations.ParkingReservation;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ParkingReservationController {
  @FXML JFXButton clearBtn, bookBtn;
  @FXML JFXDatePicker entryDate, exitDate;
  @FXML JFXTimePicker entryTime, exitTime;
  @FXML JFXTextField contNum, plateNum;
  @FXML JFXCheckBox handiCheck;
  @FXML private Label parkingSlot, start, end, ticketID;
  @FXML VBox ticket;

  private static final DateTimeFormatter resTimeFormat =
      DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");

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

    boolean shouldDisable =
        entryTime.valueProperty().getValue() == null
            || entryDate.valueProperty().getValue() == null
            || exitTime.valueProperty().getValue() == null
            || exitDate.valueProperty().getValue() == null
            || contNum.getText().equals("")
            || contNum.getText().trim().length() <= 0
            || plateNum.getText().equals("")
            || plateNum.getText().trim().length() <= 0;

    bookBtn.setDisable(shouldDisable);
    System.out.println("CHECKING FINISHED: " + shouldDisable);
  }

  public void initialize() {
    bookBtn.setDisable(true);
    entryTime.setOnAction(eh);
    entryDate.setOnAction(eh);
    exitTime.setOnAction(eh);
    exitDate.setOnAction(eh);
    contNum.setOnAction(eh);
    plateNum.setOnAction(eh);
    handiCheck.setOnAction(eh);
    ticket.managedProperty().bind(ticket.visibleProperty());
    ticket.setVisible(false);
  }

  public void redrawTicket(ParkingReservation res) {
    parkingSlot.setText("Spot " + res.getSlotCode());
    start.setText(res.getStartTimestamp().toLocalDateTime().format(resTimeFormat));
    end.setText(res.getEndTimestamp().toLocalDateTime().format(resTimeFormat));
    ticketID.setText("Ticket ID " + res.getId());
    ticket.setVisible(true);
  }

  public void submit() {
    System.out.println("Reservation submitted");
    Timestamp startTimestamp = Timestamp.valueOf(entryDate.getValue().atTime(entryTime.getValue()));
    Timestamp exitTimestamp = Timestamp.valueOf(exitDate.getValue().atTime(exitTime.getValue()));

    ParkingCustomer customer =
        ParkingPeripheralServerManager.getInstance()
            .createNewCustomer(plateNum.getText(), false, contNum.getText());

    ParkingReservation reservation =
        ParkingPeripheralServerManager.getInstance()
            .createNewReservation(customer, startTimestamp, exitTimestamp);

    redrawTicket(reservation);
    onClear();
  }

  EventHandler<javafx.event.ActionEvent> eh =
      new EventHandler<javafx.event.ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          checkFinished();
        }
      };
}
