package edu.wpi.cs3733.c21.teamI.ticket.view;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicketDataController;
import edu.wpi.cs3733.c21.teamI.ticket.ticketTypes.ReligiousTicket;
import edu.wpi.cs3733.c21.teamI.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
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
  @FXML private JFXButton submitButton;
  @FXML private AnchorPane background;

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

    ServiceTicketDataController.setupRequestView(
        background,
        serviceLocationList,
        requestAssignedList,
        requesterID,
        assignedEmployeeID,
        requestLocation);
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
  }
}