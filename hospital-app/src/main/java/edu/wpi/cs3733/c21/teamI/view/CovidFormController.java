package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CovidFormController extends Application {

  @FXML
  JFXCheckBox throat,
      breath,
      headache,
      smell,
      fever,
      nose,
      cough,
      nausea,
      diarrhea,
      proximityWithConfirmedCase,
      ProximityWithSymptomaticCase;
  @FXML JFXRadioButton yes, no;

  @FXML JFXTextField temperature;
  @FXML JFXButton cancel, clear, submit;

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("CovidForm.fxml"));
    primaryStage.setTitle("Service Form Request");
    Scene applicationScene = new Scene(root, 973, 800);
    primaryStage.setScene(applicationScene);
    primaryStage.show();
  }

  public void submit() {

    boolean symptoms, waitingForResult, suspect = false;
    double bodyTemperature = 97;

    if (throat.isSelected()
        | breath.isSelected()
        | headache.isSelected()
        | smell.isSelected()
        | fever.isSelected()
        | nose.isSelected()
        | cough.isSelected()
        | nausea.isSelected()
        | diarrhea.isSelected()
        | proximityWithConfirmedCase.isSelected()
        | ProximityWithSymptomaticCase.isSelected()) {
      symptoms = true;
    }

    if (yes.isSelected()) {
      waitingForResult = true;
    }

    if (symptoms = true | bodyTemperature >= 99) {
      suspect = true;
    }
  }

  public void clear() {
    throat.setSelected(false);
    breath.setSelected(false);
    headache.setSelected(false);
    smell.setSelected(false);
    fever.setSelected(false);
    nose.setSelected(false);
    cough.setSelected(false);
    nausea.setSelected(false);
    diarrhea.setSelected(false);
    proximityWithConfirmedCase.setSelected(false);
    ProximityWithSymptomaticCase.setSelected(false);
    yes.setSelected(false);
    no.setSelected(false);
    temperature.clear();
  }

  public void cancel() {}
}
