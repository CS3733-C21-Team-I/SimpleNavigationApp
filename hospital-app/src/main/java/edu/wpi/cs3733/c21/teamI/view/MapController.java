package edu.wpi.cs3733.c21.teamI.view;

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
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
  @FXML AnchorPane tabPane;
  @FXML ImageView mapImage;

  private MapDataEntity data = new MapDataEntity();

  private final double scale = 3.05;
  private int imgWidth = 2989;
  private int imgHeight = 2457;

  private Color blue = Color.color(68.0 / 256.0, 136.0 / 256.0, 166.0 / 256.0);
  private Color red = Color.color(217.0 / 256.0, 89.0 / 256.0, 89.0 / 256.0);
  private Color color2 = Color.DARKBLUE;

  public void updateView() throws IOException {
    if (adminMap) {
      startEditView();
    } else {
      try {
        mapImage.setImage(
            new Image(
                (getClass()
                        .getResource(
                            "/fxml/mapImages/" + ViewManager.getMapID().replace(" ", "") + ".png"))
                    .toURI()
                    .toString()));
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
      mapPane.getChildren().clear();
      if (data.foundPathExists()) {
        drawCalculatedPath(data.getFoundPath());
      }
    }
  }

  @FXML
  public void toggleEditMap(ActionEvent e) {
    adminMap = !adminMap;
    algorithmPick.setVisible(adminMap);
    mapPane.setVisible(adminMap);
    save.setVisible(adminMap);
    discard.setVisible(adminMap);
    if (adminMap) {
      startEditView();
      undoButton.setVisible(true);
      redoButton.setVisible(true);
    } else {
      nodeMenu.setVisible(false);
      //      ViewManager.getDataCont().discardChanges();
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
    ViewManager.setSelectedNode(null);
    ViewManager.navigate(e);
  }

  @FXML
  public void getDirections(ActionEvent e) throws IOException {
    String begin = start.getText();
    String end = destination.getText();
    if (begin.length() > 0 && end.length() > 0) {
      System.out.println(begin + " " + end);

      HospitalMapNode nodeA = getNodeByLongName(begin);
      HospitalMapNode nodeB = getNodeByLongName(end);
      drawCalculatedPath(data.getFoundPath(nodeA, nodeB));
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
    }
  }

  private void drawNode(HospitalMapNode node, Color color) {
    Circle circle =
        new Circle(
            (node.getxCoord() * imgWidth / 100000.0 / scale),
            (node.getyCoord() * imgHeight / 100000.0 / scale),
            13 / scale);
    circle.setFill(color);
    mapPane.getChildren().add(circle);
  }

  private void drawEdge(HospitalMapNode start, HospitalMapNode end, Color color) {
    Line line =
        LineBuilder.create()
            .startX((start.getxCoord() * imgWidth / 100000.0 / scale))
            .startY((start.getyCoord() * imgHeight / 100000.0 / scale))
            .endX((end.getxCoord() * imgWidth / 100000.0 / scale))
            .endY((end.getyCoord() * imgHeight / 100000.0 / scale))
            .stroke(color)
            .strokeLineCap(StrokeLineCap.ROUND)
            .strokeDashArray(28.0 / scale)
            .strokeWidth(14.0 / scale)
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
    HospitalMapNode nextNode = null;

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
    double xAdjustment = imgWidth / 100000.0 / scale;
    double yAdjustment = imgHeight / 100000.0 / scale;

    double imgScale = 256 / scale;
    String startIcon =
        System.getProperty("user.dir") + "\\src/main/resources/fxml/fxmlResources/startIcon.png";
    double startIconX = path.get(0).getxCoord() * xAdjustment - imgScale / 2;
    double startIconY = path.get(0).getyCoord() * yAdjustment - imgScale;
    drawNode(path.get(0), blue);
    displayImage(startIcon, startIconX, startIconY, imgScale);
  }

  private void drawEndPoint(List<HospitalMapNode> path) throws IOException {
    double xAdjustment = imgWidth / 100000.0 / scale;
    double yAdjustment = imgHeight / 100000.0 / scale;

    double imgScale = 256 / scale;
    String finishIcon =
        System.getProperty("user.dir") + "\\src/main/resources/fxml/fxmlResources/finishIcon.png";
    double finishIconX = path.get(path.size() - 1).getxCoord() * xAdjustment - imgScale / 2;
    double finishIconY = path.get(path.size() - 1).getyCoord() * yAdjustment - imgScale;
    drawNode(path.get(path.size() - 1), red);
    displayImage(finishIcon, finishIconX, finishIconY, imgScale);
  }

  private void drawArrow(HospitalMapNode start, HospitalMapNode end) {
    double xAdjustment = imgWidth / 100000.0 / scale;
    double yAdjustment = imgHeight / 100000.0 / scale;
    double arrowWidth = 25 / scale;
    double arrowLength = 25 / scale;

    double x1 = start.getxCoord() * xAdjustment;
    double x2 = end.getxCoord() * xAdjustment;
    double y1 = start.getyCoord() * yAdjustment;
    double y2 = end.getyCoord() * yAdjustment;

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
      setAddNodeHander();
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
      mapImage.setImage(
          new Image(
              (getClass()
                      .getResource(
                          "/fxml/mapImages/" + ViewManager.getMapID().replace(" ", "") + ".png"))
                  .toURI()
                  .toString()));
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

  private void setAddNodeHander() {
    EventHandler<? super MouseEvent> eventHandler =
        (EventHandler<MouseEvent>)
            e -> {
              if (e.getButton() == MouseButton.SECONDARY) {
                // definitely need a better way of making an ID
                System.out.println("u just right clicked");
                ViewManager.getDataCont()
                    .addNode(
                        new HospitalMapNode(
                            randomGenerate(),
                            ViewManager.getMapID(),
                            (int) (e.getX() * scale / imgWidth * 100000),
                            (int) (e.getY() * scale / imgHeight * 100000),
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
    ViewManager.setMapController(this);
    tabPane.getChildren().clear();
    tabPane.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/TabPane.fxml")));
    setupMapViewHandlers();
    boolean isAdmin =
        ApplicationDataController.getInstance()
            .getLoggedInUser()
            .hasPermission(User.Permission.EDIT_MAP);
    adminMapToggle.setVisible(isAdmin);
    algorithmPick.setVisible(isAdmin);
    algorithmPick.getItems().addAll("A*", "Depth First", "Breadth First");
  }

  private void drawSelectedNode() {
    if (ViewManager.selectedInActiveMap()) {
      Circle circle = new Circle();
      circle.setFill(Color.PURPLE);
      circle.setCenterX((ViewManager.getSelectedNode().getxCoord() * imgWidth / 100000 / scale));
      circle.setCenterY((ViewManager.getSelectedNode().getyCoord() * imgHeight / 100000 / scale));
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
                .startX((parent.getxCoord() * imgWidth / 100000 / scale))
                .startY((parent.getyCoord() * imgHeight / 100000 / scale))
                .endX((child.getxCoord() * imgWidth / 100000 / scale))
                .endY((child.getyCoord() * imgHeight / 100000 / scale))
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
                  ((parent.getxCoord() + child.getxCoord()) / 2) * imgWidth / 100000 / scale
                      - xMarker.getFitWidth() / 2);
              xMarker.setY(
                  ((parent.getyCoord() + child.getyCoord()) / 2) * imgHeight / 100000 / scale
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
    for (HospitalMapNode child : node.getConnections()) {
      if (!node.getMapID().equals(child.getMapID())) {
        Circle highlight =
            new Circle(
                (node.getxCoord() * imgWidth / 100000 / scale),
                (node.getyCoord() * imgHeight / 100000 / scale),
                20 / scale);
        highlight.setFill(Color.GREEN);
        mapPane.getChildren().add(highlight);
      }
    }
    Circle circle = new Circle();
    circle.setFill(Color.RED);
    circle.setCenterX((node.getxCoord() * imgWidth / 100000 / scale));
    circle.setCenterY((node.getyCoord() * imgHeight / 100000 / scale));
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
          System.out.println(node.getxCoord() + ", " + node.getyCoord());
          if (t.getButton() == MouseButton.PRIMARY) {
            if (!isDrag) {
              nodeMenu.setVisible(ViewManager.toggleNode(node));
            } else {
              isDrag = false;
              ViewManager.getDataCont().editNode(movingNode.getID(), movingNode);
            }
            nodeDeleteButton.setOnAction(
                e -> {
                  nodeMenu.setVisible(ViewManager.toggleNode(node));
                  ViewManager.getDataCont().deleteNode(node.getID());
                  if (ViewManager.getSelectedNode() == node) {
                    ViewManager.setSelectedNode(null);
                  }
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
          newCircle.setCenterY(t.getSceneY() - 50);

          HospitalMapNode newNode =
              new HospitalMapNode(
                  node.getID(),
                  node.getMapID(),
                  (int) (t.getX() * scale / imgWidth * 100000),
                  (int) (t.getY() * scale / imgHeight * 100000),
                  node.getConnections());
          movingNode = newNode;
          isDrag = true;
        });
    mapPane.getChildren().add(circle);
  }

  @FXML
  public void onSwitch() {
    //        String begin = start.getValue();
    String begin = start.getText();
    //        String end = destination.getValue();
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
    directionsField.setItems(items);
  }
}
