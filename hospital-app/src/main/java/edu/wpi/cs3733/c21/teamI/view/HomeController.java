package edu.wpi.cs3733.c21.teamI.view;

import static javafx.animation.Animation.INDEFINITE;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NotificationManager;
import edu.wpi.cs3733.c21.teamI.notification.Notification;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class HomeController extends Application {

  @FXML StackPane replacePane;

  @FXML Label titleLabel;

  @FXML Label timeLabel;

  @FXML JFXDrawer drawer, notifDrawer;

  @FXML JFXHamburger ham1;

  @FXML AnchorPane drawerPane;

  @FXML Button mobileButton;

  @FXML StackPane homePane;

  @FXML
  public void initClock() {
    Timeline clock =
        new Timeline(
            new KeyFrame(
                Duration.ZERO,
                e -> {
                  DateTimeFormatter formatter =
                      DateTimeFormatter.RFC_1123_DATE_TIME; // "yyyy-MM-dd HH:mm:ss"
                  if (timeLabel != null) {
                    String dateTimeString = ZonedDateTime.now().format(formatter);
                    timeLabel.setText(dateTimeString.substring(0, dateTimeString.length() - 9));
                  }
                }),
            new KeyFrame(Duration.seconds(1)));
    clock.setCycleCount(INDEFINITE);
    clock.play();
  }

  @FXML
  public void goToMobile() throws IOException {
    Group root = new Group();
    root.getChildren()
        .add(FXMLLoader.load(getClass().getResource("/fxml/mobilePages/GoogleMaps.fxml")));
    Scene mobile = new Scene(root);
    Stage stage = new Stage();
    stage.setScene(mobile);
    stage.initStyle(StageStyle.UNDECORATED);
    final double[] xOffset = {0};
    final double[] yOffset = {0};
    final boolean[] dragAllowed = {true};
    root.setOnMousePressed(
        event -> {
          dragAllowed[0] = root.lookup("#phone") == event.getPickResult().getIntersectedNode();
          if (dragAllowed[0]) {
            xOffset[0] = event.getSceneX();
            yOffset[0] = event.getSceneY();
          }
        });
    root.setOnMouseDragged(
        event -> {
          if (dragAllowed[0]) {
            stage.setX(event.getScreenX() - xOffset[0]);
            stage.setY(event.getScreenY() - yOffset[0]);
          }
        });
    stage.initStyle(StageStyle.TRANSPARENT);
    mobile.setFill(Color.TRANSPARENT);
    stage.show();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/fxml/PageFrame.fxml"));
    primaryStage.setTitle("Hospital App");
    Scene applicationScene = new Scene(root, 973, 800);
    primaryStage.setScene(applicationScene);
    primaryStage.setMaximized(true);
    primaryStage.show();
  }

  @FXML
  public void update() {
    VBox box = null;
    HBox hBox = null;
    try {
      FXMLLoader vLoader = new FXMLLoader(getClass().getResource("/fxml/menuFiles/Menu.fxml"));
      box = vLoader.load();
      ViewManager.homeController = this;
      System.out.println("loading notification fxml");
      FXMLLoader hLoader =
          new FXMLLoader(getClass().getResource("/fxml/menuFiles/NotificationContent.fxml"));
      hBox = hLoader.load();
      if (ApplicationDataController.getInstance()
          .getLoggedInUser()
          .hasPermission(User.Permission.VIEW_TICKET)) {
        Notification notif =
            new Notification(
                ApplicationDataController.getInstance().getLoggedInUser().getUserId(),
                "test notif 1",
                "String timestamp");
        NotificationManager.getInstance().addNotification(notif);
        notif =
            new Notification(
                ApplicationDataController.getInstance().getLoggedInUser().getUserId(),
                "test notif 2",
                "String timestamp");
        NotificationManager.getInstance().addNotification(notif);
        notif =
            new Notification(
                ApplicationDataController.getInstance().getLoggedInUser().getUserId(),
                "test notif 3",
                "String timestamp");
        NotificationManager.getInstance().addNotification(notif);
        titleLabel.setText("Admin Portal");
        replacePane.getChildren().clear();
        replacePane
            .getChildren()
            .add(
                FXMLLoader.load(
                    getClass().getResource("/fxml/menuFiles/ServiceRequestTableView.fxml")));
      } else {
        titleLabel.setText("General Portal");
        replacePane.getChildren().clear();
        replacePane
            .getChildren()
            .add(FXMLLoader.load(getClass().getResource("/fxml/menuFiles/Home.fxml")));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    drawer.setSidePane(box);
    notifDrawer.setSidePane(hBox);
  }

  @FXML
  public void displayNotification(Notification notification, String msg) {
    notifDrawer.open();
    ((Label) notifDrawer.lookup("#notifMessage")).setText(msg);
    System.out.println(
        "Setting Notification " + notification.getNotificationID() + " to hasDisplayed");
    Notification o =
        NotificationManager.getInstance().updateNotification(notification.getNotificationID());
  }

  @FXML
  public void initialize() {

    if (mobileButton != null) {
      mobileButton.managedProperty().bind(mobileButton.visibleProperty());
      mobileButton.setVisible(ApplicationDataController.getInstance().isLoggedIn());
    }
    if (timeLabel != null) {
      initClock();
      update();
      drawerPane
          .heightProperty()
          .addListener(
              (obs, oldVal, newVal) -> {
                Rectangle clip = new Rectangle(drawerPane.getWidth(), drawerPane.getHeight());
                clip.setLayoutX(0);
                clip.setLayoutY(0);
                drawerPane.setClip(clip);
              });
      drawerPane
          .widthProperty()
          .addListener(
              (obs, oldVal, newVal) -> {
                Rectangle clip = new Rectangle(drawerPane.getWidth(), drawerPane.getHeight());
                clip.setLayoutX(0);
                clip.setLayoutY(0);
                drawerPane.setClip(clip);
              });
      notifDrawer.setOnDragDetected(
          e -> {
            notifDrawer.open();
          });

      HamburgerSlideCloseTransition hamburgerTransition = new HamburgerSlideCloseTransition(ham1);
      hamburgerTransition.setRate(-1);
      ham1.addEventHandler(
          MouseEvent.MOUSE_CLICKED,
          (e) -> {
            hamburgerTransition.setRate(hamburgerTransition.getRate() * -1);
            hamburgerTransition.play();
            drawer.toggle();
          });
      drawer.open();
    }
  }

  public StackPane getReplacePane() {
    return replacePane;
  }

  public void closeNotif() {
    notifDrawer.close();
  }
}
