package edu.wpi.cs3733.c21.teamI.view.mobile;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
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

  public void changeText(boolean covidRisk) {
    Label label2 = (Label) root.lookup("#label2");
    Label label3 = (Label) root.lookup("#label3");
    if (!covidRisk) {
      label2.setText("");
      label3.setText(
          "You can enter the hospital now and navigate to your destination through the pathfinding function");
    }
  }
}
