package edu.wpi.cs3733.c21.teamI.ticket.view;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.NotificationManager;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.notification.Notification;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicketDataController;
import edu.wpi.cs3733.c21.teamI.ticket.ticketTypes.LaundryTicket;
import edu.wpi.cs3733.c21.teamI.user.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
    if (laundryPickupDate.getValue() != null) pickupDate = laundryPickupDate.getValue().toString();
    if (laundryPickupTime.getValue() != null) pickupTime = laundryPickupTime.getValue().toString();

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
      Notification notif =
          new Notification(
              AssignedID, "You have a new Laundry Request Ticket.", "String timestamp");
      NotificationManager.getInstance().addNotification(notif);
    } catch (Exception o) {
      System.out.println("Error" + o);
    }
  }

  public void checkFinished() {
    laundrySubmit.setDisable(
        laundryPickupDate.valueProperty().getValue() == null
            || laundryPickupTime.valueProperty().getValue() == null
            || laundryRequesterID.getText() == null
            || laundryRequesterID.getText().trim().length() <= 0
            || !checkEmployeeID(laundryAssigned.getText())
            || !checkLocation(laundryPickupLocation.getText()));
  }

  public boolean checkEmployeeID(String employeeText) {
    boolean check = false;
    for (Object req : requestAssignedList.getItems()) {
      check = check || employeeText.equals(req);
    }
    return check;
  }

  public boolean checkLocation(String loc) {
    boolean check = false;
    for (Object req : serviceLocationList.getItems()) {
      check = check || loc.equals(req);
    }
    return check;
  }

  public void initialize() {
    laundrySubmit.setDisable(true);
    ServiceTicketDataController.setupRequestView(
        background,
        serviceLocationList,
        requestAssignedList,
        laundryRequesterID,
        laundryAssigned,
        laundryPickupLocation);

    laundryPickupDate.setOnAction(eh);
    laundryPickupTime.setOnAction(eh);
    laundryPickupLocation.setOnAction(eh);
    laundryRequesterID.setOnAction(eh);
    laundryAssigned.setOnAction(eh);
    laundryCheckbox.setOnAction(eh);
    background.addEventHandler(MouseEvent.MOUSE_CLICKED, meh);
  }

  EventHandler<MouseEvent> meh =
      new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          checkFinished();
        }
      };

  EventHandler<ActionEvent> eh =
      new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          checkFinished();
        }
      };

  @FXML
  private void clearForm(ActionEvent actionEvent) {
    laundryRequestDetails.clear();
    laundryPickupDate.valueProperty().set(null);
    laundryPickupTime.valueProperty().set(null);
    laundryPickupLocation.clear();
    laundryRequesterID.clear();
    laundryAssigned.clear();
    laundryCheckbox.setSelected(false);
    checkFinished();
  }

  public void lookup(KeyEvent e) {
    ServiceTicketDataController.lookupNodes(e, serviceLocationList, laundryPickupLocation);
  }

  public void lookupUser(KeyEvent e) {
    ServiceTicketDataController.lookupUsernames(
        e, User.Permission.RESPOND_TO_LAUNDRY, requestAssignedList, laundryAssigned);
  }
}
