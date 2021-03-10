package edu.wpi.cs3733.c21.teamI.view.maps;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;

public class GoogleMapsMainController extends GoogleMap {
  @FXML JFXButton submitButton;
  @FXML StackPane root;
  double zoomLevel = 1;

  @Override
  public void additionalInits() {
    webview.setVisible(true);
    webview.setZoom(getZoom());
    webview
        .getEngine()
        .load("https://www.google.com/maps/@42.30167,-71.1310723,637m/data=!3m1!1e3");
  }

  public double getZoom() {
    return zoomLevel;
  }

  public void switchWindow(ActionEvent actionEvent) throws IOException {
    System.out.println(((JFXButton) actionEvent.getSource()).getScene());
    StackPane replacePane = (StackPane) root.getParent();
    replacePane.getChildren().clear();
    replacePane
        .getChildren()
        .add(FXMLLoader.load(getClass().getResource("/fxml/Pathfinding.fxml")));
  }
}
