package edu.wpi.cs3733.c21.teamI.ticket.view;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicketDataController;
import edu.wpi.cs3733.c21.teamI.user.User;
import edu.wpi.cs3733.c21.teamI.view.ViewManager;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class SanitationController {
  ServiceTicket ticket;
  @FXML TextField sanitationLocation, requesterID, requestAssigned;
  @FXML TextArea sanitationDetails;
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
    } catch (Exception o) {
      System.out.println("Error" + o);
    }
  }

  public void navigate(ActionEvent e) throws IOException {
    ViewManager.navigate(e);
  }

  public void clear() {
    sanitationDetails.clear();
    sanitationLocation.clear();
    requestAssigned.clear();
    requesterID.clear();
    serviceLocationList.setVisible(false);
    requestAssignedList.setVisible(false);
  }

  public void initialize() {
    ServiceTicketDataController.setupRequestView(
        background,
        serviceLocationList,
        requestAssignedList,
        requesterID,
        requestAssigned,
        sanitationLocation);
  }

  public void lookup(KeyEvent e) {
    ServiceTicketDataController.lookupNodes(e, serviceLocationList, sanitationLocation);
  }

  public void lookupUser(KeyEvent e) {
    ServiceTicketDataController.lookupUsernames(
        e, User.Permission.RESPOND_TO_SANITATION, requestAssignedList, requestAssigned);
  }
}