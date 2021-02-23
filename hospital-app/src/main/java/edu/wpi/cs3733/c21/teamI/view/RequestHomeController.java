package edu.wpi.cs3733.c21.teamI.view;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RequestHomeController extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    AnchorPane root = FXMLLoader.load(getClass().getResource("/fxml/Requests.fxml"));
    primaryStage.setTitle("Request Options");
    Scene applicationScene = new Scene(root, 973, 800);
    primaryStage.setScene(applicationScene);
    primaryStage.show();
  }

  public void navigate(ActionEvent e) throws IOException {
    ViewManager.navigate(e);
  }
}
