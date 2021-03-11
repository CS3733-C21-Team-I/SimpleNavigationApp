package edu.wpi.cs3733.c21.teamI.view;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ViewManager {

  private static Group root = null;
  private static Stage stage = null;
  private static StackPane replacePane = null;

  public ViewManager() {}

  public static void navigate(ActionEvent e) throws IOException {
    //        Group root = new Group();
    //        Scene scene = ((Button) e.getSource()).getScene();
    String id = ((Button) e.getSource()).getId();
    //    if (id.equals("loginButton")) {
    //
    // root.getChildren().add(FXMLLoader.load(ViewManager.class.getResource("/fxml/Home.fxml")));
    //    } else if (id.equals("COVIDButton")) {
    //
    // root.getChildren().add(FXMLLoader.load(ViewManager.class.getResource("/fxml/Requests.fxml")));
    //    } else if (id.equals("")) {
    //
    // root.getChildren().add(FXMLLoader.load(ViewManager.class.getResource("/fxml/Map.fxml")));
    //    } else if (id.equals("serviceRequests")) {
    //
    //
    // root.getChildren().add(FXMLLoader.load(ViewManager.class.getResource("/fxml/Requests.fxml")));
    //        } else if (id.equals("maintenance")) {
    //          root.getChildren()
    //
    // .add(FXMLLoader.load(ViewManager.class.getResource("/fxml/MaintenanceRequest.fxml")));
    //    } else if (id.equals("profile")) {
    //
    // root.getChildren().add(FXMLLoader.load(ViewManager.class.getResource("/fxml/Profile.fxml")));
    //    } else {
    //      root.getChildren()
    //
    // .add(FXMLLoader.load(ViewManager.class.getResource("/fxml/SanitationRequest.fxml")));
    //    }
    //    scene.setRoot(root);
  }

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
