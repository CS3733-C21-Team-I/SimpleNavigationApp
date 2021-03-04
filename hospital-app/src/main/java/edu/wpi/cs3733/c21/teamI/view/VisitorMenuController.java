package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXRippler;
import edu.wpi.cs3733.c21.teamI.hospitalMap.MapDataEntity;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class VisitorMenuController extends Application {

  StackPane replacePane = null;
  Label titleLabel = null;
  public HomeController homeController;

  @FXML
  public void navigate(MouseEvent e) throws IOException {
    String id = ((JFXRippler) e.getSource()).getId();
    replacePane =
        (StackPane)
            ((JFXRippler) e.getSource())
                .getParent()
                .getParent()
                .getParent()
                .getParent()
                .getParent()
                .getChildrenUnmodifiable()
                .get(0);
    System.out.println(replacePane);
    System.out.println("Hello");
    replacePane.getChildren().clear();

    if (id.equals("loginButton")) {
      System.out.println(getClass().getResource("/fxml/Profile.fxml"));
      FXMLLoader profLoader =
          new FXMLLoader(getClass().getClassLoader().getResource("/fxml/Profile.fxml"));
      profLoader.setLocation(getClass().getResource("/fxml/Profile.fxml"));
      replacePane.getChildren().add(profLoader.load());
      ((ProfileController) profLoader.getController()).setVisitorMenuController(this);
      ((ProfileController) profLoader.getController()).setHomeController(homeController);
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
          .add(
              FXMLLoader.load(getClass().getResource("/fxml/serviceRequests/LaundryRequest.fxml")));
    } else if (id.equals("logoutButton")) {
      replacePane.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Profile.fxml")));
    } else {
      replacePane
          .getChildren()
          .add(
              FXMLLoader.load(
                  getClass().getResource("/fxml/serviceRequests/MaintenanceRequest.fxml")));
    }
  }

  public HomeController getHomeController() {
    return homeController;
  }

  public void setHomeController(HomeController homeController) {
    this.homeController = homeController;
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}

  @FXML
  public void initialize() throws IOException {}
}
