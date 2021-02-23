package edu.wpi.cs3733.c21.teamI.view;

import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.hospitalMap.LocationNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ViewManager {
  private static MapEditManager ourManager;

  public ViewManager() { }

  public static void saveManager() {
    ourManager = new MapEditManager().getInstance();
  }

//  @Override
//  public void init() {}

//  @Override
//  public void start(Stage primaryStage) throws IOException {
//    Group root = mapManager.getRoot();
//    root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Home.fxml")));
//    primaryStage.setTitle("Home");
//    mapManager.setRoot(root);
//    Scene applicationScene = new Scene(root, 973, 800);
//    primaryStage.setScene(applicationScene);
//    mapManager.setStage(primaryStage);
//    primaryStage.show();
//  }

  public static void navigate(ActionEvent e) throws IOException {
    Group root = ourManager.getRoot();
    Scene scene = ((Button) e.getSource()).getScene();
    String id = ((Button) e.getSource()).getId();
    root.getChildren().clear();
    if (id.equals("mapReturn") || id.equals("requestsReturn") || id.equals("loginReturn")) {
      root.getChildren().add(FXMLLoader.load(ViewManager.class.getResource("/fxml/Home.fxml")));
    } else if (id.equals("sanitationReturn") || id.equals("maintenanceReturn")) {
      root.getChildren().add(FXMLLoader.load(ViewManager.class.getResource("/fxml/Requests.fxml")));
    } else if (id.equals("map")) {
      //adminMap = false;
      root.getChildren().add(FXMLLoader.load(ViewManager.class.getResource("/fxml/Map.fxml")));
//      boolean isAdmin =
//          ApplicationDataController.getInstance()
//              .getLoggedInUser()
//              .hasPermission(User.Permission.EDIT_MAP);
//      root.lookup("#adminMapToggle").setVisible(isAdmin);
//      root.lookup("#undoButton").setVisible(false);
//      root.lookup("#redoButton").setVisible(false);
//      setupMapViewHandlers();
    } else if (id.equals("serviceRequests")) {
      root.getChildren().add(FXMLLoader.load(ViewManager.class.getResource("/fxml/Requests.fxml")));
    } else if (id.equals("maintenance")) {
      root.getChildren().add(FXMLLoader.load(ViewManager.class.getResource("/fxml/MaintenanceRequest.fxml")));
    } else if (id.equals("profile")) {
      root.getChildren().add(FXMLLoader.load(ViewManager.class.getResource("/fxml/Profile.fxml")));
//      populateTicketsProfile();
    } else {
      root.getChildren().add(FXMLLoader.load(ViewManager.class.getResource("/fxml/SanitationRequest.fxml")));
//      setupRequestView();
    }
    ourManager.setRoot(root);
    scene.setRoot(root);
  }

//  @FXML
//  public void initialize() {
//    if (serviceRequests != null) {
//      if (ApplicationDataController.getInstance()
//          .getLoggedInUser()
//          .hasPermission(User.Permission.VIEW_TICKET)) {
//
//        serviceRequests.setMaxWidth(map.getMaxWidth());
//        serviceRequests.setVisible(true);
//      } else {
//        serviceRequests.setMaxWidth(0);
//        serviceRequests.setVisible(false);
//      }
//    }
//    initClock();
//  }

  public void lookupNodes(KeyEvent e, ListView listView, TextField target) {
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
}
