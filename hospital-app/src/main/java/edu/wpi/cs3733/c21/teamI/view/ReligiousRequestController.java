package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ReligiousRequestController {
  @FXML private JFXTextField requesterID;
  @FXML private JFXTextField patientName;
  @FXML private JFXTextField religiousDenomination;
  @FXML private JFXComboBox typeOfRequest;
  @FXML private JFXTextArea details;
  @FXML private JFXTextField requestlocation;
  @FXML private JFXTextField assignedEmployeeID;
  @FXML private JFXDatePicker date;
  @FXML private JFXTimePicker time;
  @FXML private JFXButton clearButton;
  @FXML private JFXButton submitButton;

  @FXML
  public void initialize() {
    ObservableList<String> requestTypeList =
        FXCollections.observableArrayList(
            "Request Chaplain",
            "Pre-operative Visit",
            "Blessing",
            "Communion",
            "End of Life Ritual",
            "Memorial Service",
            "Other");
    typeOfRequest.setItems(requestTypeList);
  }

  @FXML
  private void submitForm(ActionEvent actionEvent) {
    String rID,
        pName,
        rDenomination,
        requestType,
        requestDetails,
        loc,
        assignedEmployee,
        reqDate,
        reqTime;
    rID =
        pName =
            rDenomination =
                requestType = requestDetails = loc = assignedEmployee = reqDate = reqTime = "";

    rID = requesterID.getText();
    pName = patientName.getText();
    rDenomination = religiousDenomination.getText();
    if (typeOfRequest.getValue() != null) requestType = typeOfRequest.getValue().toString();
    requestDetails = details.getText();
    loc = requestlocation.getText();
    assignedEmployee = assignedEmployeeID.getText();
    if (date.getValue() != null) reqDate = date.getValue().toString();
    if (time.getValue() != null) reqTime = time.getValue().toString();

    System.out.println(
        rID
            + "\n"
            + pName
            + "\n"
            + rDenomination
            + "\n"
            + requestType
            + "\n"
            + requestDetails
            + "\n"
            + loc
            + "\n"
            + assignedEmployee
            + "\n"
            + reqDate
            + "\n"
            + reqTime);
  }

  @FXML
  private void clearForm(ActionEvent actionEvent) {
    requesterID.clear();
    patientName.clear();
    religiousDenomination.clear();
    typeOfRequest.valueProperty().set(null);
    details.clear();
    requestlocation.clear();
    assignedEmployeeID.clear();
    date.valueProperty().set(null);
    time.valueProperty().set(null);
  }
}
