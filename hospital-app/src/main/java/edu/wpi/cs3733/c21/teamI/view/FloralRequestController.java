package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class FloralRequestController {
  @FXML private JFXTextField requesterID;
  @FXML private JFXTextField patientName;
  @FXML private JFXTextField roomNumber;
  @FXML private JFXTextArea details;
  @FXML private JFXTextField assignedEmployeeID;
  @FXML private JFXDatePicker date;
  @FXML private JFXTimePicker time;
  @FXML private JFXButton clearButton;
  @FXML private JFXButton submitButton;

  @FXML
  public void initialize() {}

  @FXML
  private void submitForm(ActionEvent actionEvent) {
    String rID, pName, roomNum, reqDetails, assignedEmployee, reqDate, reqTime;
    rID = pName = roomNum = reqDetails = assignedEmployee = reqDate = reqTime = " ";

    rID = requesterID.getText();
    pName = patientName.getText();
    roomNum = roomNumber.getText();
    reqDetails = details.getText();
    assignedEmployee = assignedEmployeeID.getText();
    if (date.getValue() != null) reqDate = date.getValue().toString();
    if (time.getValue() != null) reqTime = time.getValue().toString();

    System.out.println(
        "Requester ID: "
            + rID
            + "\nPatient Name: "
            + pName
            + "\nRoom Number: "
            + roomNum
            + "\nDevilery Details: "
            + reqDetails
            + "\nAssignedEmployee: "
            + assignedEmployee
            + "\nRequest Date: "
            + reqDate
            + "\nRequest Time: "
            + reqTime);
  }

  @FXML
  private void clearForm(ActionEvent actionEvent) {
    requesterID.clear();
    patientName.clear();
    roomNumber.clear();
    details.clear();
    assignedEmployeeID.clear();
    date.valueProperty().set(null);
    time.valueProperty().set(null);
  }
}
