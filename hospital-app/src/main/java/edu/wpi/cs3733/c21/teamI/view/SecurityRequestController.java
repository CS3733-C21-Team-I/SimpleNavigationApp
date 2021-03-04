package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicketDataController;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class SecurityRequestController {
  ServiceTicket ticket;
  @FXML TextField locationText, requestID, requestAssigned;
  @FXML TextArea description;
  @FXML CheckBox emergency;
  @FXML ListView serviceLocationList, requestAssignedList;
  @FXML VBox background;

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
              ServiceTicket.TicketType.SECURITY,
              NavDatabaseManager.getInstance().getMapIdFromLongName(locationText.getText()),
              description.getText(),
              false);
      ticket.addAssignedUserID(AssignedID);
      ServiceTicketDatabaseManager.getInstance().addTicket(ticket);
      ServiceTicketDatabaseManager.getInstance().addEmployeeForTicket(RequestID, AssignedID);
    } catch (Exception o) {
      System.out.println("Error" + o);
    }
  }

  public void navigate(ActionEvent e) throws IOException {
    ViewManager.navigate(e);
  }

  private void setupRequestView() {
    serviceLocationList
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (ChangeListener<String>)
                (ov, oldVal, newVal) -> {
                  locationText.setText(newVal);
                  serviceLocationList.setVisible(false);
                });

    requestAssignedList
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (ChangeListener<String>)
                (ov, oldVal, newVal) -> {
                  requestAssigned.setText(newVal);
                  requestAssignedList.setVisible(false);
                });

    background.setOnMouseClicked(
        t -> {
          serviceLocationList.setVisible(false);
          requestAssignedList.setVisible(false);
        });

    requestID.setText(ApplicationDataController.getInstance().getLoggedInUser().getName());
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

  public void initialize() {
    setupRequestView();
  }

  public void lookup(KeyEvent e) {
    ServiceTicketDataController.lookupNodes(e, serviceLocationList, locationText);
  }

  public void lookupUser(KeyEvent e) {
    ServiceTicketDataController.lookupUsernames(
        e, User.Permission.RESPOND_TO_SECURITY, requestAssignedList, requestAssigned);
  }
}
