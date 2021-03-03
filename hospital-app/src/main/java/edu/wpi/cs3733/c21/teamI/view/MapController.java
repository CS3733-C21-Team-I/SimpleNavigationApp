package edu.wpi.cs3733.c21.teamI.view;

import static com.jfoenix.controls.pannable.gestures.PanningGestures.clamp;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.hospitalMap.*;
import edu.wpi.cs3733.c21.teamI.pathfinding.*;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MapController extends Application {
  boolean adminMap = false;

  @FXML Button save, discard, undoButton, redoButton, adminMapToggle;
  @FXML TextField start, destination;
  @FXML ListView startList, destList, directionsField;
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

  private MapDataEntity data = new MapDataEntity();

  private final double scale = 3.05;
  private double fullImgWidth = 0;
  private double fullImgHeight = 0;
  private double imgWidth = 0;
  private double imgHeight = 0;
  private double xOffset = 0;
  private double yOffset = 0;
  private boolean panAllowed = true;

  private Color blue = Color.color(68.0 / 256.0, 136.0 / 256.0, 166.0 / 256.0);
  private Color red = Color.color(217.0 / 256.0, 89.0 / 256.0, 89.0 / 256.0);
  private Color color2 = Color.DARKBLUE;

  public void updateView() throws IOException {
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
      if (data.foundPathExists()) {
        ObservableList<String> items = FXCollections.observableArrayList(new ArrayList<String>());
        directionsField.setItems(items);
        drawCalculatedPath(data.getFoundPath());
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
      data.lookupNodes(e, startList, start);
    } else {
      data.lookupNodes(e, destList, destination);
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
  public void getDirections(ActionEvent e) throws IOException {
    String begin = start.getText();
    String end = destination.getText();
    if (begin.length() > 0 && end.length() > 0) {
      System.out.println(begin + " " + end);

      HospitalMapNode nodeA = data.getNodeByLongName(begin);
      HospitalMapNode nodeB = data.getNodeByLongName(end);
      data.getFoundPath(nodeA, nodeB);
      updateView();
    }
  }

  public void deletePath() {
    mapPane.getChildren().clear();
  }

  @FXML
  public void drawCalculatedPath(List<HospitalMapNode> foundPath) throws IOException {
    deletePath();
    if (foundPath.size() >= 2) {
      HospitalMapNode nodeA = foundPath.get(0);
      HospitalMapNode nodeB = foundPath.get(foundPath.toArray().length - 1);
      mapPane
          .getChildren()
          .removeIf(n -> (n.getClass() == Line.class) || (n.getClass() == Circle.class));
      drawPath(foundPath);
      if (nodeA.getMapID().equals(ViewManager.getMapID())) drawStartPoint(foundPath);
      if (nodeB.getMapID().equals(ViewManager.getMapID())) drawEndPoint(foundPath);
      displayDirections(data.getFoundPathDescription());
    }
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
            .strokeLineCap(StrokeLineCap.ROUND)
            .strokeDashArray(28.0 / scale)
            .strokeWidth(14 / scale * fullImgHeight / imgHeight)
            .build();

    animateLine(start, end, line);

    mapPane.getChildren().add(line);
  }

  // First half of animation where it fades from light blue to dark blue
  private EventHandler animateLine(HospitalMapNode start, HospitalMapNode end, Line line) {
    int startX = 0;
    int startY = 0;
    int endX = 0;
    int endY = 0;
    if (start.getxCoord() > end.getxCoord()) startX = 1;
    else endX = 1;
    if (start.getyCoord() > end.getyCoord()) startY = 1;
    else endY = 1;

    DoubleProperty signalPosition = new SimpleDoubleProperty(0);
    int finalStartX = startX;
    int finalStartY = startY;
    int finalEndX = endX;
    int finalEndY = endY;
    line.strokeProperty()
        .bind(
            Bindings.createObjectBinding(
                () ->
                    new LinearGradient(
                        finalStartX,
                        finalStartY,
                        finalEndX,
                        finalEndY,
                        true,
                        CycleMethod.NO_CYCLE,
                        new Stop(0, color2),
                        new Stop(signalPosition.get(), color2),
                        new Stop(signalPosition.get(), blue),
                        new Stop(1, blue)),
                signalPosition));

    Timeline animation =
        new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(signalPosition, 0)),
            new KeyFrame(Duration.seconds(5), new KeyValue(signalPosition, 1)));

    animation.setOnFinished(finish -> animateLinePhase2(start, end, line));
    animation.setRate(2.0);
    // animation.setCycleCount(100);
    animation.play();

    return null;
  }

  // second phase of animation where it goes back from dark blue to light blue for a smooth looping
  // transition
  private EventHandler animateLinePhase2(HospitalMapNode start, HospitalMapNode end, Line line) {
    int startX = 0;
    int startY = 0;
    int endX = 0;
    int endY = 0;
    if (start.getxCoord() > end.getxCoord()) startX = 1;
    else endX = 1;
    if (start.getyCoord() > end.getyCoord()) startY = 1;
    else endY = 1;

    DoubleProperty signalPosition = new SimpleDoubleProperty(0);
    int finalStartX = startX;
    int finalStartY = startY;
    int finalEndX = endX;
    int finalEndY = endY;
    line.strokeProperty()
        .bind(
            Bindings.createObjectBinding(
                () ->
                    new LinearGradient(
                        finalStartX,
                        finalStartY,
                        finalEndX,
                        finalEndY,
                        true,
                        CycleMethod.NO_CYCLE,
                        new Stop(0, blue),
                        new Stop(signalPosition.get(), blue),
                        new Stop(signalPosition.get(), color2),
                        new Stop(1, color2)),
                signalPosition));

    Timeline animation =
        new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(signalPosition, 0)),
            new KeyFrame(Duration.seconds(5), new KeyValue(signalPosition, 1)));

    animation.setOnFinished(finish -> animateLine(start, end, line));
    animation.setRate(2.0);
    //    animation.setCycleCount(100);
    animation.play();
    return null;
  }

  private void drawPath(List<HospitalMapNode> path) throws IOException {
    HospitalMapNode currNode;
    HospitalMapNode nextNode;
    for (int i = 0; i < path.size() - 1; i++) {
      currNode = path.get(i);
      nextNode = path.get(i + 1);
      if (nextNode.getMapID().equals(ViewManager.getMapID())
          && currNode.getMapID().equals(ViewManager.getMapID())) {
        drawArrow(currNode, nextNode);
        drawEdge(currNode, nextNode, Color.BLUE);
      }
    }
  }

  private void drawStartPoint(List<HospitalMapNode> path) throws IOException {
    double imgScale = 256 / scale;
    String startIcon =
        System.getProperty("user.dir") + "\\src/main/resources/fxml/fxmlResources/startIcon.png";
    double startIconX = transformX(path.get(0).getxCoord()) - imgScale / 2;
    double startIconY = transformY(path.get(0).getyCoord()) - imgScale;
    drawNode(path.get(0), blue);
    // displayImage(startIcon, startIconX, startIconY, imgScale);
  }

  private void drawEndPoint(List<HospitalMapNode> path) throws IOException {
    double imgScale = 256 / scale;
    String finishIcon =
        System.getProperty("user.dir") + "\\src/main/resources/fxml/fxmlResources/finishIcon.png";
    double finishIconX = transformX(path.get(path.size() - 1).getxCoord()) - imgScale / 2;
    double finishIconY = transformY(path.get(path.size() - 1).getyCoord()) - imgScale;
    drawNode(path.get(path.size() - 1), red);
    // displayImage(finishIcon, finishIconX, finishIconY, imgScale);
  }

  private void drawArrow(HospitalMapNode start, HospitalMapNode end) {
    double arrowWidth = 25 / scale;
    double arrowLength = 25 / scale;

    double x1 = transformX(start.getxCoord());
    double x2 = transformX(end.getxCoord());
    double y1 = transformY(start.getyCoord());
    double y2 = transformY(end.getyCoord());

    double dx = x2 - x1, dy = y2 - y1;
    double D = Math.sqrt(dx * dx + dy * dy);
    double xm = D - arrowWidth, xn = xm, ym = arrowLength, yn = -arrowLength, x;
    double sin = dy / D, cos = dx / D;

    x = xm * cos - ym * sin + x1;
    ym = xm * sin + ym * cos + y1;
    xm = x;

    x = xn * cos - yn * sin + x1;
    yn = xn * sin + yn * cos + y1;
    xn = x;

    double[] xpoints = {x2, xm, xn};
    double[] ypoints = {y2, ym, yn};

    Polygon arrow = new Polygon();
    arrow
        .getPoints()
        .addAll(
            xpoints[0], ypoints[0],
            xpoints[1], ypoints[1],
            xpoints[2], ypoints[2]);

    arrow.setFill(blue);
    mapPane.getChildren().add(arrow);
  }

  private void displayImage(String path, double x, double y, double size) throws IOException {
    InputStream stream = new FileInputStream(path);
    Image image = new Image(stream);
    // Creating the image view
    ImageView imageView = new ImageView();
    // Setting image to the image view
    imageView.setImage(image);
    // Setting the image view parameters
    imageView.setX(x);
    imageView.setY(y);
    imageView.setFitWidth(size);
    imageView.setPreserveRatio(true);
    mapPane.getChildren().add(imageView);
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

  @FXML
  private void switchAlgorithm() {
    switch (algorithmPick.getValue().toString()) {
      case "Depth First":
        System.out.println("Making new Depth first...");
        data.pathFinderAlgorithm = new DepthFirstSearch();
        break;
      case "Breadth First":
        System.out.println("Making new Breadth first...");
        data.pathFinderAlgorithm = new BreadthFirstSearch();
        break;
      default:
        data.pathFinderAlgorithm = new PathFinder();
        break;
    }
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
              LocationCategory.HALL,
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
  public void initialize() throws IOException {
    floor1Tab(new ActionEvent());
    campusTab(new ActionEvent());
    boolean isAdmin =
        ApplicationDataController.getInstance()
            .getLoggedInUser()
            .hasPermission(User.Permission.EDIT_MAP);
    adminMapToggle.setVisible(isAdmin);
    algorithmPick.setVisible(isAdmin);
    algorithmPick.getItems().addAll("A*", "Depth First", "Breadth First");
    ViewManager.setMapController(this);
    setupMapViewHandlers();
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
    data.clearFoundPath();
    ObservableList<String> items = FXCollections.observableArrayList(new ArrayList<String>());
    directionsField.setItems(items);
    deletePath();
  }

  @FXML
  public void toggleAccessible(ActionEvent e) {
    if (data.scorer.nodeTypesToAvoid.size() > 0) {
      data.scorer.nodeTypesToAvoid.clear();
    } else {
      data.scorer.nodeTypesToAvoid.add(NodeRestrictions.WHEELCHAIR_INACCESSIBLE);
    }
    System.out.print("NodeRestrictions:" + data.scorer.nodeTypesToAvoid);
  }

  private void displayDirections(ArrayList<String> directions) {
    ObservableList<String> items = FXCollections.observableArrayList(directions);
    // System.out.println(items);
    directionsField.setItems(items);
  }

  public void campusTab(Event event) throws IOException {
    if (campus != currentTab && currentTab != null) {
      System.out.println("Tab 1");
      if (adminMap) {
        ViewManager.setActiveMap("Faulkner Lot");
      } else {
        ViewManager.setActiveMapFromCache("Faulkner Lot", data.source);
      }
      updateView();
      currentTab = campus;
      startZoomPan(mapPane);
      resize();
    }
  }

  public void floor1Tab(Event event) throws IOException {
    if (floor1 != currentTab) {
      System.out.println("Tab 2");
      if (adminMap || isFirstLoad) {
        ViewManager.setActiveMap("Faulkner 1");
      } else {
        ViewManager.setActiveMapFromCache("Faulkner 1", data.source);
      }
      updateView();
      currentTab = floor1;
      startZoomPan(mapPane);
      resize();
    }
  }

  public void floor2Tab(Event event) throws IOException {
    if (floor2 != currentTab) {
      System.out.println("Tab 3");
      if (adminMap) {
        ViewManager.setActiveMap("Faulkner 2");
      } else {
        ViewManager.setActiveMapFromCache("Faulkner 2", data.source);
      }
      updateView();
      currentTab = floor2;
      startZoomPan(mapPane);
      resize();
    }
  }

  public void floor3Tab(Event event) throws IOException {
    if (floor3 != currentTab) {

      System.out.println("Tab 4");
      if (adminMap) {
        ViewManager.setActiveMap("Faulkner 3");
      } else {
        ViewManager.setActiveMapFromCache("Faulkner 3", data.source);
      }
      updateView();
      currentTab = floor3;
      startZoomPan(mapPane);
      resize();
    }
  }

  public void floor4Tab(Event event) throws IOException {
    if (floor4 != currentTab) {
      System.out.println("Tab 5");
      if (adminMap) {
        ViewManager.setActiveMap("Faulkner 4");
      } else {
        ViewManager.setActiveMapFromCache("Faulkner 4", data.source);
      }
      updateView();
      currentTab = floor4;
      startZoomPan(mapPane);
      resize();
    }
  }

  public void floor5Tab(Event event) throws IOException {
    if (floor6 != currentTab) {
      System.out.println("Tab 6");
      if (adminMap) {
        ViewManager.setActiveMap("Faulkner 5");
      } else {
        ViewManager.setActiveMapFromCache("Faulkner 5", data.source);
      }
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
              try {
                resize();
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
    mapImage
        .fitHeightProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              try {
                resize();
              } catch (IOException e) {
                e.printStackTrace();
              }
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
              try {
                getDirections(new ActionEvent());
              } catch (IOException ioException) {
                ioException.printStackTrace();
              }
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
              try {
                getDirections(new ActionEvent());
              } catch (IOException ioException) {
                ioException.printStackTrace();
              }
            }
          } else {
            update();
          }
        });
  }

  private void resize() throws IOException {
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
