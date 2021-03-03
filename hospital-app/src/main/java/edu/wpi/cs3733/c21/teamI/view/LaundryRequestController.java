package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LaundryRequestController {
  @FXML JFXTextArea laundryRequestDetails;
  @FXML JFXDatePicker laundryPickupDate;
  @FXML JFXTimePicker laundryPickupTime;
  @FXML JFXTextField laundryPickupLocation;
  @FXML JFXTextField laundryRequesterID;
  @FXML JFXTextField laundryAssigned;
  @FXML JFXCheckBox laundryCheckbox;
  @FXML JFXButton laundryCancel;
  @FXML JFXButton laundryClear;
  @FXML JFXButton laundrySubmit;

  @FXML
  public void initialize() {}

  @FXML
  private void submitForm(ActionEvent actionEvent) {
    String requestDetails,
        pickupDate = null,
        pickupTime = null,
        pickupLocation,
        requesterID,
        assignedEmployee,
        dryClean = null;

    requestDetails = laundryAssigned.getText();
    pickupLocation = laundryPickupLocation.getText();
    requesterID = laundryRequesterID.getText();
    dryClean = String.valueOf(laundryCheckbox.isSelected());
    assignedEmployee = laundryAssigned.getText();
    if (laundryPickupDate.getValue() != null) pickupDate = laundryPickupDate.getValue().toString();
    if (laundryPickupTime.getValue() != null) pickupTime = laundryPickupTime.getValue().toString();

    System.out.println(
        requestDetails
            + "\n"
            + pickupDate
            + "\n"
            + pickupTime
            + "\n"
            + pickupLocation
            + "\n"
            + requesterID
            + "\n"
            + assignedEmployee
            + "\n"
            + dryClean);
  }

  @FXML
  private void clearForm(ActionEvent actionEvent) {
    laundryRequestDetails.clear();
    laundryPickupDate.valueProperty().set(null);
    laundryPickupTime.valueProperty().set(null);
    laundryPickupLocation.clear();
    laundryRequesterID.clear();
    laundryAssigned.clear();
    laundryCheckbox.setSelected(false);
  }
}
