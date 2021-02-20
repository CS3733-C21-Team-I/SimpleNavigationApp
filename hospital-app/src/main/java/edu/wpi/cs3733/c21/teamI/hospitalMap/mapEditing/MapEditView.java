package edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing;

import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.stage.Stage;

public class MapEditView extends Application {

  private int scale = 2;
  private MapEditManager mapManager;
  private Scene scene;
  private static MapEditManager ourManager;

  public MapEditView() {
    this.mapManager = ourManager;
  }

  public MapEditView(MapEditManager mapManager) {
    this.mapManager = mapManager;
    this.scale = mapManager.getScale();
  }

  public static void saveManager() {
    ourManager = new MapEditManager().getInstance();
  }

  @Override
  public void init() throws Exception {}

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Map Editor");
    primaryStage.show();
    mapManager.setStage(primaryStage);
    update();
  }

  public void update() {
    for (HospitalMapNode node : mapManager.getEntityNodes()) {
      drawEdges(node);
    }
    for (HospitalMapNode node : mapManager.getEntityNodes()) {
      makeNodeCircle(node);
    }
  }

  private void onRightClick(MouseEvent e, HospitalMapNode node) {
    if (e.getButton() == MouseButton.SECONDARY) {
      mapManager.deleteNode(node.getID());
      update();
    }
  }

  private void drawEdges(HospitalMapNode parent) {
    Group root = mapManager.getRoot();
    for (HospitalMapNode child : parent.getConnections()) {
      if (mapManager.getEntityNodes().contains(child)) {
        Line line =
            LineBuilder.create()
                .startX((parent.getxCoord() - 5) / scale)
                .startY((parent.getyCoord() - 5) / scale)
                .endX((child.getxCoord() - 5) / scale)
                .endY((child.getyCoord() - 5) / scale)
                .stroke(Color.RED)
                .strokeWidth(14 / scale)
                .build();
        root.getChildren().add(line);
      }
    }
  }

  private void makeNodeCircle(HospitalMapNode node) {
    Circle circle = new Circle();
    circle.setFill(Color.RED);
    circle.setCenterX(node.getxCoord() / scale - 5);
    circle.setCenterY(node.getyCoord() / scale - 5);
    circle.setRadius(14 / scale);
    circle.setOnMousePressed(
        e -> {
          onRightClick(e, node);
        });
    circle.setOnMouseEntered(
        t -> {
          Circle newCircle =
              (Circle)
                  mapManager
                      .getRoot()
                      .getChildren()
                      .get(mapManager.getRoot().getChildren().indexOf(circle));
          newCircle.setFill(Color.PINK);
        });

    circle.setOnMouseExited(
        t -> {
          Circle newCircle =
              (Circle)
                  mapManager
                      .getRoot()
                      .getChildren()
                      .get(mapManager.getRoot().getChildren().indexOf(circle));
          newCircle.setFill(Color.RED);
        });
    Group root = mapManager.getRoot();
    root.getChildren().add(circle);
  }
}
