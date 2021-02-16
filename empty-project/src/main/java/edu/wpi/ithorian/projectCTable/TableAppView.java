package edu.wpi.ithorian.projectCTable;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TableAppView extends Application {

  private TableController tableController;

  @Override
  public void init() {
    System.out.println("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    System.out.println("Table manager: " + tableController);
    Group root = new Group();
    root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/NodeTable.fxml")));
    primaryStage.setTitle("Table View");
    Scene tableScene = new Scene(root, 1000, 400);
    primaryStage.setScene(tableScene);
    System.out.println("Table scene: " + tableScene);
    primaryStage.show();
  }

  @Override
  public void stop() {
    System.out.println("Shutting Down");
  }
}
