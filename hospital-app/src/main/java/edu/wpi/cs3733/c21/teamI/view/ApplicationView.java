package edu.wpi.cs3733.c21.teamI.view;

import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.hospitalMap.LocationNode;
import edu.wpi.cs3733.c21.teamI.user.User;
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

public class ApplicationView {
  private static MapEditManager ourManager;
  private final MapEditManager mapManager;

  @FXML
  Button
          map,
          serviceRequests,
          mapReturn,
          requestsReturn,
          sanitationReturn,
          maintenanceReturn,
          loginReturn,
          maintenance,
          profile;

  @FXML ListView serviceLocationList;
  @FXML ListView startList, destList;

  @FXML TextField requestLocation;

  public ApplicationView() {
    this.mapManager = ourManager;
  }

  public ApplicationView(MapEditManager mapManager) {
    this.mapManager = mapManager;
  }

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
    Group root = mapManager.getRoot();
    Scene scene = ((Button) e.getSource()).getScene();
    root.getChildren().clear();
    if (e.getSource() == mapReturn
        || e.getSource() == requestsReturn
        || e.getSource() == loginReturn) {
      root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Home.fxml")));
    } else if (e.getSource() == sanitationReturn || e.getSource() == maintenanceReturn) {
      root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Requests.fxml")));
    } else if (e.getSource() == map) {
      adminMap = false;
      root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Map.fxml")));
      boolean isAdmin =
          ApplicationDataController.getInstance()
              .getLoggedInUser()
              .hasPermission(User.Permission.EDIT_MAP);
      root.lookup("#adminMapToggle").setVisible(isAdmin);
      root.lookup("#undoButton").setVisible(false);
      root.lookup("#redoButton").setVisible(false);
      setupMapViewHandlers();
    } else if (e.getSource() == serviceRequests) {
      root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Requests.fxml")));
    } else if (e.getSource() == maintenance) {
      root.getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/MaintenanceRequest.fxml")));
    } else if (e.getSource() == profile) {
      root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Profile.fxml")));
      populateTicketsProfile();
    } else {
      root.getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/SanitationRequest.fxml")));
      setupRequestView();
    }
    mapManager.setRoot(root);
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

  public void lookup(KeyEvent e) {
    if (e.getSource() == start) {
      lookupNodes(e, startList, start);
    } else if (e.getSource() == destination) {
      lookupNodes(e, destList, destination);
    } else if (e.getSource() == requestLocation) {
      lookupNodes(e, serviceLocationList, requestLocation);
    }
  }

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
