package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.LanguageTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicketDataController;
import edu.wpi.cs3733.c21.teamI.user.User;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
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
    } catch (Exception o) {
      System.out.println("Error" + o);
    }
  }

  private void setupRequestView() {
    serviceLocationList
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (ChangeListener<String>)
                (ov, oldVal, newVal) -> {
                  langLocationTextfield.setText(newVal);
                  serviceLocationList.setVisible(false);
                });

    requestAssignedList
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (ChangeListener<String>)
                (ov, oldVal, newVal) -> {
                  langAssignedEmp.setText(newVal);
                  requestAssignedList.setVisible(false);
                });

    background.setOnMouseClicked(
        t -> {
          serviceLocationList.setVisible(false);
          requestAssignedList.setVisible(false);
        });

    langReqID.setText(ApplicationDataController.getInstance().getLoggedInUser().getName());
  }

  public void initialize() {
    setupRequestView();
  }

  public void lookup(KeyEvent e) {
    ServiceTicketDataController.lookupNodes(e, serviceLocationList, langLocationTextfield);
  }

  public void lookupUser(KeyEvent e) {
    ServiceTicketDataController.lookupUsernames(
        e, User.Permission.RESPOND_TO_LANGUAGE, requestAssignedList, langAssignedEmp);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}
}
