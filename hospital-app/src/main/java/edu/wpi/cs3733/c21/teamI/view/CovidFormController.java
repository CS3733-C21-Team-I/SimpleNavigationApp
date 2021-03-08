package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class CovidFormController {

  @FXML
  JFXCheckBox soreThroatCheckbox,
      breathCheckbox,
      headacheCheckbox,
      tasteCheckbox,
      feverCheckbox,
      congestionCheckbox,
      coughCheckbox,
      nauseaCheckbox,
      diarrheaCheckbox,
      confirmedCheckbox,
      symptomCheckbox,
      noneCheckbox,
      noneCheckbox2;
  @FXML JFXRadioButton covidYesRadioBtn, covidNoRadioBtn;
  @FXML Label parkingIndication, warningLabel;
  @FXML JFXButton submitBttn;
  @FXML StackPane root;
  ArrayList<JFXCheckBox> symptoms = new ArrayList<>();
  ArrayList<JFXCheckBox> closeContactChecks = new ArrayList<>();

  @FXML
  public void initialize() {
    symptoms.add(soreThroatCheckbox);
    symptoms.add(breathCheckbox);
    symptoms.add(headacheCheckbox);
    symptoms.add(tasteCheckbox);
    symptoms.add(feverCheckbox);
    symptoms.add(congestionCheckbox);
    symptoms.add(coughCheckbox);
    symptoms.add(nauseaCheckbox);
    symptoms.add(diarrheaCheckbox);
    closeContactChecks.add(confirmedCheckbox);
    closeContactChecks.add(symptomCheckbox);
    submitBttn.setDisable(true);
  }

  public void submit() throws IOException {

    boolean symptoms = false, waitingForResult, isCovidRisk = false;

    if (!soreThroatCheckbox.isSelected()
        && breathCheckbox.isSelected()
            | headacheCheckbox.isSelected()
            | tasteCheckbox.isSelected()
            | feverCheckbox.isSelected()
            | congestionCheckbox.isSelected()
            | coughCheckbox.isSelected()
            | nauseaCheckbox.isSelected()
            | diarrheaCheckbox.isSelected()
            | confirmedCheckbox.isSelected()
            | symptomCheckbox.isSelected())
      if (soreThroatCheckbox.isSelected()
          | breathCheckbox.isSelected()
          | headacheCheckbox.isSelected()
          | tasteCheckbox.isSelected()
          | feverCheckbox.isSelected()
          | congestionCheckbox.isSelected()
          | coughCheckbox.isSelected()
          | nauseaCheckbox.isSelected()
          | diarrheaCheckbox.isSelected()
          | confirmedCheckbox.isSelected()
          | symptomCheckbox.isSelected()) {
        symptoms = true;
      }

    if (covidYesRadioBtn.isSelected()) {
      waitingForResult = true;
    }

    if (symptoms == true) {
      isCovidRisk = true;
    }
  }

  public void clear() {
    soreThroatCheckbox.setSelected(false);
    breathCheckbox.setSelected(false);
    headacheCheckbox.setSelected(false);
    tasteCheckbox.setSelected(false);
    feverCheckbox.setSelected(false);
    congestionCheckbox.setSelected(false);
    coughCheckbox.setSelected(false);
    nauseaCheckbox.setSelected(false);
    diarrheaCheckbox.setSelected(false);
    confirmedCheckbox.setSelected(false);
    symptomCheckbox.setSelected(false);
    covidYesRadioBtn.setSelected(false);
    covidNoRadioBtn.setSelected(false);
  }

  public void cancel() {}





  public void checkFinished() {

    if (IsCheckboxGroupChecked(symptoms) | noneCheckbox.isSelected()
        && IsCheckboxGroupChecked(closeContactChecks) | noneCheckbox2.isSelected()
        && covidYesRadioBtn.isSelected() | covidNoRadioBtn.isSelected()) {
      submitBttn.setDisable(false);
      warningLabel.setVisible(false);
    } else {
      submitBttn.setDisable(true);
      warningLabel.setVisible(true);
    }
  }

  public boolean IsCheckboxGroupChecked(ArrayList<JFXCheckBox> checkboxElements) {

    for (JFXCheckBox c : checkboxElements) {
      if (c.isSelected()) {
        return true;
      }
    }
    return false;
  }
}
