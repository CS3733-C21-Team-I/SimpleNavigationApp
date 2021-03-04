package edu.wpi.cs3733.c21.teamI.view;

import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMap;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing.MapEditDataController;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import java.io.IOException;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ViewManager {

  private static double scale = 3.05; // scales image to 1/scale
  private static Group root = null;
  private static Stage stage = null;
  private static String selectedNode = null;
  private static String selectedNodeMapID = "Faulkner 0";
  private static final MapEditDataController dataCont = new MapEditDataController();
  private static StackPane replacePane = null;
  private static ServiceTicket serviceTicketToShow;
  private static MapController mapControl = null;

  public ViewManager() {}

  public static void navigate(ActionEvent e) throws IOException {
    System.out.println("I'm in ViewManager");
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

  public static Set<HospitalMapNode> getAllNodesSet() {
    return dataCont.getActiveMap().getNodes();
  }

  public static void navigateToActiveRequest(ActionEvent e) {
    Group root = new Group();
    Scene scene = ((Button) e.getSource()).getScene();
    try {
      root.getChildren()
          .add(FXMLLoader.load(ViewManager.class.getResource("/fxml/RequestDisplay.fxml")));
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
    scene.setRoot(root);
  }

  /**
   * takes in a node object and assigns / de-assigns it to the selectedNode variable in MapState
   *
   * @param node the node to be assigned or de-assigned
   */
  public static boolean toggleNode(HospitalMapNode node) {
    if (selectedNode == null) {
      selectedNodeMapID = node.getMapID();
      selectedNode = node.getID();
      return true;
    } else if (selectedNode.equals(node)) {
      selectedNodeMapID = null;
      selectedNode = null;
      return false;
    } else {
      dataCont.addEdge(
          node,
          NavDatabaseManager.getInstance()
              .loadMapsFromMemory()
              .get(selectedNodeMapID)
              .getNode(selectedNode));
      selectedNodeMapID = null;
      selectedNode = null;
      return false;
    }
  }

  public static StackPane getReplacePane() {
    return replacePane;
  }

  public static void setReplacePane(StackPane replacePane) {
    ViewManager.replacePane = replacePane;
  }

  public static double getScale() {
    return scale;
  }

  public static Set<HospitalMapNode> getEntityNodes() {
    return dataCont.getActiveMap().getNodes();
  }

  public static void setActiveMap(String mapID) {
    dataCont.setActiveMap(NavDatabaseManager.getInstance().loadMapsFromMemory().get(mapID));
  }

  public static void setActiveMapFromCache(String mapID, Map<String, HospitalMap> cache) {
    dataCont.setActiveMap(cache.get(mapID));
  }

  public static void setMapController(MapController mapController) {
    mapControl = mapController;
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
    if (selectedInActiveMap()) {
      return dataCont.getActiveMap().getNode(selectedNode);
    } else if (selectedNodeMapID != null) {
      return NavDatabaseManager.getInstance()
          .loadMapsFromMemory()
          .get(selectedNodeMapID)
          .getNode(selectedNode);
    }
    return null;
  }

  public static boolean selectedInActiveMap() {
    try {
      dataCont.getActiveMap().getNode(selectedNode);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static MapEditDataController getDataCont() {
    return dataCont;
  }

  public static void setSelectedNode(HospitalMapNode node) {
    if (node == null) {
      selectedNode = null;
    } else {
      ViewManager.selectedNode = node.getID();
    }
  }

  public static ServiceTicket getServiceTicketToShow() {
    return serviceTicketToShow;
  }

  public static void setServiceTicketToShow(ServiceTicket serviceTicketToShow) {
    ViewManager.serviceTicketToShow = serviceTicketToShow;
  }
}
