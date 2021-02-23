package edu.wpi.cs3733.c21.teamI.view;

import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapCSVBuilder;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HomeController extends Application {

  @FXML Label dateTime;

  @FXML Button serviceRequests, map;

  @FXML
  public void initClock() {
    Timeline clock =
        new Timeline(
            new KeyFrame(
                Duration.ZERO,
                e -> {
                  DateTimeFormatter formatter =
                      DateTimeFormatter.RFC_1123_DATE_TIME; // "yyyy-MM-dd HH:mm:ss"
                  if (dateTime != null) {
                    String dateTimeString = ZonedDateTime.now().format(formatter);
                    dateTime.setText(dateTimeString.substring(0, dateTimeString.length() - 9));
                  }
                }),
            new KeyFrame(Duration.seconds(1)));
    clock.setCycleCount(Animation.INDEFINITE);
    clock.play();
  }

  @FXML
  public void exit() {
    ViewManager.getStage().close();

    HospitalMapCSVBuilder.saveCSV(
        NavDatabaseManager.getInstance().loadMapsFromMemory().values(),
        "csv/MapINewNodes.csv",
        "csv/MapINewEdgers.csv");
  }

  public void navigate(ActionEvent e) throws IOException {
    ViewManager.navigate(e);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    AnchorPane root = FXMLLoader.load(getClass().getResource("/fxml/Home.fxml"));
    primaryStage.setTitle("Home");
    Scene applicationScene = new Scene(root, 973, 800);
    primaryStage.setScene(applicationScene);
    primaryStage.show();
  }

  @FXML
  public void initialize() {
    initClock();
    if (ApplicationDataController.getInstance()
        .getLoggedInUser()
        .hasPermission(User.Permission.VIEW_TICKET)) {
      serviceRequests.setMaxWidth(map.getMaxWidth());
      serviceRequests.setVisible(true);
    } else {
      serviceRequests.setMaxWidth(0);
      serviceRequests.setVisible(false);
    }
  }
}
