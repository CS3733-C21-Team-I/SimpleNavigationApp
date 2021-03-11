package edu.wpi.cs3733.c21.teamI.ticket.view;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.NotificationManager;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.notification.Notification;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicketDataController;
import edu.wpi.cs3733.c21.teamI.ticket.ticketTypes.LanguageTicket;
import edu.wpi.cs3733.c21.teamI.user.User;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LanguageController extends Application {

  ServiceTicket ticket;
  @FXML private JFXTextField langTextfield;

  @FXML private JFXTextField langLocationTextfield;

  @FXML private JFXTimePicker langTime;

  @FXML private JFXTextField langReqID;

  @FXML private JFXTextField langAssignedEmp;

  @FXML private JFXTextArea langDetails;

  @FXML private JFXCheckBox langCheckbox;

  @FXML public JFXButton langSubmit;

  @FXML ListView serviceLocationList, requestAssignedList;

  @FXML AnchorPane background;

  @FXML
  void clear(ActionEvent event) {

    langTextfield.clear();
    langDetails.clear();
    langLocationTextfield.clear();
    langTime.setValue(null);
    langReqID.clear();
    langAssignedEmp.clear();
    langCheckbox.setSelected(false);
    checkFinished();
  }

  @FXML
  void submit(ActionEvent event) {
    String time = "";
    if (langTime.getValue() != null) time = langTime.getValue().toString();
    try {
      int RequestID = ApplicationDataController.getInstance().getLoggedInUser().getUserId();
      int AssignedID =
          UserDatabaseManager.getInstance()
              .getUserForScreenname(langAssignedEmp.getText())
              .getUserId();
      ticket =
          new LanguageTicket(
              RequestID,
              NavDatabaseManager.getInstance()
                  .getMapIdFromLongName(langLocationTextfield.getText()),
              langDetails.getText(),
              false,
              langTextfield.getText(),
              time,
              langCheckbox.isSelected());
      ticket.addAssignedUserID(AssignedID);
      int id = ServiceTicketDatabaseManager.getInstance().addTicket(ticket);
      ServiceTicketDatabaseManager.getInstance().addEmployeeForTicket(id, AssignedID);
      Notification notif =
          new Notification(
              AssignedID, "You have a new Language Request Ticket.", "String timestamp");
      NotificationManager.getInstance().addNotification(notif);
    } catch (Exception o) {
      System.out.println("Error" + o);
    }
  }

  public void checkFinished() {
    langSubmit.setDisable(
        langTime.valueProperty().getValue() == null
            || langReqID.getText() == null
            || langReqID.getText().trim().length() <= 0
            || langTextfield.getText() == null
            || langTextfield.getText().trim().length() <= 0
            || !checkEmployeeID(langAssignedEmp.getText())
            || !checkLocation(langLocationTextfield.getText()));
  }

  public boolean checkEmployeeID(String employeeText) {
    boolean check = false;
    for (Object req : requestAssignedList.getItems()) {
      check = check || employeeText.equals(req);
    }
    return check;
  }

  public boolean checkLocation(String loc) {
    boolean check = false;
    for (Object req : serviceLocationList.getItems()) {
      check = check || loc.equals(req);
    }
    return check;
  }

  public void initialize() {
    langSubmit.setDisable(true);

    ServiceTicketDataController.setupRequestView(
        background,
        serviceLocationList,
        requestAssignedList,
        langReqID,
        langAssignedEmp,
        langLocationTextfield);

    langTextfield.setOnAction(eh);
    langLocationTextfield.setOnAction(eh);
    langTime.setOnAction(eh);
    langReqID.setOnAction(eh);
    langAssignedEmp.setOnAction(eh);
    langCheckbox.setOnAction(eh);
    background.addEventHandler(MouseEvent.MOUSE_CLICKED, meh);
  }

  EventHandler<MouseEvent> meh =
      new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          checkFinished();
        }
      };

  EventHandler<ActionEvent> eh =
      new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          checkFinished();
        }
      };

  public void lookup(KeyEvent e) {
    ServiceTicketDataController.lookupNodes(e, serviceLocationList, langLocationTextfield);
  }

  public void lookupUser(KeyEvent e) {
    ServiceTicketDataController.lookupUsernames(
        e, User.Permission.RESPOND_TO_LANGUAGE, requestAssignedList, langAssignedEmp);
  }

  @Override
  public void start(Stage primaryStage) {}
}
