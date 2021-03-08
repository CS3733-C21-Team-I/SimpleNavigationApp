package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.SecurityTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicketDataController;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class SecurityRequestController {
  ServiceTicket ticket;
  @FXML TextField locationText, requestID, requestAssigned;
  @FXML TextArea description;
  @FXML CheckBox emergency;
  @FXML ListView serviceLocationList, requestAssignedList;
  @FXML AnchorPane background;

  @FXML JFXComboBox securityType;

  public void submit(ActionEvent e) {
    try {
      int RequestID = ApplicationDataController.getInstance().getLoggedInUser().getUserId();
      int AssignedID =
          UserDatabaseManager.getInstance()
              .getUserForScreenname(requestAssigned.getText())
              .getUserId();
      // System.out.println(AssignedID);
      ticket =
          new SecurityTicket(
              RequestID,
              NavDatabaseManager.getInstance().getMapIdFromLongName(locationText.getText()),
              description.getText(),
              false,
              (String) securityType.getSelectionModel().getSelectedItem(),
              emergency.isSelected());
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

  public void initialize() {
    ServiceTicketDataController.setupRequestView(
        background,
        serviceLocationList,
        requestAssignedList,
        requestID,
        requestAssigned,
        locationText);

    securityType.getItems().addAll("Police Officer", "On-site Security Employee", "Other");
    securityType.getSelectionModel().select("2");
  }

  public void clear() {
    description.clear();
    locationText.clear();
    requestAssigned.clear();
    emergency.setSelected(false);
    serviceLocationList.setVisible(false);
    requestAssignedList.setVisible(false);
    securityType.getSelectionModel().select(2);
  }

  public void lookup(KeyEvent e) {
    ServiceTicketDataController.lookupNodes(e, serviceLocationList, locationText);
  }

  public void lookupUser(KeyEvent e) {
    ServiceTicketDataController.lookupUsernames(
        e, User.Permission.RESPOND_TO_SECURITY, requestAssignedList, requestAssigned);
  }
}
