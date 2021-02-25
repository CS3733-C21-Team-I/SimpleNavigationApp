package edu.wpi.cs3733.c21.teamI.view;

import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RequestDisplayController extends Application {

  private ServiceTicket serviceTicket;
  private String title;

  @FXML TextField type, ticketID, requestID, assignmentID, locationBox;
  @FXML TextArea description;
  @FXML Label header;
  @FXML CheckBox completed;

  public void navigate(ActionEvent e) throws IOException {
    ViewManager.navigate(e);
  }

  public RequestDisplayController() {}

  @FXML
  public void initialize() {
    populatePage();
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    // For whoever knows how to set page titles
    //    primaryStage.setTitle(
    //        title.substring(0, 1).toUpperCase()
    //            + title.substring(1)
    //            + " #"
    //            + serviceTicket.getTicketId());
  }

  @FXML
  private void populatePage() {
    completed
        .selectedProperty()
        .addListener(
            (observable, oldValue, newValue) ->
                ServiceTicketDatabaseManager.getInstance()
                    .updateTicket(serviceTicket.getTicketId()));

    serviceTicket = ViewManager.getServiceTicketToShow();
    this.title = serviceTicket.getTicketType().toString().toLowerCase();
    header.setText(
        title.substring(0, 1).toUpperCase()
            + title.substring(1)
            + " ("
            + serviceTicket.getDescription()
            + ")");
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
  }
}
