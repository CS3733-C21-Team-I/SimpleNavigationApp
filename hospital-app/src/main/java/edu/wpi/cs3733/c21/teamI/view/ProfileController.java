package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXDrawer;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.FailedToAuthenticateException;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProfileController extends Application {

  @FXML VBox requestContainer, loginVBox, serviceDisplay;
  @FXML TextField username;
  @FXML PasswordField password;
  @FXML Label headerLabel;
  public String uName;
  public static String pass;
  @FXML StackPane root;

  VisitorMenuController visitorMenuController;
  HomeController homeController;
  @FXML ScrollPane requestScrollPane;

  @FXML Label titleLabel;
  @FXML JFXDrawer drawer;
  @FXML Stage loginStage;
  @FXML Scene loginScene;
  @FXML ImageView background;

  public void navigate(ActionEvent e) throws IOException {
    ViewManager.navigate(e);
  }

  @FXML
  public void login() throws FailedToAuthenticateException {
    uName = username.getText();
    pass = password.getText();
    try {
      ApplicationDataController.getInstance().logInUser(uName, pass);
      loginVBox.setVisible(false);
      //      serviceDisplay.setVisible(true);
      //      headerLabel.setText("You successfully logged in.");
      if (ApplicationDataController.getInstance()
          .getLoggedInUser()
          .hasPermission(User.Permission.VIEW_TICKET)) {
        homeController.update();
      }
      homeController.update();
    } catch (FailedToAuthenticateException e) {
      headerLabel.setText("Error: Invalid login.");
      // TODO handle failure to login
    }
  }

  public VisitorMenuController getVisitorMenuController() {
    return visitorMenuController;
  }

  public void setVisitorMenuController(VisitorMenuController visitorMenuController) {
    this.visitorMenuController = visitorMenuController;
  }

  public HomeController getHomeController() {
    return homeController;
  }

  public void setHomeController(HomeController homeController) {
    this.homeController = homeController;
  }

  @FXML
  public void initialize() throws IOException {
    loginVBox.setVisible(true);
    System.out.println(root.heightProperty());
    System.out.println(root.widthProperty());
    System.out.println(background);
    background.setPreserveRatio(false);
    // background.fitHeightProperty().bind(root.heightProperty());
    // background.fitWidthProperty().bind(root.widthProperty());
    root.heightProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              background.fitHeightProperty().bind(root.heightProperty());
              System.out.println("Height changed");
            });
    root.widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              background.fitWidthProperty().bind(root.widthProperty());
              System.out.println("Width changed");
            });
    // background.setFitHeight(Screen.getPrimary().getVisualBounds().getHeight());
    // background.setFitWidth(Screen.getPrimary().getVisualBounds().getWidth());
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}
}
