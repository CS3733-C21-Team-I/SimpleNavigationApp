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
import edu.wpi.cs3733.c21.teamI.ticket.ticketTypes.ComputerTicket;
import edu.wpi.cs3733.c21.teamI.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ComputerServiceRequestController {
  ServiceTicket ticket;
  @FXML JFXTextArea compServReqDes;
  @FXML JFXToggleButton compServReqUrgent;
  @FXML JFXTextField compServReqID;
  @FXML JFXTextField compServReqLoc;
  @FXML JFXTextField compServAssID;
  @FXML JFXComboBox compType;
  @FXML ListView serviceLocationList, requestAssignedList;
  @FXML AnchorPane background;
  @FXML JFXButton compServReqSubmit;

  @FXML
  public void getComputerServiceRequest(javafx.event.ActionEvent Event) {
    try {
      int RequestID = ApplicationDataController.getInstance().getLoggedInUser().getUserId();
      int AssignedID =
          UserDatabaseManager.getInstance()
              .getUserForScreenname(compServAssID.getText())
              .getUserId();
      String requestType = "";
      if (compType.getValue() != null) requestType = compType.getValue().toString();
      ticket =
          new ComputerTicket(
              RequestID,
              NavDatabaseManager.getInstance().getMapIdFromLongName(compServReqLoc.getText()),
              compServReqDes.getText(),
              false,
              requestType,
              compServReqUrgent.isSelected());
      ticket.addAssignedUserID(AssignedID);
      int id = ServiceTicketDatabaseManager.getInstance().addTicket(ticket);
      ServiceTicketDatabaseManager.getInstance().addEmployeeForTicket(id, AssignedID);
      Notification notif =
          new Notification(
              AssignedID, "You have a new Computer Service Request Ticket.", "String timestamp");
      NotificationManager.getInstance().addNotification(notif);
    } catch (Exception o) {
      System.out.println("Error" + o);
    }
  }

  @FXML
  public void clear(javafx.event.ActionEvent Event) {
    compServReqID.clear();
    compServAssID.clear();
    compServReqLoc.clear();
    compType.valueProperty().set(null);
    compServReqDes.clear();
    compServReqUrgent.setSelected(false);
    checkFinished();
  }

  public void checkFinished() {
    compServReqSubmit.setDisable(
        compType.valueProperty().getValue() == null
            || compServReqID.getText() == null
            || compServReqID.getText().trim().length() <= 0
            || !checkEmployeeID(compServAssID.getText())
            || !checkLocation(compServReqLoc.getText()));
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
    compServReqSubmit.setDisable(true);
    ObservableList<String> requestTypeList =
        FXCollections.observableArrayList("Desktop", "Laptop", "Other");
    compType.setItems(requestTypeList);
    ServiceTicketDataController.setupRequestView(
        background,
        serviceLocationList,
        requestAssignedList,
        compServReqID,
        compServAssID,
        compServReqLoc);

    compServReqUrgent.setOnAction(eh);
    compServReqID.setOnAction(eh);
    compServReqLoc.setOnAction(eh);
    compServAssID.setOnAction(eh);
    compType.setOnAction(eh);
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
    ServiceTicketDataController.lookupNodes(e, serviceLocationList, compServReqLoc);
  }

  public void lookupUser(KeyEvent e) {
    ServiceTicketDataController.lookupUsernames(
        e, User.Permission.RESPOND_TO_COMPUTER, requestAssignedList, compServAssID);
  }
}
