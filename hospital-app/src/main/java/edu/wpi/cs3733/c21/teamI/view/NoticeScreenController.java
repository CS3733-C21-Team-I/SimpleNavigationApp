package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;

public class NoticeScreenController {

  @FXML JFXButton okBtn;
  @FXML StackPane root;

  @FXML
  public void goToPathfinding() throws IOException {
    root.getChildren().clear();
    root.getChildren()
        .add(FXMLLoader.load(getClass().getResource("/fxml/MobilePages/PathfindingMobile.fxml")));
  }
}
