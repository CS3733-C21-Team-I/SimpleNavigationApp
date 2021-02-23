package edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing;

import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
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
  private AnchorPane nodeMenuPane;
  private Button deleteBtn;
  private boolean isDrag = false;

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
    loadFXML();
    primaryStage.setTitle("Map Editor");
    primaryStage.show();
    mapManager.setStage(primaryStage);
    deleteBtn = (Button) nodeMenuPane.getChildren().get(11);
    setAddNodeHander();
    nodeMenuPane.setVisible(mapManager.getSelectedNode() != null);
    update();
  }

  private void loadFXML() {
    try {
      nodeMenuPane = FXMLLoader.load(getClass().getResource("/fxml/EditNodePane.fxml"));
      AnchorPane.setBottomAnchor(nodeMenuPane, 50.0);
      AnchorPane.setRightAnchor(nodeMenuPane, 50.0);
      AnchorPane.setLeftAnchor(nodeMenuPane, 5.0);
      AnchorPane.setTopAnchor(nodeMenuPane, 20.0);
      mapManager.getRoot().getChildren().add(nodeMenuPane);
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
    } catch (IOException e) {
      System.out.println("IO Exception");
    }
  }

  public void hideNodeMenu(boolean visible) {
    this.nodeMenuPane.setVisible(visible);
  }

  private void setAddNodeHander() {
    EventHandler<? super MouseEvent> eventHandler =
        (EventHandler<MouseEvent>)
            e -> {
              if (e.getButton() == MouseButton.SECONDARY) {
                // definitely need a better way of making an ID
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

  private void drawEdges(HospitalMapNode parent) {
    AnchorPane root = mapManager.mapPane;
    Image xIconImg = new Image("/fxml/fxmlResources/redxicon.png");

    for (HospitalMapNode child : parent.getConnections()) {
      if (mapManager.getEntityNodes().contains(child)) {
        ImageView xMarker = new ImageView();
        xMarker.setImage(xIconImg);
        xMarker.setFitHeight(12);
        xMarker.setFitWidth(12);
        xMarker.setVisible(false);
        xMarker.setStyle("-fx-cursor: hand");
        root.getChildren().add(xMarker);
        Line line =
            LineBuilder.create()
                .startX((parent.getxCoord()) / scale - 3)
                .startY((parent.getyCoord()) / scale - 3)
                .endX((child.getxCoord()) / scale - 3)
                .endY((child.getyCoord()) / scale - 3)
                .stroke(Color.ORANGE)
                .strokeWidth(10 / scale)
                .build();
        line.setStyle("-fx-cursor: hand");
        root.getChildren().add(line);
        line.setOnMouseEntered(
            t -> {
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
              update();
            });
        xMarker.setOnMouseEntered(
            t -> {
              xMarker.setVisible(true);
            });
        xMarker.setOnMouseClicked(
            t -> {
              root.getChildren().remove(line);
              mapManager.getDataCont().deleteEdge(parent.getID(), child.getID());
              update();
            });
        xMarker.setOnMouseExited(
            t -> {
              xMarker.setVisible(false);
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
          circle.setStyle("-fx-cursor: hand");
        });

    circle.setOnMouseClicked(
        t -> {
          if (t.getButton() == MouseButton.PRIMARY) {
            if (!isDrag) {
              mapManager.toggleNode(node);
            } else {
              mapManager.setSelectedNode(null);
              isDrag = false;
            }
            deleteBtn.setOnAction(
                e -> {
                  mapManager.toggleNode(node);
                  mapManager.getDataCont().deleteNode(node.getID());
                  update();
                });
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
          circle.setStyle("-fx-cursor: default");
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

          HospitalMapNode newNode =
              new HospitalMapNode(
                  node.getID(),
                  node.getMapID(),
                  (int) (t.getX() * scale) + 3,
                  (int) (t.getY() * scale) + 3,
                  node.getConnections());
          mapManager.getDataCont().deleteNode(node.getID());
          mapManager.getDataCont().addNode(newNode);
          isDrag = true;
        });
    AnchorPane root = mapManager.mapPane;
    root.getChildren().add(circle);
  }
}
