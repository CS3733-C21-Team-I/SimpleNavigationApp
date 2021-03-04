package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import java.awt.*;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class FeedbackViewController {
  @FXML AnchorPane replaceRequest;
  @FXML JFXTextField time;
  @FXML JFXToggleButton emergency, satisfied, impact;
  @FXML JFXTextArea description;

  @FXML
  public void navigate(ActionEvent e) throws IOException {
    String id = ((JFXButton) e.getSource()).getId();
    replaceRequest.getChildren().clear();
    if (id.equals("religiousButton")) {
      replaceRequest
          .getChildren()
          .add(
              FXMLLoader.load(
                  getClass().getResource("/fxml/serviceRequests/ReligiousFeedback.fxml")));
    }
  }

  @FXML
  public void clear(ActionEvent e) {
    time.clear();
    emergency.setSelected(false);
    satisfied.setSelected(false);
    impact.setSelected(false);
    description.clear();
  }
}
