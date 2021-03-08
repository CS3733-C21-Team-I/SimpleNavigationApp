package edu.wpi.cs3733.c21.teamI.view.mobile;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WaitingScreenController extends Application {

  @FXML StackPane root;

  @FXML
  public void initialize() throws InterruptedException, IOException {
    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(5),
                ev -> {
                  try {
                    returnReviewDescision();
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                }));
    timeline.setCycleCount(1);
    timeline.play();
  }

  public void returnReviewDescision() throws IOException {
    root.getChildren().clear();
    root.getChildren()
        .add(FXMLLoader.load(getClass().getResource("/fxml/MobilePages/MobileNoticePage.fxml")));
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}
}
