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
import edu.wpi.cs3733.c21.teamI.ticket.ticketTypes.ReligiousTicket;
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

public class ReligiousRequestController {
  ServiceTicket ticket;
  @FXML private JFXTextField requesterID;
  @FXML private JFXTextField patientName;
  @FXML private JFXTextField religiousDenomination;
  @FXML private JFXComboBox typeOfRequest;
  @FXML private JFXTextArea details;
  @FXML private JFXTextField requestLocation;
  @FXML private JFXTextField assignedEmployeeID;
  @FXML private JFXDatePicker date;
  @FXML private JFXTimePicker time;
  @FXML ListView serviceLocationList, requestAssignedList;
  @FXML private JFXButton clearButton;
  @FXML public JFXButton submitButton;
  @FXML private AnchorPane background;

  @FXML
  public void initialize() {
    submitButton.setDisable(true);

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

    ServiceTicketDataController.setupRequestView(
        background,
        serviceLocationList,
        requestAssignedList,
        requesterID,
        assignedEmployeeID,
        requestLocation);

    requesterID.setOnAction(eh);
    patientName.setOnAction(eh);
    religiousDenomination.setOnAction(eh);
    typeOfRequest.setOnAction(eh);
    requestLocation.setOnAction(eh);
    assignedEmployeeID.setOnAction(eh);
    date.setOnAction(eh);
    time.setOnAction(eh);
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
    submitButton.setDisable(
        date.valueProperty().getValue() == null
            || time.valueProperty().getValue() == null
            || typeOfRequest.valueProperty().getValue() == null
            || requesterID.getText() == null
            || requesterID.getText().trim().length() <= 0
            || patientName.getText() == null
            || patientName.getText().trim().length() <= 0
            || religiousDenomination.getText() == null
            || religiousDenomination.getText().trim().length() <= 0
            || !checkEmployeeID(assignedEmployeeID.getText())
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

  public void lookup(KeyEvent e) {
    ServiceTicketDataController.lookupNodes(e, serviceLocationList, requestLocation);
  }

  public void lookupUser(KeyEvent e) {
    ServiceTicketDataController.lookupUsernames(
        e, User.Permission.RESPOND_TO_RELIGIOUS, requestAssignedList, assignedEmployeeID);
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
    loc = requestLocation.getText();
    assignedEmployee = assignedEmployeeID.getText();
    if (date.getValue() != null) reqDate = date.getValue().toString();
    if (time.getValue() != null) reqTime = time.getValue().toString();

    try {
      int RequestID = ApplicationDataController.getInstance().getLoggedInUser().getUserId();
      int AssignedID =
          UserDatabaseManager.getInstance()
              .getUserForScreenname(assignedEmployeeID.getText())
              .getUserId();
      ticket =
          new ReligiousTicket(
              RequestID,
              NavDatabaseManager.getInstance().getMapIdFromLongName(requestLocation.getText()),
              details.getText(),
              false,
              pName,
              rDenomination,
              requestType,
              reqDate,
              reqTime);
      ticket.addAssignedUserID(AssignedID);
      int id = ServiceTicketDatabaseManager.getInstance().addTicket(ticket);
      ServiceTicketDatabaseManager.getInstance().addEmployeeForTicket(id, AssignedID);
      Notification notif =
          new Notification(
              AssignedID, "You have a new Religious Request Ticket.", "String timestamp");
      NotificationManager.getInstance().addNotification(notif);
    } catch (Exception o) {
      System.out.println("Error" + o);
    }
  }

  @FXML
  private void clearForm(ActionEvent actionEvent) {
    requesterID.clear();
    patientName.clear();
    religiousDenomination.clear();
    typeOfRequest.valueProperty().set(null);
    details.clear();
    requestLocation.clear();
    assignedEmployeeID.clear();
    date.valueProperty().set(null);
    time.valueProperty().set(null);
    checkFinished();
  }
}
