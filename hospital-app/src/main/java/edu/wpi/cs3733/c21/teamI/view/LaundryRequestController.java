package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.LaundryTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicketDataController;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class LaundryRequestController {
  ServiceTicket ticket;
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
  @FXML AnchorPane background;

  @FXML ListView serviceLocationList, requestAssignedList;

  @FXML
  private void submitForm(ActionEvent actionEvent) {
     String pickupDate, pickupTime;
     pickupDate = pickupTime = "";
     if (laundryPickupDate.getValue() != null)
       pickupDate = laundryPickupDate.getValue().toString();
     if (laundryPickupTime.getValue() != null)
       pickupTime = laundryPickupTime.getValue().toString();

    try {
      int RequestID = ApplicationDataController.getInstance().getLoggedInUser().getUserId();
      int AssignedID =
          UserDatabaseManager.getInstance()
              .getUserForScreenname(laundryAssigned.getText())
              .getUserId();
      ticket =
          new LaundryTicket(
              RequestID,
              NavDatabaseManager.getInstance()
                  .getMapIdFromLongName(laundryPickupLocation.getText()),
              laundryRequestDetails.getText(),
              false,
              pickupDate,
              pickupTime,
              laundryCheckbox.isSelected());
      ticket.addAssignedUserID(AssignedID);
      int id = ServiceTicketDatabaseManager.getInstance().addTicket(ticket);
      ServiceTicketDatabaseManager.getInstance().addEmployeeForTicket(id, AssignedID);
    } catch (Exception o) {
      System.out.println("Error" + o);
    }
  }

  public void navigate(ActionEvent e) throws IOException {
    ViewManager.navigate(e);
  }

  private void setupRequestView() {
    serviceLocationList
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (ChangeListener<String>)
                (ov, oldVal, newVal) -> {
                  laundryPickupLocation.setText(newVal);
                  serviceLocationList.setVisible(false);
                });

    requestAssignedList
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (ChangeListener<String>)
                (ov, oldVal, newVal) -> {
                  laundryAssigned.setText(newVal);
                  requestAssignedList.setVisible(false);
                });

    background.setOnMouseClicked(
        t -> {
          serviceLocationList.setVisible(false);
          requestAssignedList.setVisible(false);
        });

    laundryRequesterID.setText(ApplicationDataController.getInstance().getLoggedInUser().getName());
  }

  public void initialize() {
    setupRequestView();
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

  public void lookup(KeyEvent e) {
    ServiceTicketDataController.lookupNodes(e, serviceLocationList, laundryPickupLocation);
  }

  public void lookupUser(KeyEvent e) {
    ServiceTicketDataController.lookupUsernames(
        e, User.Permission.RESPOND_TO_LAUNDRY, requestAssignedList, laundryAssigned);
  }
}
