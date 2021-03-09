package edu.wpi.cs3733.c21.teamI.ticket.view;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicketDataController;
import edu.wpi.cs3733.c21.teamI.ticket.ticketTypes.AudioVisualTicket;
import edu.wpi.cs3733.c21.teamI.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
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
  Button clearBtn;
  Button submitBtn;

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
  }

  public void initialize() {
    /*TODO common stuff*/
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
  }

  public void lookup(KeyEvent e) {
    ServiceTicketDataController.lookupNodes(e, serviceLocationList, roomNumber);
  }

  public void lookupUser(KeyEvent e) {
    ServiceTicketDataController.lookupUsernames(
        e, User.Permission.RESPOND_TO_AV, requestAssignedList, requestAssigned);
  }
}