package edu.wpi.cs3733.c21.teamI.ticket.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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

public class SanitationController {
  ServiceTicket ticket;
  @FXML TextField sanitationLocation, requesterID, requestAssigned;
  @FXML TextArea sanitationDetails;
  @FXML ListView serviceLocationList, requestAssignedList;
  @FXML AnchorPane background;
  @FXML JFXButton compServReqSubmit;

  @FXML JFXComboBox securityType;

  public void submit(ActionEvent e) {
    try {
      int RequestID = ApplicationDataController.getInstance().getLoggedInUser().getUserId();
      int AssignedID =
          UserDatabaseManager.getInstance()
              .getUserForScreenname(requestAssigned.getText())
              .getUserId();
      ticket =
          new ServiceTicket(
              RequestID,
              ServiceTicket.TicketType.SANITATION,
              NavDatabaseManager.getInstance().getMapIdFromLongName(sanitationLocation.getText()),
              sanitationDetails.getText(),
              false);
      //      ticket.addAssignedUserID(AssignedID);
      ticket.addAssignedUserID(AssignedID);
      int id = ServiceTicketDatabaseManager.getInstance().addTicket(ticket);
      ServiceTicketDatabaseManager.getInstance().addEmployeeForTicket(id, AssignedID);
      Notification notif =
          new Notification(
              AssignedID, "You have a new Sanitation Request Ticket.", "String timestamp");
      NotificationManager.getInstance().addNotification(notif);
    } catch (Exception o) {
      System.out.println("Error" + o);
    }
  }

  public void clear() {
    sanitationDetails.clear();
    sanitationLocation.clear();
    requestAssigned.clear();
    requesterID.clear();
    serviceLocationList.setVisible(false);
    requestAssignedList.setVisible(false);
    checkFinished();
  }

  public void initialize() {
    compServReqSubmit.setDisable(true);

    ServiceTicketDataController.setupRequestView(
        background,
        serviceLocationList,
        requestAssignedList,
        requesterID,
        requestAssigned,
        sanitationLocation);

    requesterID.setOnAction(eh);
    sanitationLocation.setOnAction(eh);
    requestAssigned.setOnAction(eh);
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

  public void checkFinished() {
    compServReqSubmit.setDisable(
        requesterID.getText() == null
            || requesterID.getText().trim().length() <= 0
            || !checkEmployeeID(requestAssigned.getText())
            || !checkLocation(sanitationLocation.getText()));
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

  public void lookup(KeyEvent e) {
    ServiceTicketDataController.lookupNodes(e, serviceLocationList, sanitationLocation);
  }

  public void lookupUser(KeyEvent e) {
    ServiceTicketDataController.lookupUsernames(
        e, User.Permission.RESPOND_TO_SANITATION, requestAssignedList, requestAssigned);
  }
}
