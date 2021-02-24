package edu.wpi.cs3733.c21.teamI.view;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class RequestHomeController extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {}

  public void navigate(ActionEvent e) throws IOException {
    ViewManager.navigate(e);
  }
}
