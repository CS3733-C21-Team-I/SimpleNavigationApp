package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GoogleMapsController extends Application {
  @FXML StackPane root;

  @Override
  public void start(Stage primaryStage) throws Exception {}

  public void goToCovidForm(ActionEvent actionEvent) throws IOException {
    System.out.println(((JFXButton) actionEvent.getSource()).getScene());
    root.getChildren().clear();
    root.getChildren()
        .add(FXMLLoader.load(getClass().getResource("/fxml/MobilePages/MCovidForm.fxml")));
  }
}
