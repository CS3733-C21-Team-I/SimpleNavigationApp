package edu.wpi.cs3733.c21.teamI.ticket.view;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicketDataController;
import edu.wpi.cs3733.c21.teamI.ticket.ticketTypes.MedicineTicket;
import edu.wpi.cs3733.c21.teamI.user.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MedicineDeliveryController {
  @FXML JFXTextField patient_name, currentID, assignedID, drug, dose, locationText;
  @FXML JFXDatePicker date;
  @FXML JFXTimePicker time;
  @FXML TextArea comment;
  @FXML JFXCheckBox checkNote;
  @FXML JFXButton clearBtn, submitBtn, cancelBtn;
  @FXML ListView serviceLocationList, requestAssignedList;
  @FXML AnchorPane background;
  ServiceTicket ticket;

  @FXML
  public void submit(ActionEvent e) {
    String patientName, drugPicked, dosePicked, datePicked, timePicked, locationPicked, com, check;
    patientName = patient_name.getText();
    drugPicked = drug.getText();
    dosePicked = dose.getText();
    datePicked = date.getValue().toString();
    timePicked = time.getValue().toString();
    locationPicked = NavDatabaseManager.getInstance().getMapIdFromLongName(locationText.getText());
    com = comment.getText();
    if (checkNote.isSelected()) {
      check = "want notification";
    } else {
      check = "don't want notification";
    }

    try {
      int RequestID = ApplicationDataController.getInstance().getLoggedInUser().getUserId();
      int AssignedID =
          UserDatabaseManager.getInstance().getUserForScreenname(assignedID.getText()).getUserId();
      ticket =
          new MedicineTicket(
              RequestID,
              locationPicked,
              com,
              false,
              patientName,
              drugPicked,
              dosePicked,
              datePicked,
              timePicked);

      ticket.addAssignedUserID(AssignedID);
      int id = ServiceTicketDatabaseManager.getInstance().addTicket(ticket);
      ServiceTicketDatabaseManager.getInstance().addEmployeeForTicket(id, AssignedID);
    } catch (Exception o) {
      System.out.println("Error" + o);
    }
  }
  //
  public void checkFinished() {
    if (date.valueProperty().getValue() != null
        && time.valueProperty().getValue() != null
        && patient_name.getText() != null
        && patient_name.getText().trim().length() > 0
        && currentID.getText() != null
        && currentID.getText().trim().length() > 0
        && drug.getText() != null
        && drug.getText().trim().length() > 0
        && dose.getText() != null
        && dose.getText().trim().length() > 0
        && checkEmployeeID(assignedID.getText())
        && checkLocation(locationText.getText())) {
      submitBtn.setDisable(false);
    } else {
      submitBtn.setDisable(true);
    }
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
    submitBtn.setDisable(true);
    ServiceTicketDataController.setupRequestView(
        background, serviceLocationList, requestAssignedList, currentID, assignedID, locationText);
    patient_name.setOnAction(eh);
    currentID.setOnAction(eh);
    assignedID.setOnAction(eh);
    drug.setOnAction(eh);
    dose.setOnAction(eh);
    locationText.setOnAction(eh);
    date.setOnAction(eh);
    time.setOnAction(eh);
    checkNote.setOnAction(eh);
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
  //
  public void lookup(KeyEvent e) {
    ServiceTicketDataController.lookupNodes(e, serviceLocationList, locationText);
  }

  public void lookupUser(KeyEvent e) {
    ServiceTicketDataController.lookupUsernames(
        e, User.Permission.RESPOND_TO_MEDICINE_REQUEST, requestAssignedList, assignedID);
  }

  @FXML
  private void onClear() {
    patient_name.clear();
    drug.clear();
    date.valueProperty().set(null);
    dose.clear();
    time.valueProperty().set(null);
    comment.clear();
    checkNote.setSelected(false);
    assignedID.clear();
    currentID.clear();
  }
}
