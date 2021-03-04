package edu.wpi.cs3733.c21.teamI.view;

import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicketDataController;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.io.IOException;
import java.util.List;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ServiceFormController extends Application {
  ServiceTicket sanitationTicket;
  ServiceTicket maintenanceTicket;

  @FXML CheckBox sanEmergency;
  @FXML TextArea sanDescription;
  @FXML HBox sanRequestType;
  @FXML VBox background;

  @FXML TextArea mainDesc;
  @FXML CheckBox mainEmerg;
  @FXML MenuButton mainRequestType;

  @FXML ListView serviceLocationList;
  @FXML TextField requestLocation;

  @FXML ListView requestAssignedList;
  @FXML TextField requestAssigned;
  @FXML TextField requestID;

  @FXML
  public void createSanitationTicket(ActionEvent e) {
    try {
      int RequestID = ApplicationDataController.getInstance().getLoggedInUser().getUserId();
      int AssignedID =
          UserDatabaseManager.getInstance()
              .getUserForScreenname(requestAssigned.getText())
              .getUserId();
      sanitationTicket =
          new ServiceTicket(
              RequestID,
              ServiceTicket.TicketType.SANITATION,
              NavDatabaseManager.getInstance().getMapIdFromLongName(requestLocation.getText()),
              sanDescription.getText(),
              false);
      ServiceTicketDatabaseManager.getInstance().addTicket(sanitationTicket);
      addEmployeeForTicket(RequestID, AssignedID);
    } catch (Exception o) {
      System.out.println("Error" + o);
    }
  }

  @FXML
  public void createMaintenanceTicket(ActionEvent o) {
    try {
      int RequestID = ApplicationDataController.getInstance().getLoggedInUser().getUserId();
      int AssignID =
          UserDatabaseManager.getInstance()
              .getUserForScreenname(requestAssigned.getText())
              .getUserId();

      maintenanceTicket =
          new ServiceTicket(
              RequestID,
              ServiceTicket.TicketType.MAINTENANCE,
              NavDatabaseManager.getInstance().getMapIdFromLongName(requestLocation.getText()),
              mainDesc.getText(),
              false);
      ServiceTicketDatabaseManager.getInstance().addTicket(maintenanceTicket);
      addEmployeeForTicket(RequestID, AssignID);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(" Error " + e);
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
                  requestLocation.setText(newVal);
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
  }

  public void clearMaintenance() {
    mainDesc.clear();
    requestLocation.clear();
    requestID.clear();
    requestAssigned.clear();
    mainEmerg.setSelected(false);
  }

  public void clearSanitation() {
    sanDescription.clear();
    requestLocation.clear();
    requestID.clear();
    requestAssigned.clear();
    sanEmergency.setSelected(false);
  }

  public void initialize() {
    setupRequestView();
  }

  public void lookup(KeyEvent e) {
    ServiceTicketDataController.lookupNodes(e, serviceLocationList, requestLocation);
  }

  public void lookupUserSan(KeyEvent e) {
    ServiceTicketDataController.lookupUsernames(
        e, User.Permission.RESPOND_TO_SANITATION, requestAssignedList, requestAssigned);
  }

  public void lookupUserMai(KeyEvent e) {
    ServiceTicketDataController.lookupUsernames(
        e, User.Permission.RESPOND_TO_MAINTENANCE, requestAssignedList, requestAssigned);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}

  public void addEmployeeForTicket(int requestID, int assignID) {
    try {
      List<ServiceTicket> tixLs =
          ServiceTicketDatabaseManager.getInstance().getTicketsForRequestId(requestID);
      for (ServiceTicket cur : tixLs) {
        int tixID = cur.getTicketId();
        ServiceTicketDatabaseManager.getInstance().addEmployee(tixID, assignID);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
