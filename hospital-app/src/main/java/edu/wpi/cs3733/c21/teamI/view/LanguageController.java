package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class LanguageController extends Application {

  @FXML private JFXTextField langTextfield;

  @FXML private JFXTextField langLocationTextfield;

  @FXML private JFXTimePicker langTime;

  @FXML private JFXTextField langReqID;

  @FXML private JFXTextField langAssignedEmp;

  @FXML private JFXCheckBox langCheckbox;

  @FXML
  void clear(ActionEvent event) {
    langTextfield.clear();
    langLocationTextfield.clear();
    langTime.valueProperty().set(null);
    langReqID.clear();
    langAssignedEmp.clear();
    langCheckbox.setSelected(false);
  }

  @FXML
  void submit(ActionEvent event) {
    String langString = langTextfield.getText();
    String locString = langLocationTextfield.getText();
    String timeString = langTime.getValue().toString();
    String reqString = langReqID.getText();
    String assignedString = langAssignedEmp.getText();
    Boolean isLegal = langCheckbox.isSelected();
    System.out.println(
        "Language: "
            + langString
            + ", Location: "
            + locString
            + ", Time: "
            + timeString
            + ", Requester ID: "
            + reqString
            + ", Assigned ID: "
            + assignedString
            + ", involves legal documebts: "
            + isLegal);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}
}
