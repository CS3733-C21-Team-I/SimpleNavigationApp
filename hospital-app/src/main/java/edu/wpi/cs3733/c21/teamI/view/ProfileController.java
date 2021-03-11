package edu.wpi.cs3733.c21.teamI.view;

import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.FailedToAuthenticateException;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ProfileController extends Application {

  @FXML TextField username;
  @FXML PasswordField password;
  @FXML Label headerLabel;
  public String uName;
  public static String pass;

  @FXML
  public void login() {
    uName = username.getText();
    pass = password.getText();
    try {
      ApplicationDataController.getInstance().logInUser(uName, pass);
      ViewManager.homeController.update();
    } catch (FailedToAuthenticateException e) {
      headerLabel.setVisible(true);
      headerLabel.setText("Error: Invalid login.");
    }
  }

  @FXML
  public void initialize() throws IOException {
    headerLabel.managedProperty().bind(headerLabel.visibleProperty());
    headerLabel.setVisible(false);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}
}
