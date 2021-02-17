package edu.wpi.ithorian.projectCTable;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TableAppView extends Application {

  private NodeTableController nodeTableController;
  private StackPane rootNodeLayout,
                    rootEdgeLayout;

  @Override
  public void init() {
    System.out.println("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
//    Group root = new Group();
    FXMLLoader nodePaneLoader = new FXMLLoader();
    URL nodeLoadPath = getClass().getResource("/fxml/NodeTable.fxml");
    nodePaneLoader.setLocation(nodeLoadPath);
    rootNodeLayout = (StackPane) nodePaneLoader.load();
//    root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/NodeTable.fxml")));
    primaryStage.setTitle("Node Editor");
    Scene tableScene = new Scene(rootNodeLayout, 1000, 400);
    primaryStage.setScene(tableScene);
    primaryStage.show();

    Stage stage2 = new Stage();
//    Group root2 = new Group();
    FXMLLoader edgePaneLoader = new FXMLLoader();
    URL edgeLoadPath = getClass().getResource("/fxml/EdgeTable.fxml");
    edgePaneLoader.setLocation(edgeLoadPath);
    rootEdgeLayout = (StackPane) edgePaneLoader.load();
//    root2.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/EdgeTable.fxml")));
    stage2.setTitle("Edge Editor");
    Scene tableScene2 = new Scene(rootEdgeLayout, 1000, 400);
    stage2.setScene(tableScene2);
    stage2.show();
  }

  @Override
  public void stop() {
    System.out.println("Shutting Down");
  }
}
