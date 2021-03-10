package edu.wpi.cs3733.c21.teamI.ticket.view;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicketDataController;
import edu.wpi.cs3733.c21.teamI.ticket.ticketTypes.InternalTransportationTicket;
import edu.wpi.cs3733.c21.teamI.user.User;
import edu.wpi.cs3733.c21.teamI.view.ViewManager;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class InternalTransportationController {
  ServiceTicket ticket;
  @FXML JFXDatePicker internalDate;
  @FXML JFXTimePicker internalTime;
  @FXML JFXTextField requesterID, requestAssigned, internalLocation, internalDestination;
  @FXML JFXTextArea internalDetails;
  @FXML JFXRadioButton stretcherRadio, wheelchairRadio;
  @FXML ListView requestAssignedList;
  @FXML ListView serviceLocationList;
  @FXML AnchorPane background;
  @FXML JFXButton clearBttn;
  @FXML JFXButton submitBttn;

  //  String sDate, time, sName, sDestination, sPickupLocation, ID, employee;
  //  boolean bStrecherRadio = false;
  //  boolean bWheelerRadio = true;
  //  boolean bEmergency = false;

  public void submit(ActionEvent e) {
    String inDate, inTime;
    inDate = inTime = "";
    if (internalDate.getValue() != null) inDate = internalDate.getValue().toString();
    if (internalTime.getValue() != null) inTime = internalTime.getValue().toString();

    System.out.println(inDate);
    try {
      int RequestID = ApplicationDataController.getInstance().getLoggedInUser().getUserId();
      int AssignedID =
          UserDatabaseManager.getInstance()
              .getUserForScreenname(requestAssigned.getText())
              .getUserId();
      ticket =
          new InternalTransportationTicket(
              RequestID,
              NavDatabaseManager.getInstance().getMapIdFromLongName(internalLocation.getText()),
              internalDetails.getText(),
              false,
              inDate,
              inTime,
              internalDestination.getText(),
              false,
              stretcherRadio.isSelected(),
              wheelchairRadio.isSelected());

      ticket.addAssignedUserID(AssignedID);
      System.out.println(ticket);
      int id = ServiceTicketDatabaseManager.getInstance().addTicket(ticket);
      ServiceTicketDatabaseManager.getInstance().addEmployeeForTicket(id, AssignedID);
    } catch (Exception o) {
      System.out.println("Error" + o);
    }
  }

  //
  public void checkFinished() {
    if (internalDate.valueProperty().getValue() != null
        && internalTime.valueProperty().getValue() != null
        && internalDestination.getText() != null
        && internalDestination.getText().trim().length() > 0
        && checkEmployeeID(requestAssigned.getText())
        && checkLocation(internalLocation.getText())
        && (stretcherRadio.isSelected() || wheelchairRadio.isSelected())
        && requesterID.getText() != null
        && requesterID.getText().trim().length() > 0) {
      submitBttn.setDisable(false);
    } else {
      submitBttn.setDisable(true);
    }
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

  EventHandler<ActionEvent> eh =
      new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          checkFinished();
        }
      };
  //

  public void navigate(ActionEvent e) throws IOException {
    ViewManager.navigate(e);
  }

  public void clear() {
    internalDetails.clear();
    internalLocation.clear();
    requestAssigned.clear();
    requesterID.clear();
    stretcherRadio.setSelected(false);
    wheelchairRadio.setSelected(false);
    internalDestination.clear();
    //    emergency.setSelected(false);
    internalDate.valueProperty().set(null);
    internalTime.valueProperty().set(null);
    serviceLocationList.setVisible(false);
    requestAssignedList.setVisible(false);
    checkFinished();
  }

  public void initialize() {
    submitBttn.setDisable(true);
    ServiceTicketDataController.setupRequestView(
        background,
        serviceLocationList,
        requestAssignedList,
        requesterID,
        requestAssigned,
        internalLocation);

    internalDate.setOnAction(eh);
    internalTime.setOnAction(eh);
    requesterID.setOnAction(eh);
    requestAssigned.setOnAction(eh);
    internalLocation.setOnAction(eh);
    internalDestination.setOnAction(eh);
    stretcherRadio.setOnAction(eh);
    wheelchairRadio.setOnAction(eh);
    background.addEventHandler(MouseEvent.MOUSE_CLICKED, meh);
  }

  EventHandler<MouseEvent> meh =
      new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          checkFinished();
        }
      };

  public void lookup(KeyEvent e) {
    ServiceTicketDataController.lookupNodes(e, serviceLocationList, internalLocation);
  }

  public void lookupUser(KeyEvent e) {
    ServiceTicketDataController.lookupUsernames(
        e, User.Permission.RESPOND_TO_INTERNAL, requestAssignedList, requestAssigned);
  }
}
