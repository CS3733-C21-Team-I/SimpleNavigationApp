package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class InternalTransportationController extends Application {

  @FXML JFXDatePicker date;
  @FXML JFXTimePicker pickupTime;
  @FXML JFXTextField name, pickupLocation, destination, requestID, assignedEmployee;
  @FXML JFXRadioButton strecherRadio, wheelerRadio;
  @FXML JFXCheckBox emergencyCheckbox;
  @FXML JFXButton clearBttn;
  @FXML JFXButton submitBttn;

  String sDate, time, sName, sDestination, sPickupLocation, ID, employee;
  boolean bStrecherRadio = false;
  boolean bWheelerRadio = true;
  boolean bEmergency = false;

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("internalTransportationRequest.fxml"));
    primaryStage.setTitle("Service Form Request");
    Scene applicationScene = new Scene(root, 973, 800);
    primaryStage.setScene(applicationScene);
    primaryStage.show();
  }

  @FXML
  public void submit(ActionEvent e) {

    bStrecherRadio = false;
    bWheelerRadio = true;
    bEmergency = false;
    ToggleGroup radioButtons = new ToggleGroup();
    wheelerRadio.setToggleGroup(radioButtons);
    strecherRadio.setToggleGroup(radioButtons);
    wheelerRadio.setSelected(true);

    sName = name.getText();
    sDestination = destination.getText();
    sPickupLocation = pickupLocation.getText();
    employee = assignedEmployee.getText();
    ID = requestID.getText();

    if (date.getValue() != null) {
      sDate = date.getValue().toString();
    }
    if (pickupTime.getValue() != null) time = pickupTime.getValue().toString();
    if (wheelerRadio.isSelected()) bWheelerRadio = true;
    if (strecherRadio.isSelected()) bStrecherRadio = true;
    if (emergencyCheckbox.isSelected()) bEmergency = true;

    System.out.println(
        sName
            + "\n"
            + sDestination
            + "\n"
            + bStrecherRadio
            + "\n"
            + "\n"
            + sDate
            + "\n"
            + ID
            + "\n"
            + employee);
  }

  @FXML
  void clear(ActionEvent e) {
    name.clear();
    destination.clear();
    pickupLocation.clear();

    assignedEmployee.clear();
    requestID.clear();
    emergencyCheckbox.setSelected(false);
    wheelerRadio.setSelected(true);
    strecherRadio.setSelected(false);
    date.valueProperty().setValue(null);
    pickupTime.valueProperty().setValue(null);
  }
}
