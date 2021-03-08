package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicketDataController;
import edu.wpi.cs3733.c21.teamI.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javax.swing.*;

public class MedicineDeliveryController {
  @FXML JFXTextField patient_name, currentID, assignedID, floor, room, locationText;
  @FXML JFXDatePicker date;
  @FXML JFXTimePicker time;
  @FXML TextArea comment;
  @FXML JFXCheckBox checkNote;
  @FXML JFXButton clearBtn, submitBtn, cancelBtn;
  @FXML ListView serviceLocationList, requestAssignedList;
  @FXML AnchorPane background;
  ServiceTicket ticket;

  @FXML
  private void submit() {
    String patientName, floorPicked, roomPicked, datePicked, timePicked, cond, com, check;
    patientName = patient_name.getText();
    floorPicked = floor.getText();
    roomPicked = room.getText();
    datePicked = date.getValue().toString();
    timePicked = time.getValue().toString();
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
          new ServiceTicket(
              RequestID,
              ServiceTicket.TicketType.MEDICINE,
              NavDatabaseManager.getInstance().getMapIdFromLongName(locationText.getText()),
              comment.getText(),
              false);

      ticket.addAssignedUserID(AssignedID);
      int id = ServiceTicketDatabaseManager.getInstance().addTicket(ticket);
      ServiceTicketDatabaseManager.getInstance().addEmployeeForTicket(id, AssignedID);
    } catch (Exception o) {
      System.out.println("Error" + o);
    }
  }

  public void initialize() {
    ServiceTicketDataController.setupRequestView(
        background, serviceLocationList, requestAssignedList, currentID, assignedID, locationText);
  }

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
    floor.clear();
    date.valueProperty().set(null);
    room.clear();
    time.valueProperty().set(null);
    comment.clear();
    checkNote.setSelected(false);
    assignedID.clear();
    currentID.clear();
  }
}
