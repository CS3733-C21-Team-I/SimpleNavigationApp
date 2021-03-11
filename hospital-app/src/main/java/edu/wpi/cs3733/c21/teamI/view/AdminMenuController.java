package edu.wpi.cs3733.c21.teamI.view;

import static edu.wpi.cs3733.c21.teamI.user.User.Permission.*;

import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminMenuController extends Application {

  public HomeController homeController;
  @FXML VBox menu;

  @FXML
  public void navigate(MouseEvent e) throws IOException {
    ViewManager.navigate(e, homeController);
  }

  @FXML
  public void exit() {
    Platform.exit();
    System.exit(0);
  }

  @FXML
  public void logout(MouseEvent event) throws IOException {
    ApplicationDataController.getInstance().logOutUser();
    navigate(event);
    homeController.update();
  }

  public void setHomeController(HomeController homeController) {
    this.homeController = homeController;
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}

  @FXML
  public void initialize() throws IOException {
    User user = ApplicationDataController.getInstance().getLoggedInUser();

    for (Node menuItem : menu.getChildren()) {
      menuItem.managedProperty().bind(menuItem.visibleProperty());
      menuItem.setVisible(false);
    }
    menu.getChildren().get(0).setVisible(true);
    menu.getChildren().get(2).setVisible(true);
    menu.getChildren().get(10).setVisible(true);
    menu.getChildren().get(11).setVisible(true);

    if (user.hasPermission(REQUEST_TICKET)) {
      menu.getChildren().get(4).setVisible(true);
    }
    if (user.hasPermission(VIEW_TICKET)) {
      menu.getChildren().get(5).setVisible(true);
      menu.getChildren().get(6).setVisible(true);
    }
    if (user.userRoles.contains(User.Role.NURSE)) {
      menu.getChildren().get(9).setVisible(true);
    }
    if (user.userRoles.contains(User.Role.ADMIN)) {
      menu.getChildren().get(7).setVisible(true);
      menu.getChildren().get(8).setVisible(true);
      menu.getChildren().get(9).setVisible(true);
    }
  }
}
