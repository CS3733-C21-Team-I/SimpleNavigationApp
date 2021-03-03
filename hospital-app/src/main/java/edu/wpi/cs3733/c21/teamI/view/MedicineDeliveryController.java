package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javax.swing.*;

public class MedicineDeliveryController {
  @FXML JFXTextField patient_name, currentID, assignedID, floor, room;
  @FXML JFXDatePicker date;
  @FXML JFXTimePicker time;
  @FXML JFXToggleButton urgentCond;
  @FXML TextArea comment;
  @FXML JFXCheckBox checkNote;
  @FXML JFXButton clearBtn, submitBtn, cancelBtn;
  ServiceTicket ticket;

  @FXML
  private void submit() {
    String patientName, floorPicked, roomPicked, datePicked, timePicked, cond, com, check;
    patientName = patient_name.getText();
    floorPicked = floor.getText();
    roomPicked = room.getText();
    datePicked = date.getValue().toString();
    timePicked = time.getValue().toString();
    if (urgentCond.isSelected()) {
      cond = "urgent";
    } else {
      cond = "not urgent";
    }
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
              AssignedID,
              ServiceTicket.TicketType.SECURITY,
              "Floor: " + floorPicked + " Room: " + roomPicked,
              comment.getText(),
              false,
              false);
      ServiceTicketDatabaseManager.getInstance().addTicket(ticket);
    } catch (Exception o) {
      System.out.println("Error" + o);
    }
  }

  @FXML
  private void onClear() {
    patient_name.clear();
    floor.clear();
    date.valueProperty().set(null);
    room.clear();
    time.valueProperty().set(null);
    urgentCond.setSelected(false);
    comment.clear();
    checkNote.setSelected(false);
    assignedID.clear();
    currentID.clear();
  }
}
