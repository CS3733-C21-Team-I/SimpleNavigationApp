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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ServiceFormController extends Application {
  ServiceTicket sanitationTicket;
  ServiceTicket maintenanceTicket;
  Stage textStage;
  FXMLLoader textBoxLoader;
  Label textLabel;

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
              AssignedID,
              "Test",
              ServiceTicket.TicketType.SANITATION,
              NavDatabaseManager.getInstance().getMapIdFromLongName(requestLocation.getText()),
              sanDescription.getText(),
              sanEmergency.isSelected(),
              false);
      ServiceTicketDatabaseManager.getInstance().addTicket(sanitationTicket);
      System.out.println("service request sent");
      clearSanitation();
      showSuccess();

    } catch (Exception o) {
      System.out.println("Error" + o);
      showError();
    }
  }

  private void showError() {
    textLabel.setTextFill(Color.RED);
    textLabel.setText("Please enter valid information.");
    textStage.show();
  }

  private void showSuccess() {
    textLabel.setTextFill(Color.GREEN);
    textLabel.setText("Service Request Submitted.");
    textStage.show();
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
              "Test",
              ServiceTicket.TicketType.MAINTENANCE,
              NavDatabaseManager.getInstance().getMapIdFromLongName(requestLocation.getText()),
              mainDesc.getText(),
              mainEmerg.isSelected(),
              false);
      ServiceTicketDatabaseManager.getInstance().addTicket(maintenanceTicket);
      clearMaintenance();
      showSuccess();

    } catch (Exception e) {
      System.out.println(" Error " + e);
      showError();
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

  @FXML
  public void initialize() {
    setupRequestView();
    // dialog box code
    try {
      textBoxLoader = new FXMLLoader(getClass().getResource("/fxml/dialogBox.fxml"));
      AnchorPane textBoxRoot = textBoxLoader.load();

      textStage = new Stage();
      textStage.initModality(Modality.APPLICATION_MODAL);
      textStage.initStyle(StageStyle.UNDECORATED);
      textStage.setScene(new Scene(textBoxRoot));

      Button okBtn = (Button) textBoxLoader.getNamespace().get("okBtn");
      textLabel = (Label) textBoxLoader.getNamespace().get("textLabel");
      okBtn.setOnMouseClicked(
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
              textStage.hide();
            }
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
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
