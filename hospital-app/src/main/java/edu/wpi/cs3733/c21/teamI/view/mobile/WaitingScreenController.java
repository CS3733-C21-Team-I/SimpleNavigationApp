package edu.wpi.cs3733.c21.teamI.view.mobile;

import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class WaitingScreenController extends Application {

  @FXML StackPane root;

  @FXML
  public void initialize() throws InterruptedException, IOException {
    Timer timer = new Timer();
    timer.schedule(new WaitForResults(), 0, 1000);
  }

  @FXML
  public void returnReviewDescision() throws IOException {
    root.getChildren().clear();
    root.getChildren()
        .add(FXMLLoader.load(getClass().getResource("/fxml/MobilePages/MobileNoticePage.fxml")));
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}

  @FXML
  public void exit(MouseEvent e) {
    Stage stage = (Stage) ((Circle) e.getSource()).getScene().getWindow();
    stage.close();
  }

  private class WaitForResults extends TimerTask {
    public void run() {
      System.out.println("Checking covid status");
      User.CovidRisk risk =
          UserDatabaseManager.getInstance()
              .getUserForScreenname(
                  ApplicationDataController.getInstance().getLoggedInUser().getScreenName())
              .getCovidRisk();
      System.out.println(risk);

      if (risk != User.CovidRisk.PENDING) {
        try {
          returnReviewDescision();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
