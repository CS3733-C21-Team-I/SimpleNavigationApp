package edu.wpi.cs3733.c21.teamI.view;

import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.FailedToAuthenticateException;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ProfileController extends Application {

  @FXML TextField username;
  @FXML PasswordField password;
  @FXML Label headerLabel;
  public String uName;
  public static String pass;

  VisitorMenuController visitorMenuController;
  HomeController homeController;

  public void navigate(ActionEvent e) throws IOException {
    ViewManager.navigate(e);
  }

  @FXML
  public void login() {
    uName = username.getText();
    pass = password.getText();
    try {
      ApplicationDataController.getInstance().logInUser(uName, pass);
      if (ApplicationDataController.getInstance()
          .getLoggedInUser()
          .hasPermission(User.Permission.VIEW_TICKET)) {
        homeController.update();
      }
      homeController.update();
    } catch (FailedToAuthenticateException e) {
      headerLabel.setVisible(true);
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
    headerLabel.managedProperty().bind(headerLabel.visibleProperty());
    headerLabel.setVisible(false);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}
}
