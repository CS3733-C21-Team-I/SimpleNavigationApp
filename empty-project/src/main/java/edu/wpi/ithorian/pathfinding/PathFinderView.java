package edu.wpi.ithorian.pathfinding;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PathFinderView extends Application {

  private PathFinderController pathFinderController;

  @Override
  public void init() {
    System.out.println("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    System.out.println("Table manager: " + pathFinderController);
    Group root = new Group();
    root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Map.fxml")));
    primaryStage.setTitle("Pathfinding View");
    Scene pathScene = new Scene(root, 1000, 400);
    primaryStage.setScene(pathScene);
    System.out.println("Pathfinding scene: " + pathScene);
    primaryStage.show();
  }

  @Override
  public void stop() {
    System.out.println("Shutting Down");
  }
}
