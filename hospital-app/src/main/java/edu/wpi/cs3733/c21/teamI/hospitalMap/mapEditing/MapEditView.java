package edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing;

import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import javafx.application.Application;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.stage.Stage;

public class MapEditView extends Application {

  private double scale;
  private MapEditManager mapManager;
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
    AnchorPane root = mapManager.mapPane;
    for (HospitalMapNode child : parent.getConnections()) {
      if (mapManager.getEntityNodes().contains(child)) {
        Line line =
            LineBuilder.create()
                .startX((parent.getxCoord()) / scale - 3)
                .startY((parent.getyCoord()) / scale - 3)
                .endX((child.getxCoord()) / scale - 3)
                .endY((child.getyCoord()) / scale - 3)
                .stroke(Color.RED)
                .strokeWidth(10 / scale)
                .build();
        root.getChildren().add(line);
      }
    }
  }

  private void makeNodeCircle(HospitalMapNode node) {
    Circle circle = new Circle();
    circle.setFill(Color.RED);
    circle.setCenterX((node.getxCoord() / scale) - 3);
    circle.setCenterY((node.getyCoord() / scale) - 3);
    circle.setRadius(12 / scale);
    circle.setOnMousePressed(
        e -> {
          onRightClick(e, node);
        });
    circle.setOnMouseEntered(
        t -> {
          Circle newCircle =
              (Circle)
                  mapManager
                      .mapPane
                      .getChildren()
                      .get(mapManager.mapPane.getChildren().indexOf(circle));
          newCircle.setFill(Color.PINK);
        });

    circle.setOnMouseExited(
        t -> {
          Circle newCircle =
              (Circle)
                  mapManager
                      .mapPane
                      .getChildren()
                      .get(mapManager.mapPane.getChildren().indexOf(circle));
          newCircle.setFill(Color.RED);
        });
    AnchorPane root = mapManager.mapPane;
    root.getChildren().add(circle);
  }
}
