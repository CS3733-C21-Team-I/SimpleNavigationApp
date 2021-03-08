package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.ComputerTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicketDataController;
import edu.wpi.cs3733.c21.teamI.user.User;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
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
    } catch (Exception o) {
      System.out.println("Error" + o);
    }
  }

  @FXML
  public void clear(javafx.event.ActionEvent Event) {
    compServReqID.clear();
    compServAssID.clear();
    compServReqLoc.clear();
    compServReqDes.clear();
    compServReqUrgent.setSelected(false);
  }

  private void setupRequestView() {
    serviceLocationList
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (ChangeListener<String>)
                (ov, oldVal, newVal) -> {
                  compServReqLoc.setText(newVal);
                  serviceLocationList.setVisible(false);
                });

    requestAssignedList
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (ChangeListener<String>)
                (ov, oldVal, newVal) -> {
                  compServAssID.setText(newVal);
                  requestAssignedList.setVisible(false);
                });

    background.setOnMouseClicked(
        t -> {
          serviceLocationList.setVisible(false);
          requestAssignedList.setVisible(false);
        });

    compServReqID.setText(ApplicationDataController.getInstance().getLoggedInUser().getName());
  }

  public void initialize() {

    ObservableList<String> requestTypeList =
        FXCollections.observableArrayList("Desktop", "Laptop", "Other");
    compType.setItems(requestTypeList);
    setupRequestView();
  }

  public void lookup(KeyEvent e) {
    ServiceTicketDataController.lookupNodes(e, serviceLocationList, compServReqLoc);
  }

  public void lookupUser(KeyEvent e) {
    ServiceTicketDataController.lookupUsernames(
        e, User.Permission.RESPOND_TO_COMPUTER, requestAssignedList, compServAssID);
  }
}