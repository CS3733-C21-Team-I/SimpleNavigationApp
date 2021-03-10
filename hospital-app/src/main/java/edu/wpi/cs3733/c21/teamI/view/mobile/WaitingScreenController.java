package edu.wpi.cs3733.c21.teamI.view.mobile;

import static javafx.animation.Animation.INDEFINITE;

import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WaitingScreenController extends Application {

  @FXML StackPane root;
  Timeline timeline;

  @FXML
  public void initialize() throws InterruptedException, IOException {
    this.timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                ev -> {
                  User.CovidRisk risk =
                      UserDatabaseManager.getInstance()
                          .getUserForScreenname(
                              ApplicationDataController.getInstance()
                                  .getLoggedInUser()
                                  .getScreenName())
                          .getCovidRisk();

                  if (risk != User.CovidRisk.PENDING) {
                    try {
                      returnReviewDescision();
                    } catch (IOException e) {
                      e.printStackTrace();
                    }
                  }
                }));
    timeline.setCycleCount(INDEFINITE);
    timeline.play();
  }

  @FXML
  public void returnReviewDescision() throws IOException {
    timeline.stop();
    root.getChildren().clear();
    root.getChildren()
        .add(FXMLLoader.load(getClass().getResource("/fxml/mobilePages/MobileNoticePage.fxml")));
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}

  @FXML
  public void exit(MouseEvent e) {
    Stage stage = (Stage) ((Circle) e.getSource()).getScene().getWindow();
    stage.close();
  }
}
