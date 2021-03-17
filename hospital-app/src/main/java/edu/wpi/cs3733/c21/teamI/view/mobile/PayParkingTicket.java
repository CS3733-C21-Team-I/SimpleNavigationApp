package edu.wpi.cs3733.c21.teamI.view.mobile;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c21.teamI.database.ParkingPeripheralServerManager;
import edu.wpi.cs3733.c21.teamI.parking.reservations.ParkingSlip;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javax.swing.*;

public class PayParkingTicket {

  @FXML JFXButton submitBtn1, submitPayment;
  @FXML StackPane root;
  @FXML AnchorPane background;
  @FXML ImageView phone;
  @FXML AnchorPane screen;
  @FXML TextField parkingIDInput;
  @FXML VBox parkingInfo;
  @FXML Label parkingInfoLabel;
  @FXML Label locationLabel;
  @FXML Label startTimeLabel;
  @FXML Label endTimeLabel;
  @FXML Label ticketIDLabel;
  @FXML VBox paymentInfo;
  @FXML Label paymentInfoLabel;
  @FXML TextField firstName;
  @FXML TextField lastName;
  @FXML TextField cardNum;
  @FXML TextField cvv;
  @FXML TextField cardExpDate;
  @FXML TextField zipCode;

  private static final DateTimeFormatter resTimeFormat =
      DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");

  public void initialize() {
    parkingInfo.setVisible(false);
    parkingInfoLabel.setVisible(false);
    locationLabel.setVisible(false);
    startTimeLabel.setVisible(false);
    endTimeLabel.setVisible(false);
    ticketIDLabel.setVisible(false);

    parkingIDInput.setOnMouseClicked(e);

    paymentInfo.setVisible(false);
    paymentInfoLabel.setVisible(false);
    firstName.setVisible(false);
    lastName.setVisible(false);
    cardNum.setVisible(false);
    cvv.setVisible(false);
    cardExpDate.setVisible(false);
    zipCode.setVisible(false);

    firstName.setOnAction(eh);
    lastName.setOnAction(eh);
    cardNum.setOnAction(eh);
    cvv.setOnAction(eh);
    cardExpDate.setOnAction(eh);
    zipCode.setOnAction(eh);

    submitBtn1.setDisable(true);
    submitPayment.setDisable(true);
    lookupParking();
  }

  EventHandler<MouseEvent> e =
      new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          checkFinishedForTicketID();
        }
      };
  EventHandler<javafx.event.ActionEvent> eh =
      new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          checkFinishedForTransaction();
        }
      };

  public void goToPathfinding(ActionEvent e) throws IOException {
    root.getChildren().clear();
    root.getChildren()
        .add(FXMLLoader.load(getClass().getResource("/fxml/mobilePages/PathfindingMobile.fxml")));
  }

  public void checkFinishedForTicketID() {
    submitBtn1.setDisable(parkingIDInput.getText() == null);
  }

  public void checkFinishedForTransaction() {
    submitPayment.setDisable(
        firstName.getText() == null
            || lastName.getText() == null
            || cardNum.getText() == null
            || cvv.getText() == null
            || cardExpDate.getText() == null
            || zipCode.getText() == null);
  }

  @FXML
  public void lookupParking() {
    // Integer matchString = Integer.parseInt(parkingIDInput.getText());
    try {
      int idInput = Integer.parseInt(parkingIDInput.getText());

      List<Integer> ticketIDList = ParkingPeripheralServerManager.getInstance().getCurrentSlips();
      List<Integer> matches = new ArrayList<>();
      for (Integer ticketID : ticketIDList) {
        if (ticketID == idInput) {
          matches.add(ticketID);

          System.out.println("Ticket number" + ticketID + "is valid.");
          parkingInfo.setVisible(true);
          parkingInfoLabel.setVisible(true);
          locationLabel.setVisible(true);
          startTimeLabel.setVisible(true);
          endTimeLabel.setVisible(true);
          ticketIDLabel.setVisible(true);

          paymentInfo.setVisible(true);
          paymentInfoLabel.setVisible(true);
          firstName.setVisible(true);
          lastName.setVisible(true);
          cardNum.setVisible(true);
          cvv.setVisible(true);
          cardExpDate.setVisible(true);
          zipCode.setVisible(true);
          update(ticketID);
        } else System.out.println("Ticket number" + idInput + "is not valid.");
      }
    } catch (NumberFormatException e) {

    }
  }

  public void update(int i) {
    ParkingSlip a = ParkingPeripheralServerManager.getInstance().getSlipForId(i);
    locationLabel.setText("Parking Spot: " + a.getReservation().getSlotCode());
    startTimeLabel.setText(a.getEntryTimestamp().toLocalDateTime().format(resTimeFormat));
    endTimeLabel.setText(
        a.getReservation().getEndTimestamp().toLocalDateTime().format(resTimeFormat));
    ticketIDLabel.setText("Ticket ID: " + a.getId());
  }

  public void submit() throws IOException {}

  public void clear() {}

  @FXML
  public void exit(javafx.scene.input.MouseEvent e) {
    Stage stage = (Stage) ((Circle) e.getSource()).getScene().getWindow();
    stage.close();
  }
}
