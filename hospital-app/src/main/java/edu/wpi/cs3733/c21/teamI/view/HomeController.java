package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
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
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HomeController extends Application {

  @FXML Label dateTime;

  @FXML Button serviceRequests, map;

  @FXML StackPane replacePane;

  @FXML Label titleLabel;

  @FXML JFXDrawer drawer;

  @FXML JFXHamburger ham1;

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
    HospitalMapCSVBuilder.saveCSV(
        NavDatabaseManager.getInstance().loadMapsFromMemory().values(),
        "csv/MapINewNodes.csv",
        "csv/MapINewEdgers.csv");
    Platform.exit();
    System.exit(0);
  }

  //  public void navigate(ActionEvent e) throws IOException {
  //    String id = ((JFXRippler) e.getSource()).getId();
  //    if (id.equals("loginButton")) {
  //      replacePane.getChildren().clear();
  //      replacePane
  //          .getChildren()
  //          .add(FXMLLoader.load(ViewManager.class.getResource("/fxml/Profile.fxml")));
  //    } else if (id.equals("COVIDButton")) {
  //      replacePane.getChildren().clear();
  //      replacePane
  //          .getChildren()
  //          .add(FXMLLoader.load(ViewManager.class.getResource("/fxml/Requests.fxml")));
  //    } else if (id.equals("navigateButton")) {
  //      replacePane.getChildren().clear();
  //      replacePane
  //          .getChildren()
  //          .add(FXMLLoader.load(ViewManager.class.getResource("/fxml/Map.fxml")));
  //    } else if (id.equals("giftsButton")) {
  //      replacePane.getChildren().clear();
  //      replacePane
  //          .getChildren()
  //          .add(FXMLLoader.load(ViewManager.class.getResource("/fxml/SanitationRequest.fxml")));
  //    } else if (id.equals("logoutButton")) {
  //      replacePane.getChildren().clear();
  //      replacePane
  //          .getChildren()
  //          .add(FXMLLoader.load(ViewManager.class.getResource("/fxml/Profile.fxml")));
  //    } else {
  //      replacePane.getChildren().clear();
  //      replacePane
  //          .getChildren()
  //          .add(FXMLLoader.load(ViewManager.class.getResource("/fxml/MaintenanceRequest.fxml")));
  //    }
  //  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/fxml/menuFiles/Menu.fxml"));
    primaryStage.setTitle("Hospital App");
    Scene applicationScene = new Scene(root, 973, 800);
    ViewManager.setReplacePane(replacePane);
    primaryStage.setScene(applicationScene);
    primaryStage.show();
  }

  @FXML
  public void initialize() throws IOException {
    initClock();
    VBox box = null;
    try {
      if (ApplicationDataController.getInstance()
          .getLoggedInUser()
          .userRoles
          .contains(User.Role.ADMIN)) {
        box = FXMLLoader.load(getClass().getResource("/fxml/menuFiles/AdminMenu.fxml"));
        titleLabel.setText("Admin Portal");
      } else {
        box = FXMLLoader.load(getClass().getResource("/fxml/menuFiles/VisitorMenu.fxml"));
        titleLabel.setText("General Portal");
      }
      replacePane
          .getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/menuFiles/LanguageRequest.fxml")));
    } catch (IOException e) {
      e.printStackTrace();
    }
    drawer.setSidePane(box);

    HamburgerSlideCloseTransition hamburgerTransition = new HamburgerSlideCloseTransition(ham1);
    hamburgerTransition.setRate(-1);
    ham1.addEventHandler(
        MouseEvent.MOUSE_CLICKED,
        (e) -> {
          hamburgerTransition.setRate(hamburgerTransition.getRate() * -1);
          hamburgerTransition.play();

          if (drawer.isOpened()) {
            drawer.close();
          } else {
            drawer.open();
          }
        });
    //    if (ApplicationDataController.getInstance()
    //        .getLoggedInUser()
    //        .hasPermission(User.Permission.VIEW_TICKET)) {
    //      serviceRequests.setMaxWidth(map.getMaxWidth());
    //      serviceRequests.setVisible(true);
    //      serviceRequests.setManaged(true);
    //    } else {
    //      serviceRequests.setMaxWidth(0);
    //      serviceRequests.setVisible(false);
    //      serviceRequests.setManaged(false);
    //    }

  }
}
