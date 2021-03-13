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
import javafx.util.Duration;

public class NotificationController implements Initializable {

  @FXML Label notifMessage;
  @FXML Button notifXBtn;

  public JFXDrawer notifDrawer;
  public List<Notification> notifs;
  public int currentNotifID;

  public void initialize(URL url, ResourceBundle rb) {
    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(4),
                ev -> {
                  updateNotifText();
                }));
    timeline.setCycleCount(INDEFINITE);
    timeline.play();
  }

  public void closeNotif(javafx.scene.input.MouseEvent e) {
    notifDrawer = (JFXDrawer) ((Button) e.getSource()).getParent().getParent().getParent();
    NotificationManager.getInstance().removeNotification(currentNotifID);
    System.out.println("Just removed Notif " + currentNotifID + " from db");
    notifDrawer.close();
    notifMessage.setText("");
    updateNotifText();
  }

  public void updateNotifText() {
    notifs =
        NotificationManager.getInstance()
            .getPendingNotifications(ApplicationDataController.getInstance().getLoggedInUser());
    if (notifs.size() > 0) {
      notifMessage.setText(notifs.get(0).getDetails());
      currentNotifID = notifs.get(0).getNotificationID();
    }
  }

  public void setNotifMessage(String msg) {
    notifMessage.setText(msg);
  }
}
