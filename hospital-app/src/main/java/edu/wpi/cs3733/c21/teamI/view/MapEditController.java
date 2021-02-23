package edu.wpi.cs3733.c21.teamI.view;

import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.LocationNode;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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

public class MapEditController extends Application {

  private double scale;
  private AnchorPane newLoadedPane;
  private Button deleteBtn, saveBtn;
  private Button undoBtn;
  private Button redoBtn;
  private boolean isDrag = false;
  private HospitalMapNode movingNode;
  private AnchorPane mapPane;

  @Override
  public void init() throws Exception {}

  @Override
  public void start(Stage primaryStage) {
    loadFXML();
    primaryStage.show();
    ViewManager.setStage(primaryStage);
    deleteBtn = (Button) newLoadedPane.lookup("#nodeDeleteButton");
    saveBtn = (Button) newLoadedPane.lookup("#saveButton");
    undoBtn = (Button) ViewManager.getRoot().lookup("#undoButton");
    redoBtn = (Button) ViewManager.getRoot().lookup("#redoButton");
    setAddNodeHander();
    newLoadedPane.setVisible(ViewManager.getSelectedNode() != null);
    undoBtn.setVisible(false);
    redoBtn.setVisible(false);
    undoBtn.setOnAction(
        e -> {
          if (ViewManager.getDataCont().isUndoAvailable()) {
            ViewManager.getDataCont().undo();
          }
          update();
        });
    redoBtn.setOnAction(
        e -> {
          if (ViewManager.getDataCont().isRedoAvailable()) {
            ViewManager.getDataCont().redo();
          }
          update();
        });

    update();
  }

  private void populateEditNodeMenu(HospitalMapNode node) {
    TextField sNameField = (TextField) newLoadedPane.lookup("#sNameField");
    TextField lNameField = (TextField) newLoadedPane.lookup("#lNameField");
    try {
      sNameField.setText(((LocationNode) node).getShortName());
      lNameField.setText(((LocationNode) node).getLongName());
    } catch (ClassCastException e) {
      sNameField.setText("");
      lNameField.setText("");
    }
  }

  private void editNodeName(HospitalMapNode node) {
    TextField sNameField = (TextField) newLoadedPane.lookup("#sNameField");
    TextField lNameField = (TextField) newLoadedPane.lookup("#lNameField");
    String sName = sNameField.getText();
    String lName = lNameField.getText();
    try {
      LocationNode newNode = (LocationNode) node;
      newNode.setShortName(sName);
      newNode.setLongName(lName);
      ViewManager.getDataCont().editNode(node.getID(), newNode);
    } catch (ClassCastException e) {
      LocationNode newNode =
          new LocationNode(
              node.getID(),
              node.getMapID(),
              node.getxCoord(),
              node.getyCoord(),
              sName,
              lName,
              "I",
              node.getConnections());
      ViewManager.getDataCont().deleteNode(node.getID());
      ViewManager.getDataCont().addNode(newNode);
    }
  }

  private void loadFXML() {
    try {
      newLoadedPane = FXMLLoader.load(getClass().getResource("/fxml/EditNodePane.fxml"));
      AnchorPane.setBottomAnchor(newLoadedPane, 0.0);
      AnchorPane.setRightAnchor(newLoadedPane, 0.0);
      ViewManager.getRoot().getChildren().add(newLoadedPane);
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
    } catch (IOException e) {
      System.out.println("IO Exception");
    }
  }

  public void hideNodeMenu(boolean visible) {
    this.newLoadedPane.setVisible(visible);
  }

  private void setAddNodeHander() {
    EventHandler<? super MouseEvent> eventHandler =
        (EventHandler<MouseEvent>)
            e -> {
              if (e.getButton() == MouseButton.SECONDARY) {
                // definitely need a better way of making an ID
                System.out.println("map ID: " + ViewManager.getMapID());
                ViewManager.getDataCont()
                    .addNode(
                        new HospitalMapNode(
                            randomGenerate(),
                            ViewManager.getMapID(),
                            (int) ((e.getX() * scale) + 10),
                            (int) ((e.getY() * scale) + 10),
                            new ArrayList<>()));
                update();
              }
            };
    mapPane.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
  }

  // TODO NOT THIS ANYTHING BUT THIS
  private static AtomicInteger idGen = new AtomicInteger();

  private String randomGenerate() {
    System.out.println("BadSol" + idGen.incrementAndGet());
    return new String("BadSol" + idGen.incrementAndGet());
  }

  public void navigate(ActionEvent e) throws IOException {
    ViewManager.navigate(e);
  }

  public void update() {
    mapPane.getChildren().clear();
    drawSelectedNode();
    for (HospitalMapNode node : ViewManager.getEntityNodes()) {
      drawEdges(node);
    }
    for (HospitalMapNode node : ViewManager.getEntityNodes()) {
      makeNodeCircle(node);
    }
    if (ViewManager.getDataCont().isUndoAvailable()) {
      undoBtn.setOpacity(1);
      System.out.println("making undo full");
    } else {
      undoBtn.setOpacity(0.2);
      System.out.println("making undo gray");
    }

    if (ViewManager.getDataCont().isRedoAvailable()) {
      redoBtn.setOpacity(1);
      System.out.println("making redo full");
    } else {
      redoBtn.setOpacity(0.2);
      System.out.printf("making redo gray");
    }
  }

  private void drawSelectedNode() {
    if (ViewManager.getSelectedNode() != null) {
      Circle circle = new Circle();
      circle.setFill(Color.PURPLE);
      circle.setCenterX((ViewManager.getSelectedNode().getxCoord() / scale) - 3);
      circle.setCenterY((ViewManager.getSelectedNode().getyCoord() / scale) - 3);
      circle.setRadius(20 / scale);
      AnchorPane root = mapPane;
      root.getChildren().add(circle);
    }
  }

  private void drawEdges(HospitalMapNode parent) {
    AnchorPane root = mapPane;
    Image xIconImg = new Image("/fxml/fxmlResources/redxicon.png");

    for (HospitalMapNode child : parent.getConnections()) {
      if (ViewManager.getEntityNodes().contains(child)) {
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
              ViewManager.getDataCont().deleteEdge(parent.getID(), child.getID());
              update();
            });
        xMarker.setOnMouseEntered(
            t -> {
              xMarker.setVisible(true);
            });
        xMarker.setOnMouseClicked(
            t -> {
              root.getChildren().remove(line);
              ViewManager.getDataCont().deleteEdge(parent.getID(), child.getID());
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
              (Circle) mapPane.getChildren().get(mapPane.getChildren().indexOf(circle));
          newCircle.setFill(Color.YELLOW);
          circle.setStyle("-fx-cursor: hand");
        });

    circle.setOnMouseClicked(
        t -> {
          if (t.getButton() == MouseButton.PRIMARY) {
            if (!isDrag) {
              ViewManager.toggleNode(node);
            } else {
              ViewManager.setSelectedNode(null);
              isDrag = false;
              ViewManager.getDataCont().editNode(movingNode.getID(), movingNode);
            }
            deleteBtn.setOnAction(
                e -> {
                  ViewManager.toggleNode(node);
                  ViewManager.getDataCont().deleteNode(node.getID());
                  update();
                });

            saveBtn.setOnAction(
                e -> {
                  editNodeName(node);
                  update();
                });

            populateEditNodeMenu(node);

            update();
          }
        });

    circle.setOnMouseExited(
        t -> {
          Circle newCircle =
              (Circle) mapPane.getChildren().get(mapPane.getChildren().indexOf(circle));
          newCircle.setFill(Color.RED);
          newCircle.setRadius(12 / scale);
          circle.setStyle("-fx-cursor: default");
        });

    circle.setOnMouseDragged(
        t -> {
          Circle newCircle =
              (Circle) mapPane.getChildren().get(mapPane.getChildren().indexOf(circle));
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
          movingNode = newNode;
          isDrag = true;
        });
    AnchorPane root = mapPane;
    root.getChildren().add(circle);
  }
}
