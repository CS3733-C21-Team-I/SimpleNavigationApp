package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AudioVisualRequestController {

  @FXML JFXTextField patientName;
  @FXML JFXTextField roomNum;
  @FXML JFXComboBox mediaType;
  @FXML JFXTextArea details;
  @FXML JFXTextField employeeID;

  Button clearBtn;
  Button submitBtn;
  ServiceTicket ticket;

  @FXML
  public void createTicket(ActionEvent e) {}

  @FXML
  public void clear(javafx.event.ActionEvent Event) {
    patientName.clear();
    roomNum.clear();
    mediaType.getSelectionModel().select(null);
    details.clear();
    employeeID.clear();
  }

  public void initialize() {
    /*TODO common stuff*/
  }
}
