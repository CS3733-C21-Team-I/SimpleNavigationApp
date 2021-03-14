package edu.wpi.cs3733.c21.teamI.view;

import static javafx.animation.Animation.INDEFINITE;

import com.jfoenix.controls.JFXDrawer;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NotificationManager;
import edu.wpi.cs3733.c21.teamI.notification.Notification;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class NotificationController implements Initializable {

  @FXML Label notifMessage;
  @FXML Button notifXBtn;
  @FXML HBox topElem;
  @FXML VBox whiteSpace;

  public JFXDrawer notifDrawer;
  public List<Notification> notifs;

  public void initialize(URL url, ResourceBundle rb) {
    boolean firstInit = ViewManager.getFirstNotif();
    if (firstInit) {
      topElem.setOnMouseClicked(
          e -> {
            System.out.println("topelem clicked");
          });
      whiteSpace.setOnMouseClicked(
          e -> {
            System.out.println("whitespace clicked");
          });
      notifXBtn.setOnMouseClicked(
          e -> {
            System.out.println("button clicked");
          });
      notifMessage.setOnMouseClicked(
          e -> {
            System.out.println("msg clicked");
          });

      Timeline timeline =
          new Timeline(
              new KeyFrame(
                  Duration.seconds(4),
                  ev -> {
                    getNewNotification();
                  }));
      timeline.setCycleCount(INDEFINITE);
      timeline.play();
      ViewManager.updateFirstNotifInit();
    } else {
      System.out.println("firstInit was false");
    }
  }

  @FXML
  public void closeNotif() {
    List<Notification> notifs =
        NotificationManager.getInstance()
            .getPendingNotifications(ApplicationDataController.getInstance().getLoggedInUser());
    if (!notifs.isEmpty()) {
      NotificationManager.getInstance().removeNotification(notifs.get(0).getNotificationID());
      ViewManager.homeController.closeNotif();
    } else {
      System.out.println("tried removing nonexistent notif");
    }
  }

  public Notification getNewNotification() {
    List<Notification> notifs =
        NotificationManager.getInstance()
            .getPendingNotifications(ApplicationDataController.getInstance().getLoggedInUser());
    for (Notification n : notifs) {
      if (n.isHasDisplayed()) {
        return n;
      } else if (!n.isHasDisplayed()) {
        showNotif(n);
        return n;
      } else {
        return null;
      }
    }
    return null;
  }

  @FXML
  public void showNotif(Notification n) {
    notifMessage.setText(n.getDetails());
    ViewManager.homeController.displayNotification(n, n.getDetails());
  }
}
