package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javax.swing.*;

public class MedicineDeliveryController {
  @FXML JFXTextField patient_name;
  @FXML JFXComboBox<String> floor, room;
  @FXML JFXDatePicker date;
  @FXML JFXTimePicker time;
  @FXML JFXToggleButton urgentCond;
  @FXML TextArea comment;
  @FXML JFXCheckBox checkNote;
  @FXML JFXButton clearBtn, submitBtn, cancelBtn;

  @FXML
  private void onSubmit() {
    String patientName, floorPicked, roomPicked, datePicked, timePicked, cond, com, check;
    patientName = patient_name.getText();
    floorPicked = floor.getValue();
    roomPicked = room.getValue();
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
    System.out.println(
        "The patient named "
            + patientName
            + " picked floor "
            + floorPicked
            + "in room "
            + roomPicked
            + " on date "
            + datePicked
            + " at time "
            + timePicked
            + "for a "
            + cond
            + " "
            + com
            + ". The patient "
            + check);
  }

  @FXML
  private void onClear() {
    patient_name.clear();
    floor.valueProperty().set(null);
    date.valueProperty().set(null);
    room.valueProperty().set(null);
    time.valueProperty().set(null);
    urgentCond.setSelected(false);
    comment.clear();
    checkNote.setSelected(false);
  }

  @FXML
  private void onCancel() {
    // TODO return to home page
  }
}
