package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;

public class ComputerServiceRequestController {

  @FXML JFXTextArea compServReqDes;
  @FXML JFXToggleButton compServReqUrgent;
  @FXML JFXTextField compServReqID;
  @FXML JFXTextField compServReqLoc;
  @FXML JFXTextField compServAssID;

  @FXML
  public void getComputerServiceRequest(javafx.event.ActionEvent Event) {
    System.out.println(
        "RequesterID: "
            + compServReqID.getText()
            + '\n'
            + "AssignedID: "
            + compServAssID.getText()
            + '\n'
            + "Location: "
            + compServReqLoc.getText()
            + '\n'
            + "Description: "
            + compServReqDes.getText()
            + '\n'
            + "Urgent: "
            + compServReqUrgent.isSelected());
  }

  @FXML
  public void clear(javafx.event.ActionEvent Event) {
    compServReqID.clear();
    compServAssID.clear();
    compServReqLoc.clear();
    compServReqDes.clear();
    compServReqUrgent.setSelected(false);
  }
}
