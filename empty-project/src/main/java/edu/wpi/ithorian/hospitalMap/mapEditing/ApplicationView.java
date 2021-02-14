package edu.wpi.ithorian.hospitalMap.mapEditing;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ApplicationView extends Application {
  @FXML
  Button map,
      serviceRequests,
      mapReturn,
      requestsReturn,
      sanitationReturn,
      maintenanceReturn,
      maintenance,
      start1,
      start2,
      start3,
      start4,
      start5,
      dest1,
      dest2,
      dest3,
      dest4,
      dest5;
  @FXML ImageView mapImage, adminPath;
  @FXML HBox destinationPoint;
  @FXML TextField start, destination;
  @FXML Label dateTime;
  @FXML AnchorPane nodeMenu;

  boolean adminMap = false;
  private static MapEditManager ourManager;
  private MapEditManager mapManager;

  public ApplicationView() {
    this.mapManager = ourManager;
  }

  public ApplicationView(MapEditManager mapManager) {
    this.mapManager = mapManager;
  }

  public static void saveManager() {
    ourManager = new MapEditManager().getInstance();
  }

  @Override
  public void init() {
    System.out.println("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    System.out.println("Application manager: " + mapManager);
    Group root = mapManager.getRoot();
    root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Home.fxml")));
    primaryStage.setTitle("Map View");
    mapManager.setRoot(root);
    Scene applictionScene = new Scene(root, 973, 800);
    primaryStage.setScene(applictionScene);
    mapManager.setStage(primaryStage);
    System.out.println("Application scene: " + applictionScene);
    primaryStage.show();
  }

  @FXML
  public void navigate(ActionEvent e) throws IOException {
    Group root = mapManager.getRoot();
    Scene scene = ((Button) e.getSource()).getScene();
    root.getChildren().clear();
    if (e.getSource() == mapReturn || e.getSource() == requestsReturn) {
      root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Home.fxml")));
    } else if (e.getSource() == sanitationReturn || e.getSource() == maintenanceReturn) {
      root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Requests.fxml")));
    } else if (e.getSource() == map) {
      adminMap = false;
      root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Map.fxml")));
    } else if (e.getSource() == serviceRequests) {
      root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Requests.fxml")));
    } else if (e.getSource() == maintenance) {
      root.getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/MaintenanceRequest.fxml")));
    } else {
      root.getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/SanitationRequest.fxml")));
    }
    mapManager.setRoot(root);
    scene.setRoot(root);
  }

  @FXML
  public void initialize() {
    initClock();
  }

  @FXML
  public void initClock() {
    Timeline clock =
        new Timeline(
            new KeyFrame(
                Duration.ZERO,
                e -> {
                  DateTimeFormatter formatter =
                      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // "yyyy-MM-dd HH:mm:ss"
                  if (dateTime != null) {
                    dateTime.setText(LocalDateTime.now().format(formatter));
                  }
                }),
            new KeyFrame(Duration.seconds(1)));
    clock.setCycleCount(Animation.INDEFINITE);
    clock.play();
  }

  public void lookup(KeyEvent e) {
    String matchString =
        (((TextField) e.getSource()).getText()
            + (!e.getCharacter().equals(Character.toString((char) 8)) ? e.getCharacter() : "")
                .toLowerCase());
    ArrayList<String> locations;
    // this is janky but intelliJ formatted it horribly, temporary solution
    {
      locations =
          new ArrayList<>(
              Arrays.asList(
                  "Admitting",
                  "Blood Draw Lab",
                  "Cardiac Rehab",
                  "Elevator X",
                  "Elevator Y",
                  "Elevator Z",
                  "Emergency Department",
                  "Endoscopy",
                  "Hall 1",
                  "Hall 2",
                  "Hall 3",
                  "Hall 4",
                  "Hall 5",
                  "Hall 6",
                  "Hall 7",
                  "Hall 8",
                  "Hall 9",
                  "Hall 10",
                  "Hall 11",
                  "Hall 12",
                  "Hall 13",
                  "Hall 14",
                  "Hall 15",
                  "Hall 16",
                  "Hall 17",
                  "Hall 18",
                  "Hall 19",
                  "Hall 20",
                  "Hall 21",
                  "Hall 22",
                  "Hall 23",
                  "Hall 24",
                  "Hall 25",
                  "Hall 26",
                  "Hall 27",
                  "Hall 28",
                  "Hall 29",
                  "Hall 30",
                  "Hall 31",
                  "Navigation Kiosk",
                  "LAB",
                  "Linens",
                  "Atrium Main Lobby",
                  "MRI/CT",
                  "Operation Room",
                  "Operation Rooms",
                  "Pre-Admittance Screening",
                  "PULM Lab",
                  "QR MAT Management",
                  "Radiology",
                  "Recovery",
                  "Restroom A",
                  "Restroom B",
                  "Starbucks",
                  "Special testing",
                  "Staircase C",
                  "Staircase D",
                  "Staircase E",
                  "Staircase F",
                  "VASC Lab"));
    }
    ArrayList<String> matches = new ArrayList<>();
    for (String location : locations) {
      if (location.toLowerCase().contains(matchString)) {
        matches.add(location);
      }
    }

    clearMenus();
    for (int i = 0; i < matches.size(); i++) {
      if (i < 5) {
        getMenuButton(i, e.getSource() == start).setVisible(true);
        getMenuButton(i, e.getSource() == start).setText(matches.get(i));
      } else {
        break;
      }
    }
  }

  @FXML
  private Button getMenuButton(int index, boolean isStart) {
    switch (index) {
      case 0:
        return isStart ? start1 : dest1;
      case 1:
        return isStart ? start2 : dest2;
      case 2:
        return isStart ? start3 : dest3;
      case 3:
        return isStart ? start4 : dest4;
      default:
        return isStart ? start5 : dest5;
    }
  }

  @FXML
  private void clearMenus() {
    for (int i = 0; i < 5; i++) {
      getMenuButton(i, true).setVisible(false);
      getMenuButton(i, false).setVisible(false);
    }
  }

  @FXML
  public void autoFillStart(MouseEvent e) {
    start.setText(((Button) e.getSource()).getText());
  }

  @FXML
  public void autoFillDest(MouseEvent e) {
    destination.setText(((Button) e.getSource()).getText());
  }

  @FXML
  public void toggleEditMap(ActionEvent e) throws IOException {
    mapManager.startEditorView();
    adminMap = !adminMap;
    if (adminMap) {
      //      adminPath.setVisible(true);
      nodeMenu.setVisible(true);
    } else {
      //      adminPath.setVisible(false);
      nodeMenu.setVisible(false);
    }
  }
}
