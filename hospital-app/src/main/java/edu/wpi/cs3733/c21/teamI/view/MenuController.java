package edu.wpi.cs3733.c21.teamI.view;

import static edu.wpi.cs3733.c21.teamI.user.User.Permission.*;

import com.jfoenix.controls.JFXRippler;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuController extends Application {

  @FXML VBox menu;
  @FXML JFXRippler loginButton;
  Image loginIcon = new Image("/fxml/menuFiles/menuImages/baseline_login_white_18dp.png");
  Image homeIcon = new Image("/fxml/menuFiles/menuImages/homeIcon.png");

  @FXML
  public void navigate(MouseEvent e) throws IOException {
    ViewManager.navigate(e);
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
    ViewManager.homeController.update();
  }

  @Override
  public void start(Stage primaryStage) {}

  @FXML
  public void initialize() {
    User user = ApplicationDataController.getInstance().getLoggedInUser();

    for (Node menuItem : menu.getChildren()) {
      menuItem.managedProperty().bind(menuItem.visibleProperty());
      menuItem.setVisible(false);
    }
    menu.getChildren().get(0).setVisible(true);
    menu.getChildren().get(1).setVisible(true);
    menu.getChildren().get(2).setVisible(true);
    menu.getChildren().get(7).setVisible(true);
    menu.getChildren().get(11).setVisible(true);

    if (user.hasPermission(REQUEST_TICKET)) {
      menu.getChildren().get(4).setVisible(true);
    }
    if (user.hasPermission(VIEW_TICKET)) {
      menu.getChildren().get(5).setVisible(true);
    }
    if (user.userRoles.contains(User.Role.NURSE)) {
      menu.getChildren().get(9).setVisible(true);
    }
    if (user.userRoles.contains(User.Role.ADMIN)) {
      menu.getChildren().get(6).setVisible(true);
      menu.getChildren().get(8).setVisible(true);
      menu.getChildren().get(9).setVisible(true);
    }
    if (ApplicationDataController.getInstance().isLoggedIn()) {
      menu.getChildren().get(10).setVisible(true);
      Node nodeOut = loginButton.getChildren().get(0);

      HBox h = (HBox) nodeOut;

      boolean btnChanged = false;
      for (Node nodeIn : ((HBox) nodeOut).getChildren()) {
        if (nodeIn.getClass().equals(Label.class)) {
          ((Label) nodeIn).setText("Home");
        } else if (nodeIn.getClass().equals(ImageView.class)) {
          ((ImageView) nodeIn).setImage(homeIcon);
        }
      }
    }

    if (!(ApplicationDataController.getInstance().isLoggedIn())) {
      Node nodeOut = loginButton.getChildren().get(0);

      for (Node nodeIn : ((HBox) nodeOut).getChildren()) {
        if (nodeIn.getClass().equals(Label.class)) {
          ((Label) nodeIn).setText("Login");
        } else if (nodeIn.getClass().equals(ImageView.class)) {
          ((ImageView) nodeIn).setImage(loginIcon);
        }
      }
    }
  }
}
