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
import edu.wpi.cs3733.c21.teamI.ticket.ticketTypes.AudioVisualTicket;
import edu.wpi.cs3733.c21.teamI.user.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class AudioVisualRequestController {
  ServiceTicket ticket;
  @FXML JFXTextField patientName;
  @FXML JFXTextField roomNumber;
  @FXML JFXComboBox typeRequested;
  @FXML JFXTextArea requestDetails;
  @FXML JFXTextField requesterID;
  @FXML JFXTextField requestAssigned;
  @FXML ListView serviceLocationList;
  @FXML ListView requestAssignedList;
  @FXML AnchorPane background;
  @FXML JFXButton clearBtn;
  @FXML JFXButton submitBtn;

  @FXML
  public void submit(ActionEvent e) {
    try {
      int RequestID = ApplicationDataController.getInstance().getLoggedInUser().getUserId();
      int AssignedID =
          UserDatabaseManager.getInstance()
              .getUserForScreenname(requestAssigned.getText())
              .getUserId();
      ticket =
          new AudioVisualTicket(
              RequestID,
              NavDatabaseManager.getInstance().getMapIdFromLongName(roomNumber.getText()),
              requestDetails.getText(),
              false,
              patientName.getText(),
              (String) typeRequested.getSelectionModel().getSelectedItem());
      ticket.addAssignedUserID(AssignedID);
      int id = ServiceTicketDatabaseManager.getInstance().addTicket(ticket);
      ServiceTicketDatabaseManager.getInstance().addEmployeeForTicket(id, AssignedID);
      Notification notif =
          new Notification(
              AssignedID, "You have a new AudioVisual Request Ticket.", "String timestamp");
      NotificationManager.getInstance().addNotification(notif);
    } catch (Exception o) {
      System.out.println("Error" + o);
    }
  }

  @FXML
  public void clear(javafx.event.ActionEvent Event) {
    patientName.clear();
    roomNumber.clear();
    typeRequested.getSelectionModel().select(null);
    requestDetails.clear();
    requestAssigned.clear();
    requesterID.clear();
    checkFinished();
  }

  public void checkFinished() {
    submitBtn.setDisable(
        typeRequested.valueProperty().getValue() == null
            || patientName.getText() == null
            || patientName.getText().trim().length() <= 0
            || roomNumber.getText() == null
            || roomNumber.getText().trim().length() <= 0
            || requesterID.getText() == null
            || requesterID.getText().trim().length() <= 0
            || !checkLocation(roomNumber.getText())
            || !checkEmployeeID(requestAssigned.getText()));
  }

  public boolean checkLocation(String loc) {
    boolean check = false;
    for (Object req : serviceLocationList.getItems()) {
      check = check || loc.equals(req);
    }
    return check;
  }

  public boolean checkEmployeeID(String employeeText) {
    boolean check = false;
    for (Object req : requestAssignedList.getItems()) {
      check = check || employeeText.equals(req);
    }
    return check;
  }

  public void initialize() {
    /*TODO common stuff*/
    submitBtn.setDisable(true);
    typeRequested.setPromptText("Type of Media Requested");
    ServiceTicketDataController.setupRequestView(
        background,
        serviceLocationList,
        requestAssignedList,
        requesterID,
        requestAssigned,
        roomNumber);
    typeRequested.getItems().addAll("Headphones", "Monitor", "Other");
    typeRequested.getSelectionModel().select("2");

    patientName.setOnAction(eh);
    roomNumber.setOnAction(eh);
    typeRequested.setOnAction(eh);
    requesterID.setOnAction(eh);
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

  public void lookup(KeyEvent e) {
    ServiceTicketDataController.lookupNodes(e, serviceLocationList, roomNumber);
  }

  public void lookupUser(KeyEvent e) {
    ServiceTicketDataController.lookupUsernames(
        e, User.Permission.RESPOND_TO_AV, requestAssignedList, requestAssigned);
  }
}
