package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXRippler;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.hospitalMap.MapDataEntity;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class ViewManager {

  static Map<String, String> navigationMap =
      Stream.of(
              new String[][] {
                {"COVIDButton", "/fxml/menuFiles/CovidForm.fxml"},
                {"giftsButton", "/fxml/serviceRequests/GiftRequest.fxml"},
                {"requestButton", "/fxml/menuFiles/ServiceView.fxml"},
                {"employeeButton", "/fxml/EmployeeTable.fxml"},
                {"logoutButton", "/fxml/Home.fxml"},
                {"parkingButton", "/fxml/ActiveLots.fxml"},
                {"ticketButton", "/fxml/ServiceRequestTableView.fxml"},
                {"feedbackButton", "/fxml/menuFiles/feedbackView.fxml"},
                {"trackerButton", "/fxml/menuFiles/COVIDTracker.fxml"},
                {"navigateButton", "/fxml/Pathfinding.fxml"},
                {"loginButton", "/fxml/Profile.fxml"}
              })
          .collect(Collectors.toMap(data -> data[0], data -> data[1]));
  protected static HomeController homeController;

  public static void navigate(MouseEvent e) throws IOException {
    String id = ((JFXRippler) e.getSource()).getId();
    StackPane replacePane = homeController.getReplacePane();
    replacePane.getChildren().clear();
    for (String button : navigationMap.keySet()) {
      if (id.equals("loginButton")) {
        replacePane
            .getChildren()
            .add(
                FXMLLoader.load(
                    ViewManager.class.getResource(
                        navigationMap.get(
                            ApplicationDataController.getInstance().isLoggedIn()
                                ? "logoutButton"
                                : "loginButton"))));
        break;
      }
      if (id.equals(button)) {
        replacePane
            .getChildren()
            .add(FXMLLoader.load(ViewManager.class.getResource(navigationMap.get(button))));
        break;
      }
      if (button.equals("navigateButton")) {
        MapDataEntity.getNodesSet(true);
      }
    }
  }
}
