package edu.wpi.cs3733.c21.teamI.view;

import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import java.io.IOException;
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
  @FXML TextField sanRequestID;
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
              AssignedID,
              ServiceTicket.TicketType.SANITATION,
              NavDatabaseManager.getInstance().getMapIdFromLongName(requestLocation.getText()),
              sanDescription.getText(),
              sanEmergency.isSelected(),
              false);
      ServiceTicketDatabaseManager.getInstance().addTicket(sanitationTicket);
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
              AssignID,
              ServiceTicket.TicketType.MAINTENANCE,
              NavDatabaseManager.getInstance().getMapIdFromLongName(requestLocation.getText()),
              mainDesc.getText(),
              mainEmerg.isSelected(),
              false);
      System.out.println(maintenanceTicket);
      ServiceTicketDatabaseManager.getInstance().addTicket(maintenanceTicket);

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

    requestID.setText(
        ApplicationDataController.getInstance().getLoggedInUser().getName());
  }

  @FXML
  public void initialize() {
    setupRequestView();
  }

  public void lookup(KeyEvent e) {
    ViewManager.lookupNodes(e, serviceLocationList, requestLocation);
  }

  public void lookupUser(KeyEvent e) {
    ViewManager.lookupUsernames(e, requestAssignedList, requestAssigned);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}
}
