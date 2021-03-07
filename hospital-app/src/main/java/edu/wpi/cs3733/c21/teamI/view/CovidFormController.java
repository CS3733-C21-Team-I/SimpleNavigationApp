package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
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
      symptomCheckbox;
  @FXML JFXRadioButton covidYesRadioBtn, covidNoRadioBtn;

  @FXML JFXTextField tempTextfield;
  // @FXML JFXButton cancel, clear, submit;
  @FXML StackPane root;
  @FXML Label parkingIndication;

  @FXML
  public void initialize() {}

  public void submit() throws IOException {

    boolean symptoms = false, waitingForResult, suspect = false;
    double bodyTemperature = 97;

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

    if (symptoms == true | bodyTemperature >= 99) {
      suspect = true;
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
    tempTextfield.clear();
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
}
