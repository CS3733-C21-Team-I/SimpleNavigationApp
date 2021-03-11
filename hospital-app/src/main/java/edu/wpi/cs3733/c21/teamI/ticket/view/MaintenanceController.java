package edu.wpi.cs3733.c21.teamI.ticket.view;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.NotificationManager;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.notification.Notification;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicketDataController;
import edu.wpi.cs3733.c21.teamI.user.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MaintenanceController {
  ServiceTicket ticket;
  @FXML TextField requestLocation, requestID, requestAssigned;
  @FXML TextArea mainDesc;
  @FXML CheckBox mainEmerg;
  @FXML ListView serviceLocationList, requestAssignedList;
  @FXML JFXButton mainSubmit;
  @FXML AnchorPane background;

  public void submit(ActionEvent e) {
    try {
      int RequestID = ApplicationDataController.getInstance().getLoggedInUser().getUserId();
      int AssignedID =
          UserDatabaseManager.getInstance()
              .getUserForScreenname(requestAssigned.getText())
              .getUserId();
      System.out.println(AssignedID);
      ticket =
          new ServiceTicket(
              RequestID,
              ServiceTicket.TicketType.MAINTENANCE,
              NavDatabaseManager.getInstance().getMapIdFromLongName(requestLocation.getText()),
              mainDesc.getText(),
              false);
      ticket.addAssignedUserID(AssignedID);
      int id = ServiceTicketDatabaseManager.getInstance().addTicket(ticket);
      ServiceTicketDatabaseManager.getInstance().addEmployeeForTicket(id, AssignedID);
      Notification notif =
          new Notification(
              AssignedID, "You have a new Maintenance Request Ticket.", "String timestamp");
      NotificationManager.getInstance().addNotification(notif);
    } catch (Exception o) {
      System.out.println("Error" + o);
    }
  }

  public void clear() {
    mainDesc.clear();
    requestLocation.clear();
    requestAssigned.clear();
    mainEmerg.setSelected(false);
    serviceLocationList.setVisible(false);
    requestAssignedList.setVisible(false);
    checkFinished();
  }

  public void initialize() {
    mainSubmit.setDisable(true);

    ServiceTicketDataController.setupRequestView(
        background,
        serviceLocationList,
        requestAssignedList,
        requestID,
        requestAssigned,
        requestLocation);

    requestLocation.setOnAction(eh);
    requestID.setOnAction(eh);
    requestAssigned.setOnAction(eh);
    mainEmerg.setOnAction(eh);
    background.addEventHandler(MouseEvent.MOUSE_CLICKED, meh);
  }

  public void checkFinished() {
    mainSubmit.setDisable(
        requestID.getText() == null
            || requestID.getText().trim().length() <= 0
            || !checkEmployeeID(requestAssigned.getText())
            || !checkLocation(requestLocation.getText()));
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

  public void lookup(KeyEvent e) {
    ServiceTicketDataController.lookupNodes(e, serviceLocationList, requestLocation);
  }

  public void lookupUser(KeyEvent e) {
    ServiceTicketDataController.lookupUsernames(
        e, User.Permission.RESPOND_TO_MAINTENANCE, requestAssignedList, requestAssigned);
  }
}
