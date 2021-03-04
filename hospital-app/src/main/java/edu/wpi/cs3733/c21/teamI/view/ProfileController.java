package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXDrawer;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProfileController extends Application {

  @FXML VBox requestContainer, loginVBox, serviceDisplay;
  @FXML TextField username;
  @FXML PasswordField password;
  @FXML Label headerLabel;
  public String uName;
  public static String pass;

  VisitorMenuController visitorMenuController;
  HomeController homeController;
  @FXML ScrollPane requestScrollPane;

  @FXML Label titleLabel;
  @FXML JFXDrawer drawer;
  @FXML Stage loginStage;
  @FXML Scene loginScene;

  public void navigate(ActionEvent e) throws IOException {
    ViewManager.navigate(e);
  }

  private void populateTicketsProfile() {
    loginVBox.setVisible(true);
    serviceDisplay.setVisible(false);
  }

  @FXML
  public void login() throws IOException {
    uName = username.getText();
    pass = password.getText();
    if (ApplicationDataController.getInstance().logInUser(uName, pass)) {
      loginVBox.setVisible(false);
      serviceDisplay.setVisible(true);
      //      headerLabel.setText("You successfully logged in.");
      if (ApplicationDataController.getInstance()
          .getLoggedInUser()
          .hasPermission(User.Permission.VIEW_TICKET)) {
        generateRequestList();
        homeController.update();
      }
    } else {
      //      headerLabel.setText("Error: Invalid login.");
    }
  }

  public void generateRequestList() {
    List<ServiceTicket> requests =
        ServiceTicketDatabaseManager.getInstance()
            .getTicketsForRequestId(
                ApplicationDataController.getInstance().getLoggedInUser().getUserId());
    List<String> requestNames =
        requests.stream()
            .map(st -> st.getTicketType() + " (" + st.getDescription() + ")")
            .collect(Collectors.toList());
    requestContainer.getStylesheets().add("/fxml/fxmlResources/main.css");
    for (int i = 0; i < requestNames.size(); i++) {
      Button requestButton = new Button(requestNames.get(i));
      int finalI = i;
      requestButton.setOnAction(
          event -> {
            ViewManager.setServiceTicketToShow(requests.get(finalI));
            ViewManager.navigateToActiveRequest(event);
          });
      requestButton.getStyleClass().add("requestButton");
      requestButton.setMinHeight(50);
      requestButton.setMaxWidth(requestContainer.getMaxWidth());
      requestContainer.getChildren().add(requestButton);
    }
    requestScrollPane.setVisible(true);
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
  public void initialize() {
    populateTicketsProfile();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}
}
