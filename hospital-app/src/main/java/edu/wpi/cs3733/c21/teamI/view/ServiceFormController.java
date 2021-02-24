package edu.wpi.cs3733.c21.teamI.view;

import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
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
  @FXML TextField sanAssignedID;
  @FXML HBox sanRequestType;
  @FXML TextField sanRequestID;
  @FXML VBox background;

  @FXML TextArea mainDesc;
  @FXML CheckBox mainEmerg;
  @FXML MenuButton mainRequestType;
  @FXML TextField mainAssignedID;
  @FXML TextField mainRequestID;
  @FXML ListView serviceLocationList;
  @FXML TextField requestLocation;
  @FXML MenuButton mainRequestMenu;

  @FXML
  public void createSanitationTicket(ActionEvent e) {
    try {
      int RequestID = Integer.parseInt(sanRequestID.getText());
      int AssignedID = Integer.parseInt(sanAssignedID.getText());
      sanitationTicket =
          new ServiceTicket(
              RequestID,
              AssignedID,
              ServiceTicket.TicketType.SANITATION,
              requestLocation.getText(),
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
      int RequestID = Integer.parseInt(mainRequestID.getText());
      System.out.println(RequestID);
      int AssignID = Integer.parseInt(mainAssignedID.getText());
      System.out.println(AssignID);

      maintenanceTicket =
          new ServiceTicket(
              RequestID,
              AssignID,
              ServiceTicket.TicketType.MAINTENANCE,
              requestLocation.getText(),
              mainDesc.getText(),
              mainEmerg.isSelected(),
              false);
      System.out.println(maintenanceTicket);
      //  ServiceTicketDatabaseManager.getInstance().addTicket(maintenanceTicket);

    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(" Error " + e);
    }
  }

  public void clearMaintenance() {
    mainDesc.clear();
    requestLocation.clear();
    mainRequestID.clear();
    mainAssignedID.clear();
    mainEmerg.setSelected(false);
  }

  public void clearSanitation() {
    sanDescription.clear();
    requestLocation.clear();
    sanRequestID.clear();
    sanAssignedID.clear();
    sanEmergency.setSelected(false);
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
    background.setOnMouseClicked(
        t -> {
          serviceLocationList.setVisible(false);
        });
  }

  @FXML
  public void initialize() {
    setupRequestView();
  }

  public void lookup(KeyEvent e) {
    ViewManager.lookupNodes(e, serviceLocationList, requestLocation);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}
}
