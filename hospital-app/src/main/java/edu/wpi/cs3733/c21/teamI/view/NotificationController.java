package edu.wpi.cs3733.c21.teamI.view;

import static javafx.animation.Animation.INDEFINITE;

import com.jfoenix.controls.JFXDrawer;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.Notification.Notification;
import edu.wpi.cs3733.c21.teamI.database.NotificationManager;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class NotificationController implements Initializable {

  @FXML Label notifMessage;
  @FXML Button notifXBtn;

  public HomeController homeController;
  public JFXDrawer notifDrawer;
  public List<Notification> notifs;
  public int currentNotifID;

  public void initialize(URL url, ResourceBundle rb) {
    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(5),
                ev -> {
                  //                  System.out.println("printing 2 secs latah");
                  notifs =
                      NotificationManager.getInstance()
                          .getPendingNotifications(
                              ApplicationDataController.getInstance().getLoggedInUser());
                  for (Notification n : notifs) {
                    if (n.isHasDisplayed()) {
                      notifMessage.setText(n.getDetails());
                      currentNotifID = n.getNotificationID();
                    }
                  }
                }));
    timeline.setCycleCount(INDEFINITE);
    timeline.play();
  }

  public void closeNotif(javafx.scene.input.MouseEvent e) {
    notifDrawer = (JFXDrawer) ((Button) e.getSource()).getParent().getParent().getParent();
    NotificationManager.getInstance().removeNotification(currentNotifID);
    System.out.println("Just removed Notif " + currentNotifID + " from db");
    notifDrawer.close();
  }

  public void setHomeController(HomeController homeController) {
    this.homeController = homeController;
  }

  public void setNotifMessage(String msg) {
    notifMessage.setText(msg);
  }

  public HomeController getHomeController() {
    return homeController;
  }
}
