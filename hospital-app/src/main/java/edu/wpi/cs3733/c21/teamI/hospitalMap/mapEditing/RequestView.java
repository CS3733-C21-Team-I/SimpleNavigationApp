package edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing;

import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RequestView extends Application {
  private ServiceTicket serviceTicket;
  private MapEditManager mapManager;
  private static MapEditManager ourManager;

  @FXML TextField type, ticketID, requestID, assignmentID, locationBox;
  @FXML TextArea description;
  @FXML Label header;
  @FXML CheckBox completed;

  public RequestView() {
    this.mapManager = ourManager;
  }

  public RequestView(MapEditManager mapManager, ServiceTicket serviceTicket) {
    this.mapManager = mapManager;
    this.serviceTicket = serviceTicket;
  }

  public static void saveManager() {
    ourManager = new MapEditManager().getInstance();
  }

  @Override
  public void init() throws Exception {}

  @Override
  public void start(Stage primaryStage) throws IOException {
    Group root = new Group();
    root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/RequestDisplay.fxml")));
    String title = serviceTicket.getTicketType().toString().toLowerCase();
    primaryStage.setTitle(
        title.substring(0, 1).toUpperCase()
            + title.substring(1)
            + " #"
            + serviceTicket.getTicketId());
    mapManager.setRoot(root);
    Scene applicationScene = new Scene(root, 973, 800);
    primaryStage.setScene(applicationScene);
    mapManager.setStage(primaryStage);
    primaryStage.show();
    System.out.println(root.getChildren());
    populatePage();
  }

  @FXML
  private void populatePage() {
    header.setText(
        serviceTicket.getTicketType().toString().toLowerCase().substring(0, 1).toUpperCase()
            + " #"
            + serviceTicket.getTicketId());
    type.setText(serviceTicket.getTicketType().toString());
    ticketID.setText(String.valueOf(serviceTicket.getTicketId()));
    requestID.setText(String.valueOf(serviceTicket.getRequestingUserID()));
    assignmentID.setText(String.valueOf(serviceTicket.getAssignedUserID()));
    locationBox.setText(serviceTicket.getLocation());
    description.setText(serviceTicket.getDescription());
    completed.setSelected(serviceTicket.isCompleted());
  }

  public void returnHome(ActionEvent e) throws IOException {
    Group root = (Group) ((Button) e.getSource()).getScene().getRoot();
    root.getChildren().clear();
    root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Login.fxml")));
  }
}
