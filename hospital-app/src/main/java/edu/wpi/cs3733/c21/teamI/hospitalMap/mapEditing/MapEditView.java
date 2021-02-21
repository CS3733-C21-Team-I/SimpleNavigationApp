package edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing;

import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.LocationNode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
  private final MapEditManager mapManager;
  private static MapEditManager ourManager;
  private HospitalMapNode selectedNode;
  private boolean isDrag;

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
    setAddNodeHander();
    if (mapManager.getSelectedNode() != null) {
      mapManager.nodeMenu.setVisible(true);
    }
    mapManager.mapPane.setOnMouseDragReleased(
        t -> {
          System.out.println("drag released");
        });
    update();
  }

  private void setAddNodeHander() {
    EventHandler<? super MouseEvent> eventHandler =
        (EventHandler<MouseEvent>)
            e -> {
              if (e.getButton() == MouseButton.SECONDARY) {
                // ID will be generated later
                mapManager
                    .getDataCont()
                    .addNode(
                        new HospitalMapNode(
                            randomGenerate(),
                            mapManager.getMapID(),
                            (int) ((e.getX() * scale) + 10),
                            (int) ((e.getY() * scale) + 10),
                            new ArrayList<>()));
                update();
              }
            };
    mapManager.mapPane.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
  }

  private String randomGenerate() {
    byte[] array = new byte[10]; // length is bounded by 7
    new Random().nextBytes(array);
    return new String(array, StandardCharsets.UTF_8);
  }

  public void update() {
    System.out.println("update time");
    mapManager.mapPane.getChildren().clear();
    drawSelectedNode();
    for (HospitalMapNode node : mapManager.getEntityNodes()) {
      drawEdges(node);
    }
    for (HospitalMapNode node : mapManager.getEntityNodes()) {
      makeNodeCircle(node);
    }
  }

  private void drawSelectedNode() {
    if (mapManager.getSelectedNode() != null) {
      Circle circle = new Circle();
      circle.setFill(Color.PURPLE);
      circle.setCenterX((mapManager.getSelectedNode().getxCoord() / scale) - 3);
      circle.setCenterY((mapManager.getSelectedNode().getyCoord() / scale) - 3);
      circle.setRadius(20 / scale);
      AnchorPane root = mapManager.mapPane;
      root.getChildren().add(circle);
    }
  }
  /*
   Draws connections between nodes. On hover the color is lighter and there is
   an x icon which will be used for deleting an edge.
  */
  private void drawEdges(HospitalMapNode parent) {
    AnchorPane root = mapManager.mapPane;
    Image xIconImg = new Image("/fxml/fxmlResources/redxicon.png");
    ImageView xMarker = new ImageView();
    xMarker.setImage(xIconImg);
    xMarker.setFitHeight(12);
    xMarker.setFitWidth(12);
    xMarker.setVisible(false);
    root.getChildren().add(xMarker);
    for (HospitalMapNode child : parent.getConnections()) {
      if (mapManager.getEntityNodes().contains(child)) {
        Line line =
            LineBuilder.create()
                .startX((parent.getxCoord()) / scale - 3)
                .startY((parent.getyCoord()) / scale - 3)
                .endX((child.getxCoord()) / scale - 3)
                .endY((child.getyCoord()) / scale - 3)
                .stroke(Color.ORANGE)
                .strokeWidth(10 / scale)
                .build();
        root.getChildren().add(line);
        line.setOnMouseEntered(
            t -> {
              //              line.setStroke(Color.PINK);
              xMarker.setVisible(true);
              xMarker.toFront();
              xMarker.setX(((parent.getxCoord() + child.getxCoord()) / 2) / scale - 7);
              xMarker.setY(((parent.getyCoord() + child.getyCoord()) / 2) / scale - 7);
            });
        line.setOnMouseExited(
            t -> {
              line.setStroke(Color.ORANGE);
              xMarker.setVisible(false);
            });
        line.setOnMouseClicked(
            t -> {
              root.getChildren().remove(line);
              mapManager.getDataCont().deleteEdge(parent.getID(), child.getID());
            });
      }
    }
  }

  private void makeNodeCircle(HospitalMapNode node) {
    Circle circle = new Circle();
    circle.setFill(Color.RED);
    circle.setCenterX((node.getxCoord() / scale) - 3);
    circle.setCenterY((node.getyCoord() / scale) - 3);
    circle.setRadius(12 / scale);
    circle.setOnMouseEntered(
        t -> {
          Circle newCircle =
              (Circle)
                  mapManager
                      .mapPane
                      .getChildren()
                      .get(mapManager.mapPane.getChildren().indexOf(circle));
          newCircle.setFill(Color.YELLOW);
        });

    circle.setOnMouseClicked(
        t -> {
          if (t.getButton() == MouseButton.PRIMARY) {
            System.out.println(circle.onDragDoneProperty());
            if (!isDrag) {
              mapManager.toggleNode(node);
            } else {
              isDrag = false;
            }
            update();
          }
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
          newCircle.setRadius(12 / scale);
        });

    circle.setOnMouseDragged(
        t -> {
          Circle newCircle =
              (Circle)
                  mapManager
                      .mapPane
                      .getChildren()
                      .get(mapManager.mapPane.getChildren().indexOf(circle));
          newCircle.setFill(Color.YELLOW);
          newCircle.setCenterX(t.getSceneX());
          newCircle.setCenterY(t.getSceneY());
          node.setxCoord((int) (t.getX() * 3.05) + 3);
          node.setyCoord((int) (t.getY() * 3.05) + 3);
          mapManager.getDataCont().editNode(node.getID(), (LocationNode) node);
          isDrag = true;
          mapManager.setSelectedNode(null);
          System.out.println("set null");
          System.out.println("Selected Node: " + mapManager.getSelectedNode());
        });
    AnchorPane root = mapManager.mapPane;
    root.getChildren().add(circle);
  }
}
