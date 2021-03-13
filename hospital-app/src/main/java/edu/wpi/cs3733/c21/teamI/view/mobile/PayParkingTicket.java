package edu.wpi.cs3733.c21.teamI.view.mobile;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;

import edu.wpi.cs3733.c21.teamI.parking.reservations.ParkingSlip;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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
  @FXML Label locationLabel;
  @FXML Label startTimeLabel;
  @FXML Label endTimeLabel;
  @FXML Label priceLabel;
  @FXML Label ticketIDLabel;
  @FXML VBox paymentInfo;
  @FXML TextField firstName;
  @FXML TextField lastName;
  @FXML TextField cardNum;
  @FXML TextField cvv;
  @FXML TextField cardExpDate;
  @FXML TextField zipCode;

  public void initialize() {
    parkingInfo.setVisible(false);
    paymentInfo.setVisible(false);
    submitBtn1.setDisable(true);
    submitPayment.setDisable(true);
    lookupParking();
  }

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
    String matchString = parkingIDInput.getText();
     //java.util.List<String> ticketIDs = ParkingSlip.getId();

    System.out.println(matchString);
    //      //  for (String ticketID : ticketIDs) {
    //            if (ticketID.equals(matchString)) {
    //                System.out.println("Ticket number" + matchString + "is valid.");
                        parkingInfo.setVisible(true);
                        paymentInfo.setVisible(true);
                        update();
    //            }
    //            else System.out.println("Ticket number" + matchString + "is not valid.");
    //        }
  }
  public void update() {
//    locationLabel.setText("Western Parking" );//need location from ticket
//    startTimeLabel.setText("Start Time: " + slipID.getEntryTimestamp());
//    endTimeLabel.setText("End Time: " + slipID.g);
//    priceLabel.setText("Price: " + slipID.getBaseCost());
//    ticketIDLabel.setText(String.valueOf(slipID.getId()));
  }
  public void submit() throws IOException {}

  public void clear() {}

  @FXML
  public void exit(javafx.scene.input.MouseEvent e) {
    Stage stage = (Stage) ((Circle) e.getSource()).getScene().getWindow();
    stage.close();
  }
}
