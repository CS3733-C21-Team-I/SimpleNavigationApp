package edu.wpi.cs3733.c21.teamI.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class NotificationController implements Initializable {

  public HomeController homeController;

  public void initialize(URL url, ResourceBundle rb) {}

  public void setHomeController(HomeController homeController) {
    this.homeController = homeController;
  }

  public HomeController getHomeController() {
    return homeController;
  }
}
