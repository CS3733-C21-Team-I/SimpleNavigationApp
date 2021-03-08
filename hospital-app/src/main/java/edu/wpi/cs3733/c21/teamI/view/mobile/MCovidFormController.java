package edu.wpi.cs3733.c21.teamI.view.mobile;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class MCovidFormController {
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

    goToWaitingScreen();
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

  public void displayParkingSpot(int lotAssigned) {

    parkingIndication.setText(
        "Your parking spot is "
            + String.valueOf(lotAssigned)
            + ". Please take the following covid form before entering the hospital.");
  }

  public void goToWaitingScreen() throws IOException {
    //    System.out.println(((JFXButton) actionEvent.getSource()).getScene());
    root.getChildren().clear();
    root.getChildren()
        .add(FXMLLoader.load(getClass().getResource("/fxml/MobilePages/MWaitingScreen.fxml")));
  }

  public void checkFinished() {
    if (IsCheckboxGroupChecked(symptoms) | noneCheckbox.isSelected()
        && IsCheckboxGroupChecked(closeContactChecks) | noneCheckbox2.isSelected()
        && covidYesRadioBtn.isSelected() | covidNoRadioBtn.isSelected()) {
      submitBttn.setDisable(false);
    } else {
      submitBttn.setDisable(true);
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

  @FXML
  public void exit(MouseEvent e) {
    Stage stage = (Stage) ((Circle) e.getSource()).getScene().getWindow();
    stage.close();
  }
}
