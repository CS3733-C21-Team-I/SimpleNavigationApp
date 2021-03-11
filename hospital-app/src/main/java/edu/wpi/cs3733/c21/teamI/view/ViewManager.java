package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXRippler;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.hospitalMap.MapDataEntity;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ViewManager {

  private static Group root = null;
  private static Stage stage = null;
  private static StackPane replacePane = null;
  static Map<String, String> navigationMap =
      Stream.of(
              new String[][] {
                {"COVIDButton", "/fxml/menuFiles/CovidForm.fxml"},
                {"giftsButton", "/fxml/serviceRequests/GiftRequest.fxml"},
                {"requestButton", "/fxml/menuFiles/ServiceView.fxml"},
                {"employeeButton", "/fxml/EmployeeTable.fxml"},
                {"logoutButton", "/fxml/Profile.fxml"},
                {"parkingButton", "/fxml/ActiveLots.fxml"},
                {"ticketButton", "/fxml/ServiceRequestTableView.fxml"},
                {"feedbackButton", "/fxml/menuFiles/feedbackView.fxml"},
                {"trackerButton", "/fxml/menuFiles/COVIDTracker.fxml"},
                {"navigateButton", "/fxml/Pathfinding.fxml"},
              })
          .collect(Collectors.toMap(data -> data[0], data -> data[1]));

  public static void navigate(MouseEvent e, HomeController homeController) throws IOException {
    String id = ((JFXRippler) e.getSource()).getId();
    StackPane replacePane =
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
        profLoader =
            new FXMLLoader(ViewManager.class.getClassLoader().getResource("/fxml/Home.fxml"));
        profLoader.setLocation(ViewManager.class.getResource("/fxml/Home.fxml"));
        replacePane.getChildren().add(profLoader.load());
      } else {
        profLoader =
            new FXMLLoader(ViewManager.class.getClassLoader().getResource("/fxml/Profile.fxml"));
        profLoader.setLocation(ViewManager.class.getResource("/fxml/Profile.fxml"));
        replacePane.getChildren().add(profLoader.load());
        ((ProfileController) profLoader.getController()).setHomeController(homeController);
      }
    }
    for (String button : navigationMap.keySet()) {
      if (id.equals(button)) {
        replacePane
            .getChildren()
            .add(FXMLLoader.load(ViewManager.class.getResource(navigationMap.get(button))));
      }
      if (button.equals("navigateButton")) {
        MapDataEntity.getNodesSet(true);
      }
    }
  }

  public ViewManager() {}

  public static StackPane getReplacePane() {
    return replacePane;
  }

  public static void setReplacePane(StackPane replacePane) {
    ViewManager.replacePane = replacePane;
  }

  public void setRoot(Group root) {
    ViewManager.root = root;
  }

  public static void setStage(Stage stage) {
    ViewManager.stage = stage;
  }

  public static Stage getStage() {
    return stage;
  }

  public static Group getRoot() {
    return root;
  }
}
