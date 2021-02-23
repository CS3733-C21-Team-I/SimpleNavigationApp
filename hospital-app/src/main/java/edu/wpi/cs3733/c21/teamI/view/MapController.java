package edu.wpi.cs3733.c21.teamI.view;

import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.hospitalMap.EuclidianDistCalc;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.LocationNode;
import edu.wpi.cs3733.c21.teamI.pathfinding.PathFinder;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.stage.Stage;

public class MapController extends Application {
  boolean adminMap = false;

  @FXML Button save, discard, undoButton, redoButton, adminMapToggle;
  @FXML TextField start, destination;
  @FXML ListView startList, destList;
  private HospitalMapNode movingNode;
  @FXML AnchorPane nodeMenu;
  @FXML Button nodeDeleteButton, saveButton;
  @FXML TextField sNameField, lNameField;
  private boolean isDrag = false;
  @FXML AnchorPane mapPane;

  private final double scale = 3.05;
  private EuclidianDistCalc scorer;

  @FXML
  public void toggleEditMap(ActionEvent e) {
    adminMap = !adminMap;
    System.out.println(adminMap);
    mapPane.setVisible(adminMap);
    save.setVisible(adminMap);
    discard.setVisible(adminMap);
    if (adminMap) {
      ViewManager.getDataCont()
          .setActiveMap(NavDatabaseManager.getInstance().loadMapsFromMemory().get("Faulkner 0"));
      startEditView();
      undoButton.setVisible(true);
      redoButton.setVisible(true);
    } else {
      ViewManager.setNodeMenuVisible(false);
      ViewManager.getDataCont().discardChanges();
      undoButton.setVisible(false);
      redoButton.setVisible(false);
    }
  }

  public void lookup(KeyEvent e) {
    if (e.getSource() == start) {
      ViewManager.lookupNodes(e, startList, start);
    } else {
      ViewManager.lookupNodes(e, destList, destination);
    }
  }

  private void setupMapViewHandlers() {
    startList
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (ChangeListener<String>)
                (ov, oldVal, newVal) -> {
                  start.setText(newVal);
                  startList.setVisible(false);
                });
    destList
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (ChangeListener<String>)
                (ov, oldVal, newVal) -> {
                  destination.setText(newVal);
                  destList.setVisible(false);
                });
    mapPane.setOnMouseClicked(
        (MouseEvent evt) -> {
          if (mapPane != null) {
            startList.setVisible(false);
            destList.setVisible(false);
          }
        });
  }

  public void navigate(ActionEvent e) throws IOException {
    ViewManager.navigate(e);
  }

  @FXML
  public void getDirections(ActionEvent e) {
    String begin = start.getText();
    String end = destination.getText();
    drawPathBetweenNodes(begin, end);
  }

  public LocationNode getNodeByLongName(String longName) {
    for (HospitalMapNode node :
        NavDatabaseManager.getInstance().loadMapsFromMemory().get("Faulkner 0").getNodes()) {
      if (node.getClass() == LocationNode.class
          && ((LocationNode) node).getLongName().equals(longName)) {
        return (LocationNode) node;
      }
    }
    return null;
  }

  @FXML
  public void deletePath() {
    mapPane.getChildren().clear();
  }

  @FXML
  public void drawPathBetweenNodes(String aName, String bName) {
    deletePath();
    mapPane
        .getChildren()
        .removeIf(n -> (n.getClass() == Line.class) || (n.getClass() == Circle.class));

    this.scorer = new EuclidianDistCalc();
    HospitalMapNode nodeA = getNodeByLongName(aName);
    HospitalMapNode nodeB = getNodeByLongName(bName);
    List<HospitalMapNode> aStarPath = PathFinder.findPath(nodeA, nodeB, scorer);
    drawPath(aStarPath);
    drawNode(nodeA, Color.BLUE);
    drawNode(nodeB, Color.BLUE);
  }

  private void drawNode(HospitalMapNode node, Color color) {
    Circle circle =
        new Circle((node.getxCoord() / scale) - 3, (node.getyCoord() / scale), 13 / scale);
    circle.setFill(color);
    mapPane.getChildren().add(circle);
  }

  private void drawEdge(HospitalMapNode start, HospitalMapNode end, Color color) {
    Line line =
        LineBuilder.create()
            .startX((start.getxCoord() / scale) - 3)
            .startY((start.getyCoord() / scale))
            .endX((end.getxCoord() / scale) - 3)
            .endY((end.getyCoord() / scale))
            .stroke(color)
            .strokeWidth(14 / scale)
            .build();
    mapPane.getChildren().add(line);
  }

  public void drawPath(List<HospitalMapNode> path) {
    HospitalMapNode currNode;
    HospitalMapNode nextNode = null;
    for (int i = 0; i < path.size() - 1; i++) {
      currNode = path.get(i);
      nextNode = path.get(i + 1);
      drawEdge(currNode, nextNode, Color.BLUE);
    }
  }

  @FXML
  public void saveChanges() {
    ViewManager.getDataCont().saveChanges();
    adminMap = !adminMap;
    if (adminMap) {
      startEditView();
    } else {
      ViewManager.setNodeMenuVisible(false);
    }
    mapPane.setVisible(adminMap);
    save.setVisible(adminMap);
    discard.setVisible(adminMap);
  }

  @FXML
  public void discardChanges() {
    ViewManager.getDataCont().discardChanges();
    adminMap = !adminMap;
    if (adminMap) {
      startEditView();
    } else {
      ViewManager.setNodeMenuVisible(false);
    }
    mapPane.setVisible(adminMap);
    save.setVisible(adminMap);
    discard.setVisible(adminMap);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    Group root = FXMLLoader.load(getClass().getResource("/fxml/Map.fxml"));
    primaryStage.setTitle("Hospital Map");
    root.getChildren().add(root);
    Scene applicationScene = new Scene(root, 973, 800);
    primaryStage.setScene(applicationScene);
    primaryStage.show();
  }

  public void startEditView() {
    System.out.println("STARTING EDIT VIEW");
    adminMap = false;
    boolean isAdmin =
        ApplicationDataController.getInstance()
            .getLoggedInUser()
            .hasPermission(User.Permission.EDIT_MAP);
    adminMapToggle.setVisible(isAdmin);
    setupMapViewHandlers();
    undoButton.setVisible(false);
    redoButton.setVisible(false);
    undoButton.setOnAction(
        e -> {
          if (ViewManager.getDataCont().isUndoAvailable()) {
            ViewManager.getDataCont().undo();
          }
          update();
        });
    redoButton.setOnAction(
        e -> {
          if (ViewManager.getDataCont().isRedoAvailable()) {
            ViewManager.getDataCont().redo();
          }
          update();
        });
    update();
  }

  private void populateEditNodeMenu(HospitalMapNode node) {
    TextField sNameField = (TextField) nodeMenu.lookup("#sNameField");
    TextField lNameField = (TextField) nodeMenu.lookup("#lNameField");
    try {
      sNameField.setText(((LocationNode) node).getShortName());
      lNameField.setText(((LocationNode) node).getLongName());
    } catch (ClassCastException e) {
      sNameField.setText("");
      lNameField.setText("");
    }
  }

  private void editNodeName(HospitalMapNode node) {
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

  private void setAddNodeHander() {
    EventHandler<? super MouseEvent> eventHandler =
        (EventHandler<MouseEvent>)
            e -> {
              if (e.getButton() == MouseButton.SECONDARY) {
                // definitely need a better way of making an ID
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

  public void update() {
    mapPane.getChildren().clear();
    drawSelectedNode();
    System.out.println(mapPane.getChildren());
    for (HospitalMapNode node : ViewManager.getEntityNodes()) {
      drawEdges(node);
    }
    for (HospitalMapNode node : ViewManager.getEntityNodes()) {
      makeNodeCircle(node);
    }
    if (ViewManager.getDataCont().isUndoAvailable()) {
      undoButton.setOpacity(1);
      System.out.println("making undo full");
    } else {
      undoButton.setOpacity(0.2);
      System.out.println("making undo gray");
    }

    if (ViewManager.getDataCont().isRedoAvailable()) {
      redoButton.setOpacity(1);
      System.out.println("making redo full");
    } else {
      redoButton.setOpacity(0.2);
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
      mapPane.getChildren().add(circle);
    }
  }

  private void drawEdges(HospitalMapNode parent) {
    Image xIconImg = new Image("/fxml/fxmlResources/redxicon.png");
    for (HospitalMapNode child : parent.getConnections()) {
      if (ViewManager.getEntityNodes().contains(child)) {
        ImageView xMarker = new ImageView();
        xMarker.setImage(xIconImg);
        xMarker.setFitHeight(12);
        xMarker.setFitWidth(12);
        xMarker.setVisible(false);
        xMarker.setStyle("-fx-cursor: hand");
        mapPane.getChildren().add(xMarker);
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
        mapPane.getChildren().add(line);
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
              mapPane.getChildren().remove(line);
              ViewManager.getDataCont().deleteEdge(parent.getID(), child.getID());
              update();
            });
        xMarker.setOnMouseEntered(
            t -> {
              xMarker.setVisible(true);
            });
        xMarker.setOnMouseClicked(
            t -> {
              mapPane.getChildren().remove(line);
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
            nodeDeleteButton.setOnAction(
                e -> {
                  ViewManager.toggleNode(node);
                  ViewManager.getDataCont().deleteNode(node.getID());
                  update();
                });

            saveButton.setOnAction(
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
    mapPane.getChildren().add(circle);
  }
}
