package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.hospitalMap.EuclidianDistCalc;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.LocationNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.NodeRestrictions;
import edu.wpi.cs3733.c21.teamI.pathfinding.PathFinder;
import edu.wpi.cs3733.c21.teamI.pathfinding.PathPlanningAlgorithm;
import edu.wpi.cs3733.c21.teamI.pathfinding.TextDirections;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
  @FXML JFXComboBox algorithmPick;
  @FXML TextField sNameField, lNameField;
  private boolean isDrag = false;
  private boolean isFirstLoad = true;
  @FXML AnchorPane mapPane;
  @FXML ImageView mapImage;
  @FXML StackPane imageContainer;
  @FXML VBox stackContainer;
  @FXML Tab campus;
  @FXML Tab floor1;
  @FXML Tab floor2;
  @FXML Tab floor3;
  @FXML Tab floor4;
  @FXML Tab floor6;
  private Tab currentTab = null;

  private final double scale = 3.05;
  private EuclidianDistCalc scorer = new EuclidianDistCalc();
  private double fullImgWidth = 0;
  private double fullImgHeight = 0;
  private double imgWidth = 0;
  private double imgHeight = 0;
  private List<HospitalMapNode> aStarPath;
  private double xOffset = 0;
  private double yOffset = 0;
  private boolean panAllowed = true;

  public void updateView() {
    if (adminMap) {
      startEditView();
    } else {
      try {
        Image background =
            new Image(
                (getClass()
                        .getResource(
                            "/fxml/mapImages/" + ViewManager.getMapID().replace(" ", "") + ".png"))
                    .toURI()
                    .toString());
        mapImage.setImage(background);
        fullImgWidth = background.getWidth();
        fullImgHeight = background.getHeight();
        imgWidth = background.getWidth();
        imgHeight = background.getHeight();
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
      mapPane.getChildren().clear();
      if (this.aStarPath != null) {
        drawAStarPath();
      }
    }
  }

  @FXML
  public void toggleEditMap(ActionEvent e) {
    adminMap = !adminMap;
    algorithmPick.setVisible(adminMap);
    mapPane.getChildren().clear();
    save.setVisible(adminMap);
    discard.setVisible(adminMap);
    if (adminMap) {
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

  private double transformX(double x) {
    return x * (fullImgWidth / imgWidth) * mapPane.getPrefWidth() / 100000
        - xOffset * mapPane.getPrefWidth() / imgWidth;
  }

  private double transformY(double y) {
    return y * (fullImgHeight / imgHeight) * mapPane.getPrefHeight() / 100000
        - yOffset * mapPane.getPrefHeight() / imgHeight;
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
    if (begin.length() > 0 && end.length() > 0) {
      System.out.println(begin + " " + end);

      HospitalMapNode nodeA = getNodeByLongName(begin);
      HospitalMapNode nodeB = getNodeByLongName(end);
      getNewPath(nodeA, nodeB);
      drawAStarPath();
    }
  }

  public LocationNode getNodeByLongName(String longName) {
    for (HospitalMapNode node : ViewManager.getAllNodesSet()) {
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

  public void getNewPath(HospitalMapNode nodeA, HospitalMapNode nodeB) {
    PathPlanningAlgorithm aStar = new PathFinder();
    this.aStarPath = aStar.findPath(nodeA, nodeB, scorer);
    System.out.println(TextDirections.getDirections(scorer, aStarPath));
  }

  @FXML
  public void drawAStarPath() {
    deletePath();
    HospitalMapNode nodeA = this.aStarPath.get(0);
    HospitalMapNode nodeB = this.aStarPath.get(this.aStarPath.toArray().length - 1);
    mapPane
        .getChildren()
        .removeIf(n -> (n.getClass() == Line.class) || (n.getClass() == Circle.class));
    drawPath(this.aStarPath);
    if (nodeA.getMapID().equals(ViewManager.getMapID())) drawNode(nodeA, Color.BLUE);
    if (nodeB.getMapID().equals(ViewManager.getMapID())) drawNode(nodeB, Color.BLUE);
  }

  private void drawNode(HospitalMapNode node, Color color) {
    Circle circle =
        makeCircle(
            transformX(node.getxCoord()),
            transformY(node.getyCoord()),
            13 / scale * fullImgHeight / imgHeight,
            color);
    mapPane.getChildren().add(circle);
  }

  private void drawEdge(HospitalMapNode start, HospitalMapNode end, Color color) {
    Line line =
        LineBuilder.create()
            .startX(clamp(transformX(start.getxCoord()), 0, mapPane.getPrefWidth()))
            .startY(clamp(transformY(start.getyCoord()), 0, mapPane.getPrefHeight()))
            .endX(clamp(transformX(end.getxCoord()), 0, mapPane.getPrefWidth()))
            .endY(clamp(transformY(end.getyCoord()), 0, mapPane.getPrefHeight()))
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
      if (nextNode.getMapID().equals(ViewManager.getMapID())
          && currNode.getMapID().equals(ViewManager.getMapID())) {
        drawEdge(currNode, nextNode, Color.BLUE);
      }
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

  void startEditView() {
    if (isFirstLoad) {
      isFirstLoad = false;
      setAddNodeHandler();
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
    }
    try {
      Image background =
          new Image(
              (getClass()
                      .getResource(
                          "/fxml/mapImages/" + ViewManager.getMapID().replace(" ", "") + ".png"))
                  .toURI()
                  .toString());
      mapImage.setImage(background);
      fullImgWidth = background.getWidth();
      fullImgHeight = background.getHeight();
      imgWidth = background.getWidth();
      imgHeight = background.getHeight();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    nodeMenu.setVisible(
        ViewManager.selectedInActiveMap() && ViewManager.getSelectedNode() != null && adminMap);
    undoButton.setVisible(false);
    redoButton.setVisible(false);
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
      ViewManager.getDataCont().editNode(node.getID(), newNode);
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
    floor1Tab(new ActionEvent());
    campusTab(new ActionEvent());
    ViewManager.setMapController(this);
    setupMapViewHandlers();
    boolean isAdmin =
        ApplicationDataController.getInstance()
            .getLoggedInUser()
            .hasPermission(User.Permission.EDIT_MAP);
    adminMapToggle.setVisible(isAdmin);
    algorithmPick.setVisible(isAdmin);
  }

  private void drawSelectedNode() {
    if (ViewManager.selectedInActiveMap()) {
      Circle circle =
          makeCircle(
              transformX(ViewManager.getSelectedNode().getxCoord()),
              transformY(ViewManager.getSelectedNode().getyCoord()),
              20 / scale,
              Color.PURPLE);
      mapPane.getChildren().add(circle);
    }
  }

  private void drawEdges(HospitalMapNode parent) {
    Image xIconImg = new Image("/fxml/fxmlResources/redxicon.png");
    for (HospitalMapNode child : parent.getConnections()) {
      if (ViewManager.getEntityNodes().contains(child)
          && ViewManager.getEntityNodes().contains(parent)) {
        ImageView xMarker = new ImageView();
        xMarker.setImage(xIconImg);
        xMarker.setFitHeight(12);
        xMarker.setFitWidth(12);
        xMarker.setVisible(false);
        xMarker.setStyle("-fx-cursor: hand");
        mapPane.getChildren().add(xMarker);
        Line line =
            LineBuilder.create()
                .startX(clamp(transformX(parent.getxCoord()), 0, mapPane.getPrefWidth()))
                .startY(clamp(transformY(parent.getyCoord()), 0, mapPane.getPrefHeight()))
                .endX(clamp(transformX(child.getxCoord()), 0, mapPane.getPrefWidth()))
                .endY(clamp(transformY(child.getyCoord()), 0, mapPane.getPrefHeight()))
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
              ViewManager.getDataCont().deleteEdge(parent, child);
              update();
            });
        xMarker.setOnMouseEntered(
            t -> {
              xMarker.setVisible(true);
            });
        xMarker.setOnMouseClicked(
            t -> {
              mapPane.getChildren().remove(line);
              ViewManager.getDataCont().deleteEdge(parent, child);
              update();
            });
        xMarker.setOnMouseExited(
            t -> {
              xMarker.setVisible(false);
            });
      } else {
        ImageView xMarker = new ImageView();
        xMarker.setImage(xIconImg);
        xMarker.setFitHeight(12);
        xMarker.setFitWidth(12);
        xMarker.setVisible(false);
        xMarker.setStyle("-fx-cursor: hand");
        mapPane.getChildren().add(xMarker);
        HospitalMapNode nodeInMap = ViewManager.getAllNodesSet().contains(parent) ? parent : child;
        Line line =
            LineBuilder.create()
                .startX(clamp(transformX(nodeInMap.getxCoord()), 0, mapPane.getPrefWidth()))
                .startY(clamp(transformY(nodeInMap.getyCoord()), 0, mapPane.getPrefHeight()))
                .endX(
                    clamp(
                        transformX(nodeInMap.getxCoord() - mapPane.getPrefHeight()),
                        0,
                        mapPane.getPrefWidth()))
                .endY(
                    clamp(
                        transformY(nodeInMap.getyCoord() - mapPane.getPrefHeight()),
                        0,
                        mapPane.getPrefHeight()))
                .stroke(Color.GREEN)
                .strokeWidth(10 / scale)
                .build();
        line.setStyle("-fx-cursor: hand");
        mapPane.getChildren().add(line);
        line.setOnMouseEntered(
            t -> {
              xMarker.setVisible(true);
              xMarker.toFront();
              xMarker.setX(
                  transformX(nodeInMap.getxCoord() - (mapPane.getPrefHeight() / 2))
                      - xMarker.getFitWidth() / 2);
              xMarker.setY(
                  transformY(nodeInMap.getyCoord() - (mapPane.getPrefHeight() / 2))
                      - xMarker.getFitHeight() / 2);
            });
        line.setOnMouseExited(
            t -> {
              xMarker.setVisible(false);
            });
        line.setOnMouseClicked(
            t -> {
              mapPane.getChildren().remove(line);
              ViewManager.getDataCont().deleteEdge(parent, child);
              update();
            });
        xMarker.setOnMouseEntered(
            t -> {
              xMarker.setVisible(true);
            });
        xMarker.setOnMouseClicked(
            t -> {
              mapPane.getChildren().remove(line);
              ViewManager.getDataCont().deleteEdge(parent, child);
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
    for (HospitalMapNode child : node.getConnections()) {
      if (!node.getMapID().equals(child.getMapID())) {
        Circle highlight =
            makeCircle(
                transformX(node.getxCoord()),
                transformY(node.getyCoord()),
                20 / scale,
                Color.GREEN);
        mapPane.getChildren().add(highlight);
      }
    }
    Circle circle =
        makeCircle(
            transformX(node.getxCoord()), transformY(node.getyCoord()), 12 / scale, Color.RED);
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
              Point2D mousePress = imageViewToImage(mapImage, new Point2D(t.getX(), t.getY()));
              movingNode.setxCoord((int) (mousePress.getX() / fullImgWidth * 100000));
              movingNode.setyCoord((int) (mousePress.getY() / fullImgHeight * 100000));
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
          newCircle.setCenterX(t.getX());
          newCircle.setCenterY(t.getY());
          HospitalMapNode newNode =
              new HospitalMapNode(node.getID(), node.getMapID(), 0, 0, node.getConnections());
          movingNode = newNode;
          isDrag = true;
        });
    mapPane.getChildren().add(circle);
  }

  private Circle makeCircle(double x, double y, double r, Color color) {
    Circle returnCircle = new Circle(x, y, r);
    returnCircle.setFill(color);
    if (x <= 0 || x >= mapPane.getPrefWidth() || y <= 0 || y >= mapPane.getPrefHeight()) {
      returnCircle.setVisible(false);
    }
    return returnCircle;
  }

  @FXML
  public void onSwitch() {
    String begin = start.getText();
    String end = destination.getText();
    start.setText(end);
    destination.setText(begin);
  }

  @FXML
  public void onClear() {
    start.setText("");
    destination.setText("");
    this.aStarPath = null;
    deletePath();
  }

  @FXML
  public void toggleAccessible(ActionEvent e) {
    if (this.scorer.nodeTypesToAvoid.size() > 0) {
      this.scorer.nodeTypesToAvoid.clear();
    } else {
      this.scorer.nodeTypesToAvoid.add(NodeRestrictions.WHEELCHAIR_INACCESSIBLE);
    }
    System.out.print("NodeRestrictions:" + this.scorer.nodeTypesToAvoid);
  }

  public void campusTab(Event event) {
    if (campus != currentTab && currentTab != null) {
      System.out.println("Tab 1");
      ViewManager.setActiveMap("Faulkner 0");
      updateView();
      currentTab = campus;
      startZoomPan(mapPane);
      resize();
    }
  }

  public void floor1Tab(Event event) {
    if (floor1 != currentTab) {
      System.out.println("Tab 2");
      ViewManager.setActiveMap("Faulkner 1");
      updateView();
      currentTab = floor1;
      startZoomPan(mapPane);
      resize();
    }
  }

  public void floor2Tab(Event event) {
    if (floor2 != currentTab) {
      System.out.println("Tab 3");
      ViewManager.setActiveMap("Faulkner 2");
      updateView();
      currentTab = floor2;
      startZoomPan(mapPane);
      resize();
    }
  }

  public void floor3Tab(Event event) {
    if (floor3 != currentTab) {

      System.out.println("Tab 4");
      ViewManager.setActiveMap("Faulkner 3");
      updateView();
      currentTab = floor3;
      startZoomPan(mapPane);
      resize();
    }
  }

  public void floor4Tab(Event event) {
    if (floor4 != currentTab) {
      System.out.println("Tab 5");
      ViewManager.setActiveMap("Faulkner 4");
      updateView();
      currentTab = floor4;
      startZoomPan(mapPane);
      resize();
    }
  }

  public void floor5Tab(Event event) {
    if (floor6 != currentTab) {
      System.out.println("Tab 6");
      ViewManager.setActiveMap("Faulkner 5");
      updateView();
      currentTab = floor6;
      startZoomPan(mapPane);
      resize();
    }
  }

  // Scaling code is from https://gist.github.com/james-d/ce5ec1fd44ce6c64e81a
  private static final int MIN_PIXELS = 200;

  private void startZoomPan(AnchorPane zoomPane) {
    mapImage.fitWidthProperty().bind(imageContainer.widthProperty());
    mapImage.fitHeightProperty().bind(imageContainer.heightProperty());
    mapImage.setPreserveRatio(true);
    double width = imgWidth;
    double height = imgHeight;
    reset(mapImage, width, height);
    ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

    mapImage
        .fitWidthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              resize();
            });
    mapImage
        .fitHeightProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              resize();
            });

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
            if (!start.getText().equals("") && !destination.getText().equals("")) {
              getDirections(new ActionEvent());
            }
          } else {
            update();
          }
        });
  }

  private void resize() {
    if (mapImage != null && mapImage.getFitWidth() > 0) {
      if (imageContainer.getHeight() / imageContainer.getWidth() > fullImgHeight / fullImgWidth) {
        mapPane.setPrefWidth(mapImage.getFitWidth());
        mapPane.setMaxWidth(mapImage.getFitWidth());
        mapPane.setPrefHeight(mapImage.getFitWidth() * imgHeight / imgWidth);
        mapPane.setMaxHeight(mapImage.getFitWidth() * imgHeight / imgWidth);
      } else {
        mapPane.setPrefHeight(mapImage.getFitHeight());
        mapPane.setMaxHeight(mapImage.getFitHeight());
        mapPane.setPrefWidth(mapImage.getFitHeight() * imgWidth / imgHeight);
        mapPane.setMaxWidth(mapImage.getFitHeight() * imgWidth / imgHeight);
      }
      updateView();
    }
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
