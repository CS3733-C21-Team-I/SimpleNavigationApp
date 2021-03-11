package edu.wpi.cs3733.c21.teamI.view;

import static edu.wpi.cs3733.c21.teamI.user.User.Permission.*;

import com.jfoenix.controls.JFXRippler;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.hospitalMap.MapDataEntity;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminMenuController extends Application {

  StackPane replacePane = null;
  public HomeController homeController;
  @FXML VBox menu;

  @FXML
  public void navigate(MouseEvent e) throws IOException {
    String id = ((JFXRippler) e.getSource()).getId();
    ViewManager.setReplacePane(replacePane);
    replacePane =
        (StackPane)
            (((AnchorPane)
                    ((JFXRippler) e.getSource())
                        .getParent()
                        .getParent()
                        .getParent()
                        .getParent()
                        .getParent()
                        .getChildrenUnmodifiable()
                        .get(0))
                .getChildren()
                .get(0));
    replacePane.getChildren().clear();

    if (id.equals("loginButton")) {
      FXMLLoader profLoader;
      if (ApplicationDataController.getInstance().isLoggedIn()) {
        profLoader = new FXMLLoader(getClass().getClassLoader().getResource("/fxml/Home.fxml"));
        profLoader.setLocation(getClass().getResource("/fxml/Home.fxml"));
        replacePane.getChildren().add(profLoader.load());
      } else {
        profLoader = new FXMLLoader(getClass().getClassLoader().getResource("/fxml/Profile.fxml"));
        profLoader.setLocation(getClass().getResource("/fxml/Profile.fxml"));
        replacePane.getChildren().add(profLoader.load());
        ((ProfileController) profLoader.getController()).setHomeController(homeController);
      }
      //      ((ProfileController) profLoader.getController()).setVisitorMenuController(this);
    } else if (id.equals("COVIDButton")) {
      //      titleLabel.setText("Language Service Request");
      replacePane
          .getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/menuFiles/CovidForm.fxml")));
    } else if (id.equals("navigateButton")) {
      replacePane
          .getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/Pathfinding.fxml")));
      // pre-load these things before their use
      MapDataEntity.getNodesSet(true);
    } else if (id.equals("giftsButton")) {
      replacePane
          .getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/serviceRequests/GiftRequest.fxml")));
    } else if (id.equals("requestButton")) {
      replacePane
          .getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/menuFiles/ServiceView.fxml")));
    } else if (id.equals("employeeButton")) {
      replacePane
          .getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/EmployeeTable.fxml")));
    } else if (id.equals("logoutButton")) {
      replacePane.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Profile.fxml")));
    } else if (id.equals("parkingButton")) {
      replacePane
          .getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/ActiveLots.fxml")));
    } else if (id.equals("ticketButton")) {
      replacePane
          .getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/ServiceRequestTableView.fxml")));
    } else if (id.equals("feedbackButton")) {
      replacePane
          .getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/menuFiles/feedbackView.fxml")));
    } else if (id.equals("trackerButton")) {
      replacePane
          .getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/menuFiles/COVIDTracker.fxml")));
    }
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
