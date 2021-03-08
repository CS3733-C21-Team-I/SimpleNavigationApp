package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.InternalTransportationTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicketDataController;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
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
  // boolean bEmergency = false;

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
      int id = ServiceTicketDatabaseManager.getInstance().addTicket(ticket);
      ServiceTicketDatabaseManager.getInstance().addEmployeeForTicket(id, AssignedID);
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
                  internalLocation.setText(newVal);
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

    requesterID.setText(ApplicationDataController.getInstance().getLoggedInUser().getName());
  }

  public void clear() {
    internalDetails.clear();
    internalLocation.clear();
    requestAssigned.clear();
    requesterID.clear();
    stretcherRadio.setSelected(false);
    wheelchairRadio.setSelected(false);
    //    emergency.setSelected(false);
    internalDate.valueProperty().set(null);
    internalTime.valueProperty().set(null);
    serviceLocationList.setVisible(false);
    requestAssignedList.setVisible(false);
  }

  public void initialize() {
    setupRequestView();
  }

  public void lookup(KeyEvent e) {
    ServiceTicketDataController.lookupNodes(e, serviceLocationList, internalLocation);
  }

  public void lookupUser(KeyEvent e) {
    ServiceTicketDataController.lookupUsernames(
        e, User.Permission.RESPOND_TO_INTERNAL, requestAssignedList, requestAssigned);
  }
}
