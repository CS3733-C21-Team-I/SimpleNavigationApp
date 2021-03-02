package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXRippler;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class VisitorMenuController extends Application {

  StackPane replacePane = null;
  Label titleLabel = null;

  @FXML
  public void navigate(MouseEvent e) throws IOException {
    String id = ((JFXRippler) e.getSource()).getId();
    replacePane =
        (StackPane)
            ((JFXRippler) e.getSource())
                .getParent()
                .getParent()
                .getParent()
                .getParent()
                .getParent()
                .getChildrenUnmodifiable()
                .get(0);
    System.out.println(replacePane);
    System.out.println("Hello");
    replacePane.getChildren().clear();
    if (id.equals("loginButton")) {
      replacePane.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Profile.fxml")));
    } else if (id.equals("COVIDButton")) {
      //      titleLabel.setText("Language Service Request");
      replacePane
          .getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/menuFiles/CovidForm.fxml")));
    } else if (id.equals("navigateButton")) {
      replacePane.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Map.fxml")));
    } else if (id.equals("giftsButton")) {
      replacePane
          .getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/menuFiles/LaundryRequest.fxml")));
    } else if (id.equals("logoutButton")) {
      replacePane.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Profile.fxml")));
    } else {
      replacePane
          .getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/MaintenanceRequest.fxml")));
    }
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}

  @FXML
  public void initialize() throws IOException {}
}
