package edu.wpi.cs3733.c21.teamI.view;

import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.LocationNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing.MapEditDataController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ViewManager {

  private static double scale = 3.05; // scales image to 1/scale
  private static MapEditController mapEditorView = null;
  private static Group root = null;
  private static Stage stage = null;
  private static HospitalMapNode selectedNode = null;
  private static final MapEditDataController dataCont = new MapEditDataController();

  public ViewManager() {}

  public static void navigate(ActionEvent e) throws IOException {
    Group root = new Group();
    Scene scene = ((Button) e.getSource()).getScene();
    String id = ((Button) e.getSource()).getId();
    if (id.equals("mapReturn") || id.equals("requestsReturn") || id.equals("loginReturn")) {
      root.getChildren().add(FXMLLoader.load(ViewManager.class.getResource("/fxml/Home.fxml")));
    } else if (id.equals("sanitationReturn") || id.equals("maintenanceReturn")) {
      root.getChildren().add(FXMLLoader.load(ViewManager.class.getResource("/fxml/Requests.fxml")));
    } else if (id.equals("map")) {
      root.getChildren().add(FXMLLoader.load(ViewManager.class.getResource("/fxml/Map.fxml")));
    } else if (id.equals("serviceRequests")) {
      root.getChildren().add(FXMLLoader.load(ViewManager.class.getResource("/fxml/Requests.fxml")));
    } else if (id.equals("maintenance")) {
      root.getChildren()
          .add(FXMLLoader.load(ViewManager.class.getResource("/fxml/MaintenanceRequest.fxml")));
    } else if (id.equals("profile")) {
      root.getChildren().add(FXMLLoader.load(ViewManager.class.getResource("/fxml/Profile.fxml")));
    } else {
      root.getChildren()
          .add(FXMLLoader.load(ViewManager.class.getResource("/fxml/SanitationRequest.fxml")));
    }
    scene.setRoot(root);
  }

  public static void lookupNodes(KeyEvent e, ListView listView, TextField target) {
    String matchString =
        (((TextField) e.getSource()).getText()
                + (!e.getCharacter().equals(Character.toString((char) 8)) ? e.getCharacter() : ""))
            .toLowerCase();
    ArrayList<String> nodeNames =
        NavDatabaseManager.getInstance().loadMapsFromMemory().get("Faulkner 0").getNodes().stream()
            .filter(n -> n instanceof LocationNode)
            .map(n -> ((LocationNode) n).getLongName())
            .filter(s -> !s.equals(""))
            .collect(Collectors.toCollection(ArrayList::new));
    ArrayList<String> matches = new ArrayList<>();
    for (String location : nodeNames) {
      if (location.toLowerCase().contains(matchString)) {
        matches.add(location);
      }
    }

    // Add elements to ListView
    ObservableList<String> items = FXCollections.observableArrayList(matches);
    listView.setItems(items);
    listView.setVisible(e.getSource() == target);
  }

  /**
   * takes in a node object and assigns / de-assigns it to the selectedNode variable in MapState
   *
   * @param node the node to be assigned or de-assigned
   */
  public static boolean toggleNode(HospitalMapNode node) {
    if (selectedNode == null) {
      selectedNode = node;
      return true;
    } else if (selectedNode.equals(node)) {
      selectedNode = null;
      return false;
    } else {
      dataCont.addEdge(node.getID(), selectedNode.getID());
      selectedNode = null;
      return false;
    }
  }

  public static double getScale() {
    return scale;
  }

  public static Set<HospitalMapNode> getEntityNodes() {
    return dataCont.getActiveMap().getNodes();
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

  public static String getMapID() {
    return dataCont.getActiveMap().getId();
  }

  public static HospitalMapNode getSelectedNode() {
    return selectedNode;
  }

  public static MapEditDataController getDataCont() {
    return dataCont;
  }

  public static void setSelectedNode(HospitalMapNode node) {
    ViewManager.selectedNode = node;
  }
}
