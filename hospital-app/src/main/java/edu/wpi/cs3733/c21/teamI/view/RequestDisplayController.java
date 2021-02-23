package edu.wpi.cs3733.c21.teamI.view;

import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RequestDisplayController extends Application {

  private ServiceTicket serviceTicket;
  private AnchorPane root;
  private String title;

  @FXML TextField type, ticketID, requestID, assignmentID, locationBox;
  @FXML TextArea description;
  @FXML Label header;
  @FXML CheckBox completed;

  public void navigate(ActionEvent e) throws IOException {
    ViewManager.navigate(e);
  }

  public RequestDisplayController(ServiceTicket serviceTicket) {
    this.serviceTicket = serviceTicket;
  }

  @Override
  public void init() throws Exception {}

  @Override
  public void start(Stage primaryStage) throws IOException {
    this.root = FXMLLoader.load(getClass().getResource("/fxml/RequestDisplay.fxml"));
    root.getChildren().add(this.root);
    this.title = serviceTicket.getTicketType().toString().toLowerCase();
    primaryStage.setTitle(
        title.substring(0, 1).toUpperCase()
            + title.substring(1)
            + " #"
            + serviceTicket.getTicketId());
    Scene applicationScene = new Scene(root, 973, 800);
    primaryStage.setScene(applicationScene);
    primaryStage.show();
    populatePage();
  }

  @FXML
  private void populatePage() {
    // use fxml IDs
    Label header = (Label) root.getChildren().get(0);
    VBox vbox = (VBox) root.getChildren().get(2);
    TextField type = (TextField) vbox.getChildren().get(1);
    TextField ticketID = (TextField) vbox.getChildren().get(3);
    TextField requestID = (TextField) vbox.getChildren().get(5);
    TextField assignmentID = (TextField) vbox.getChildren().get(7);
    TextField locationBox = (TextField) vbox.getChildren().get(9);
    TextArea description = (TextArea) vbox.getChildren().get(11);
    CheckBox completed = (CheckBox) vbox.getChildren().get(12);

    header.setText(
        title.substring(0, 1).toUpperCase()
            + title.substring(1)
            + " #"
            + serviceTicket.getTicketId());
    type.setText(serviceTicket.getTicketType().toString());
    ticketID.setText(String.valueOf(serviceTicket.getTicketId()));
    requestID.setText(
        String.valueOf(
            UserDatabaseManager.getInstance()
                .getDisplayNameForId(serviceTicket.getRequestingUserID())));
    assignmentID.setText(
        String.valueOf(
            UserDatabaseManager.getInstance()
                .getDisplayNameForId(serviceTicket.getAssignedUserID())));
    locationBox.setText(serviceTicket.getLocation());
    description.setText(serviceTicket.getDescription());
    completed.setSelected(serviceTicket.isCompleted());
  }

  public void returnHome(ActionEvent e) throws IOException {
    Group root = (Group) ((Button) e.getSource()).getScene().getRoot();
    root.getChildren().clear();
    root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Profile.fxml")));
    root.lookup("#loginVBox").setVisible(false);
    root.lookup("#serviceDisplay").setVisible(true);
  }
}
