package edu.wpi.cs3733.c21.teamI.view;

import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.hospitalMap.EuclidianDistCalc;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.LocationNode;
import edu.wpi.cs3733.c21.teamI.pathfinding.PathFinder;
import edu.wpi.cs3733.c21.teamI.pathfinding.PathPlanningAlgorithm;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
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

// Scaling code is from https://gist.github.com/james-d/ce5ec1fd44ce6c64e81a
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
  @FXML ImageView mapImage;

  private final double scale = 3.05;
  private EuclidianDistCalc scorer;
  private double fullImgWidth = 2989;
  private double fullImgHeight = 2457;
  private double imgWidth = 2989;
  private double imgHeight = 2457;
  private double xOffset = 0;
  private double yOffset = 0;
  private boolean panAllowed = true;

  @FXML
  public void toggleEditMap(ActionEvent e) {
    adminMap = !adminMap;
    save.setVisible(adminMap);
    discard.setVisible(adminMap);
    if (adminMap) {
      ViewManager.getDataCont()
          .setActiveMap(NavDatabaseManager.getInstance().loadMapsFromMemory().get("Faulkner 0"));
      startEditView();
      undoButton.setVisible(true);
      redoButton.setVisible(true);
    } else {
      mapPane.getChildren().clear();
      nodeMenu.setVisible(false);
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
    PathPlanningAlgorithm aStar = new PathFinder();
    List<HospitalMapNode> aStarPath = aStar.findPath(nodeA, nodeB, scorer);
    drawPath(aStarPath);
    drawNode(nodeA, Color.BLUE);
    drawNode(nodeB, Color.BLUE);
  }

  private double transformX(double x) {
    return x * fullImgWidth * fullImgWidth / imgWidth / 100000 / scale
        - xOffset * fullImgWidth / imgWidth / scale;
  }

  private double transformY(double y) {
    return y * fullImgHeight * fullImgHeight / imgHeight / 100000 / scale
        - yOffset * fullImgHeight / imgHeight / scale;
  }

  private void drawNode(HospitalMapNode node, Color color) {
    Circle circle =
        new Circle(
            transformX(node.getxCoord()),
            transformY(node.getyCoord()),
            13 / scale * fullImgHeight / imgHeight);
    circle.setFill(color);
    mapPane.getChildren().add(circle);
  }

  private void drawEdge(HospitalMapNode start, HospitalMapNode end, Color color) {
    Line line =
        LineBuilder.create()
            .startX(transformX(start.getxCoord()))
            .startY(transformY(start.getyCoord()))
            .endX(transformX(end.getxCoord()))
            .endY(transformY(end.getyCoord()))
            .stroke(color)
            .strokeWidth(14 / scale * fullImgHeight / imgHeight)
            .build();
    mapPane.getChildren().add(line);
  }

  private void drawPath(List<HospitalMapNode> path) {
    HospitalMapNode currNode;
    HospitalMapNode nextNode;
    for (int i = 0; i < path.size() - 1; i++) {
      currNode = path.get(i);
      nextNode = path.get(i + 1);
      drawEdge(currNode, nextNode, Color.BLUE);
    }
  }

  @FXML
  public void saveChanges() {
    ViewManager.getDataCont().saveChanges();
    toggleEditMap(new ActionEvent());
  }

  @FXML
  public void discardChanges() {
    ViewManager.getDataCont().discardChanges();
    toggleEditMap(new ActionEvent());
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}

  private void startEditView() {
    setAddNodeHandler();
    nodeMenu.setVisible(ViewManager.getSelectedNode() != null && adminMap);
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

  private void setAddNodeHandler() {
    EventHandler<? super MouseEvent> eventHandler =
        (EventHandler<MouseEvent>)
            e -> {
              if (e.getButton() == MouseButton.SECONDARY) {
                // definitely need a better way of making an ID
                Point2D mousePress = imageViewToImage(mapImage, new Point2D(e.getX(), e.getY()));
                ViewManager.getDataCont()
                    .addNode(
                        new HospitalMapNode(
                            randomGenerate(),
                            ViewManager.getMapID(),
                            (int) (mousePress.getX() / fullImgWidth * 100000),
                            (int) (mousePress.getY() / fullImgHeight * 100000),
                            new ArrayList<>()));
                update();
              }
            };
    mapPane.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
  }

  // TODO NOT THIS ANYTHING BUT THIS
  private static AtomicInteger idGen = new AtomicInteger();

  private String randomGenerate() {
    return "BadSol" + idGen.incrementAndGet();
  }

  private void update() {
    mapPane.getChildren().clear();
    drawSelectedNode();
    for (HospitalMapNode node : ViewManager.getEntityNodes()) {
      drawEdges(node);
    }
    for (HospitalMapNode node : ViewManager.getEntityNodes()) {
      makeNodeCircle(node);
    }
    if (ViewManager.getDataCont().isUndoAvailable()) {
      undoButton.setOpacity(1);
    } else {
      undoButton.setOpacity(0.2);
    }

    if (ViewManager.getDataCont().isRedoAvailable()) {
      redoButton.setOpacity(1);
    } else {
      redoButton.setOpacity(0.2);
    }
  }

  @FXML
  public void initialize() {
    startZoomPan(mapPane);
    setupMapViewHandlers();
    boolean isAdmin =
        ApplicationDataController.getInstance()
            .getLoggedInUser()
            .hasPermission(User.Permission.EDIT_MAP);
    adminMapToggle.setVisible(isAdmin);
  }

  private void drawSelectedNode() {
    if (ViewManager.getSelectedNode() != null) {
      Circle circle = new Circle();
      circle.setFill(Color.PURPLE);
      circle.setCenterX(transformX(ViewManager.getSelectedNode().getxCoord()));
      circle.setCenterY(transformY(ViewManager.getSelectedNode().getyCoord()));
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
                .startX(transformX(parent.getxCoord()))
                .startY(transformY(parent.getyCoord()))
                .endX(transformX(child.getxCoord()))
                .endY(transformY(child.getyCoord()))
                .stroke(Color.ORANGE)
                .strokeWidth(10 / scale)
                .build();
        line.setStyle("-fx-cursor: hand");
        mapPane.getChildren().add(line);
        line.setOnMouseEntered(
            t -> {
              xMarker.setVisible(true);
              xMarker.toFront();
              xMarker.setX(
                  transformX((parent.getxCoord() + child.getxCoord()) / 2)
                      - xMarker.getFitWidth() / 2);
              xMarker.setY(
                  transformY((parent.getyCoord() + child.getyCoord()) / 2)
                      - xMarker.getFitHeight() / 2);
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
    circle.setCenterX(transformX(node.getxCoord()));
    circle.setCenterY(transformY(node.getyCoord()));
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
              nodeMenu.setVisible(ViewManager.toggleNode(node));
            } else {
              panAllowed = true;
              ViewManager.setSelectedNode(null);
              isDrag = false;
              ViewManager.getDataCont().editNode(movingNode.getID(), movingNode);
            }
            nodeDeleteButton.setOnAction(
                e -> {
                  nodeMenu.setVisible(ViewManager.toggleNode(node));
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
          panAllowed = false;
          Circle newCircle =
              (Circle) mapPane.getChildren().get(mapPane.getChildren().indexOf(circle));
          newCircle.setFill(Color.YELLOW);
          newCircle.setCenterX(t.getSceneX());
          newCircle.setCenterY(t.getSceneY());
          Point2D mousePress = imageViewToImage(mapImage, new Point2D(t.getX(), t.getY()));
          HospitalMapNode newNode =
              new HospitalMapNode(
                  node.getID(),
                  node.getMapID(),
                  (int) (mousePress.getX() / fullImgWidth * 100000),
                  (int) (mousePress.getY() / fullImgHeight * 100000),
                  node.getConnections());
          movingNode = newNode;
          isDrag = true;
        });
    mapPane.getChildren().add(circle);
  }

  private static final int MIN_PIXELS = 200;

  private void startZoomPan(AnchorPane zoomPane) {
    double width = imgWidth;
    double height = imgHeight;
    mapImage.setPreserveRatio(true);
    reset(mapImage, width, height);
    ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

    zoomPane.setOnMousePressed(
        e -> {
          if (panAllowed) {
            Point2D mousePress = imageViewToImage(mapImage, new Point2D(e.getX(), e.getY()));
            mouseDown.set(mousePress);
          }
        });

    zoomPane.setOnMouseDragged(
        e -> {
          if (panAllowed) {
            Point2D dragPoint = imageViewToImage(mapImage, new Point2D(e.getX(), e.getY()));
            shift(mapImage, dragPoint.subtract(mouseDown.get()));
            mouseDown.set(imageViewToImage(mapImage, new Point2D(e.getX(), e.getY())));
            xOffset = mapImage.getViewport().getMinX();
            yOffset = mapImage.getViewport().getMinY();
            if (!adminMap) {
              getDirections(new ActionEvent());
            } else {
              update();
            }
          }
        });

    zoomPane.setOnScroll(
        e -> {
          double delta = e.getDeltaY();
          Rectangle2D viewport = mapImage.getViewport();
          double scale =
              clamp(
                  Math.pow(1.001, delta),
                  // don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
                  Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),
                  // don't scale so that we're bigger than image dimensions:
                  Math.max(width / viewport.getWidth(), height / viewport.getHeight()));
          Point2D mouse = imageViewToImage(mapImage, new Point2D(e.getX(), e.getY()));
          double newWidth = viewport.getWidth() * scale;
          double newHeight = viewport.getHeight() * scale;
          double newMinX =
              clamp(
                  mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale, 0, width - newWidth);
          double newMinY =
              clamp(
                  mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale,
                  0,
                  height - newHeight);
          mapImage.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
          imgWidth = mapImage.getViewport().getWidth();
          imgHeight = mapImage.getViewport().getHeight();
          xOffset = mapImage.getViewport().getMinX();
          yOffset = mapImage.getViewport().getMinY();
          if (!adminMap) {
            getDirections(new ActionEvent());
          } else {
            update();
          }
        });
  }

  // reset to the top left:
  private void reset(ImageView imageView, double width, double height) {
    imageView.setViewport(new Rectangle2D(0, 0, width, height));
  }

  // shift the viewport of the imageView by the specified delta, clamping so
  // the viewport does not move off the actual image:
  private void shift(ImageView imageView, Point2D delta) {
    Rectangle2D viewport = imageView.getViewport();
    double width = imageView.getImage().getWidth();
    double height = imageView.getImage().getHeight();
    double maxX = width - viewport.getWidth();
    double maxY = height - viewport.getHeight();
    double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
    double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);
    imageView.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
  }

  private double clamp(double value, double min, double max) {
    return Math.max(min, Math.min(max, value));
  }

  // convert mouse coordinates in the imageView to coordinates in the actual image:
  private Point2D imageViewToImage(ImageView imageView, Point2D imageViewCoordinates) {
    double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
    double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();
    Rectangle2D viewport = imageView.getViewport();
    return new Point2D(
        viewport.getMinX() + xProportion * viewport.getWidth(),
        viewport.getMinY() + yProportion * viewport.getHeight());
  }
}
