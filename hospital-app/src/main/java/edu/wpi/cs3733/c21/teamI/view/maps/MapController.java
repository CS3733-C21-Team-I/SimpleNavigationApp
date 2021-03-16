package edu.wpi.cs3733.c21.teamI.view.maps;

import static edu.wpi.cs3733.c21.teamI.hospitalMap.LocationCategory.*;

import com.jfoenix.controls.JFXTabPane;
import edu.wpi.cs3733.c21.teamI.hospitalMap.*;
import edu.wpi.cs3733.c21.teamI.pathfinding.DirectionStep;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
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

public abstract class MapController extends Application {
  boolean adminMap = false;

  @FXML StackPane rootPane;
  @FXML AnchorPane nodeMenu;
  @FXML TextField sNameField, lNameField;
  protected boolean isDrag = false;
  protected boolean isFirstLoad = true;
  @FXML AnchorPane mapPane;
  @FXML ImageView mapImage;

  @FXML StackPane imageContainer;
  @FXML VBox stackContainer;
  @FXML JFXTabPane tabPane;
  @FXML Tab campus;
  @FXML Tab floor1;
  @FXML Tab floor2;
  @FXML Tab floor3;
  @FXML Tab floor4;
  @FXML Tab floor6;
  protected Tab currentTab = null;

  final double scale = 3.05;
  double fullImgWidth = 0;
  double fullImgHeight = 0;
  double imgWidth = 0;
  double imgHeight = 0;
  double xOffset = 0;
  double yOffset = 0;
  boolean panAllowed = true;

  protected ArrayList<HospitalMapNode> selectedNode = new ArrayList<>();
  protected String currentMapID = "Faulkner 0";

  protected Color blue = Color.color(68.0 / 256.0, 136.0 / 256.0, 166.0 / 256.0);
  protected Color red = Color.color(217.0 / 256.0, 89.0 / 256.0, 89.0 / 256.0);
  protected Color color2 = Color.DARKBLUE;
  public Circle centerPoint = makeCircle(500, 500, 13, Color.RED);
  public Rectangle viewRect = makeRectangle(10, 10, 50, 50, Color.RED);

  /*public void initializeTabs() {
    campus.setOnSelectionChanged(
        t -> {
          if (campus != currentTab) {
            System.out.println("Tab 1");
            currentMapID = "Faulkner Lot";
            updateView();
            currentTab = campus;
            startZoomPan(mapPane);
            resize();
          }
          ;
        });
  }*/

  public abstract void updateView();

  protected abstract void update();

  @FXML
  public abstract void toggleEditMap(ActionEvent e) throws IOException;

  protected double transformX(double x) {
    return x * (fullImgWidth / imgWidth) * mapPane.getPrefWidth() / 100000
        - xOffset * mapPane.getPrefWidth() / imgWidth;
  }

  protected double transformY(double y) {
    return y * (fullImgHeight / imgHeight) * mapPane.getPrefHeight() / 100000
        - yOffset * mapPane.getPrefHeight() / imgHeight;
  }

  public void clearMap() {
    mapPane.getChildren().clear();
  }

  // replacing viewmanager split

  protected void drawNode(HospitalMapNode node, Color color) {
    Circle circle =
        makeCircle(
            transformX(node.getxCoord()),
            transformY(node.getyCoord()),
            13 / scale * fullImgHeight / imgHeight,
            color);
    mapPane.getChildren().add(circle);
  }

  protected void drawEdge(HospitalMapNode start, HospitalMapNode end, Color color) {
    Line line =
        LineBuilder.create()
            .startX(transformX(start.getxCoord()))
            .startY(transformY(start.getyCoord()))
            .endX(transformX(end.getxCoord()))
            .endY(transformY(end.getyCoord()))
            .stroke(color)
            .strokeLineCap(StrokeLineCap.ROUND)
            .strokeDashArray(28.0 / scale)
            .strokeWidth(14 / scale * fullImgHeight / imgHeight)
            .build();

    animateLine(start, end, line);
    mapPane.getChildren().add(line);
  }

  // First half of animation where it fades from light blue to dark blue
  protected EventHandler animateLine(HospitalMapNode start, HospitalMapNode end, Line line) {
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
  protected EventHandler animateLinePhase2(HospitalMapNode start, HospitalMapNode end, Line line) {
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

  protected void drawPath(List<HospitalMapNode> path) {
    HospitalMapNode currNode;
    HospitalMapNode nextNode;
    for (int i = 0; i < path.size() - 1; i++) {
      currNode = path.get(i);
      nextNode = path.get(i + 1);
      if (nextNode.getMapID().equals(currentMapID) && currNode.getMapID().equals(currentMapID)) {
        drawArrow(currNode, nextNode);
        drawEdge(currNode, nextNode, Color.BLUE);
      }
    }
  }

  protected void drawStartPoint(List<HospitalMapNode> path) throws IOException {
    double imgScale = 256 / scale;
    Image startIcon = null;
    try {
      startIcon =
          new Image(
              (getClass().getResource("/fxml/map/mapImages/symbolIcons/startIcon.png"))
                  .toURI()
                  .toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    double startIconX = transformX(path.get(0).getxCoord()) - imgScale / 2;
    double startIconY = transformY(path.get(0).getyCoord()) - imgScale;
    // drawNode(path.get(0), blue);
    displayImage(startIcon, startIconX, startIconY, imgScale);
  }

  protected void drawEndPoint(List<HospitalMapNode> path) throws IOException {
    double imgScale = 256 / scale;
    Image finishIcon = null;
    try {
      finishIcon =
          new Image(
              (getClass().getResource("/fxml/map/mapImages/symbolIcons/finishIcon.png"))
                  .toURI()
                  .toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    double finishIconX = transformX(path.get(path.size() - 1).getxCoord()) - imgScale / 2;
    double finishIconY = transformY((path.get(path.size() - 1).getyCoord())) - imgScale;

    // drawNode(path.get(path.size() - 1), red);
    displayImage(finishIcon, finishIconX, finishIconY, imgScale);
  }

  protected void drawArrow(HospitalMapNode start, HospitalMapNode end) {
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

  protected void displayImage(Image image, double x, double y, double size) {
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

  @Override
  public void start(Stage primaryStage) {}

  @FXML
  public abstract void initialize();

  protected void drawSelectedNode() {
    if (selectedInActiveMap()) {
      for (HospitalMapNode node : selectedNode) {
        Circle circle =
            makeCircle(
                transformX(node.getxCoord()),
                transformY(node.getyCoord()),
                20 / scale,
                Color.CORNFLOWERBLUE);
        mapPane.getChildren().add(circle);
      }
    }
  }

  protected boolean selectedInActiveMap() {
    return (selectedNode.size() != 0) && selectedNode.get(0).getMapID().equals(currentMapID);
  }

  public void toggleNode(HospitalMapNode node) {
    if (selectedNode.size() == 0) {
      selectedNode.add(node);

    } else if (selectedNode.get(0).equals(node)) {
      selectedNode.clear();
    }
  }

  protected void makeNodeCircle(HospitalMapNode node) {
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
    Circle circle;
    if (node instanceof LocationNode) {
      circle =
          makeCircle(
              transformX(node.getxCoord()),
              transformY(node.getyCoord()),
              12 / scale,
              Color.DARKRED);
    } else {
      circle =
          makeCircle(
              transformX(node.getxCoord()), transformY(node.getyCoord()), 12 / scale, Color.RED);
    }
    circle = (Circle) setMouseActions(circle, node);
    mapPane.getChildren().add(circle);
  }

  protected abstract Node setMouseActions(Node circle, HospitalMapNode node);

  protected Circle makeCircle(double x, double y, double r, Color color) {
    Circle returnCircle = new Circle(x, y, r);
    returnCircle.setFill(color);
    return returnCircle;
  }

  protected Rectangle makeRectangle(
      double minX, double minY, double width, double height, Color color) {
    viewRect = new Rectangle(minX, minY, width, height);
    viewRect.setFill(color);
    viewRect.setOpacity(0.2);
    return viewRect;
  }

  // Scaling code is from https://gist.github.com/james-d/ce5ec1fd44ce6c64e81a
  protected static final int MIN_PIXELS = 200;

  protected void startZoomPan(AnchorPane zoomPane) {
    mapImage.fitWidthProperty().bind(imageContainer.widthProperty());
    mapImage.fitHeightProperty().bind(imageContainer.heightProperty());
    mapImage.setPreserveRatio(true);
    double width = imgWidth;
    double height = imgHeight;
    reset(mapImage, width, height);
    ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();
    imgWidth = mapImage.getViewport().getWidth();
    imgHeight = mapImage.getViewport().getHeight();
    xOffset = mapImage.getViewport().getMinX();
    yOffset = mapImage.getViewport().getMinY();

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
            update();
          }
        });

    zoomPane.setOnScroll(
        e -> {
          updateScale(e, (int) width, (int) height);
        });
  }

  private void updateScale(ScrollEvent e, double width, double height) {
    double delta = -e.getDeltaY();
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
        clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale, 0, width - newWidth);
    double newMinY =
        clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale, 0, height - newHeight);
    mapImage.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
    System.out.println(mapImage.getViewport());
    imgWidth = mapImage.getViewport().getWidth();
    imgHeight = mapImage.getViewport().getHeight();
    xOffset = mapImage.getViewport().getMinX();
    yOffset = mapImage.getViewport().getMinY();
    update();

    System.out.println("mapImage  " + mapImage + "  Viewport  " + mapImage.getViewport());
  }

  protected void resize() {
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
      Rectangle clip = new Rectangle(mapPane.getPrefWidth(), mapPane.getPrefHeight());
      clip.setLayoutX(0);
      clip.setLayoutY(0);
      mapPane.setClip(clip);
      update();
    }
  }

  public void goToTab(String floorID) {
    if (floorID.equals("Faulkner Lot")) {
      goToCampus();
    } else if (floorID.equals("Faulkner 1")) {
      goToFloor1();
    } else if (floorID.equals("Faulkner 2")) {
      goToFloor2();
    } else if (floorID.equals("Faulkner 3")) {
      goToFloor3();
    } else if (floorID.equals("Faulkner 4")) {
      goToFloor4();
    } else if (floorID.equals("Faulkner 5")) {
      goToFloor5();
    }
  }

  public void campusTab(Event event) {
    goToCampus();
  }

  private void goToCampus() {
    if (campus != currentTab && mapPane != null) {
      System.out.println("Tab 1");
      currentMapID = "Faulkner Lot";
      updateView();
      currentTab = campus;
      startZoomPan(mapPane);
      resize();
    }
  }

  public void floor1Tab(Event event) {
    goToFloor1();
  }

  private void goToFloor1() {
    if (floor1 != currentTab) {
      System.out.println("Tab 2");
      currentMapID = "Faulkner 1";
      updateView();
      currentTab = floor1;
      startZoomPan(mapPane);
      resize();
    }
  }

  public void floor2Tab(Event event) {
    goToFloor2();
  }

  private void goToFloor2() {
    if (floor2 != currentTab) {
      System.out.println("Tab 3");
      currentMapID = "Faulkner 2";
      updateView();
      currentTab = floor2;
      startZoomPan(mapPane);
      resize();
    }
  }

  public void floor3Tab(Event event) {
    goToFloor3();
  }

  private void goToFloor3() {
    if (floor3 != currentTab) {
      System.out.println("Tab 4");
      currentMapID = "Faulkner 3";
      updateView();
      currentTab = floor3;
      startZoomPan(mapPane);
      resize();
    }
  }

  public void floor4Tab(Event event) {
    goToFloor4();
  }

  private void goToFloor4() {
    if (floor4 != currentTab) {
      System.out.println("Tab 5");
      currentMapID = "Faulkner 4";
      updateView();
      currentTab = floor4;
      startZoomPan(mapPane);
      resize();
    }
  }

  public void floor5Tab(Event event) {
    goToFloor5();
  }

  private void goToFloor5() {
    if (floor6 != currentTab) {
      System.out.println("Tab 6");
      currentMapID = "Faulkner 5";
      updateView();
      currentTab = floor6;
      startZoomPan(mapPane);
      resize();
    }
  }

  // reset to the top left:
  protected void reset(ImageView imageView, double width, double height) {
    imageView.setViewport(new Rectangle2D(0, 0, width, height));
  }

  // shift the viewport of the imageView by the specified delta, clamping so
  // the viewport does not move off the actual image:
  protected void shift(ImageView imageView, Point2D delta) {
    Rectangle2D viewport = imageView.getViewport();
    double width = imageView.getImage().getWidth();
    double height = imageView.getImage().getHeight();
    double maxX = width - viewport.getWidth();
    double maxY = height - viewport.getHeight();
    double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
    double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);
    imageView.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
    System.out.println(imageView.getViewport());
  }

  protected double clamp(double value, double min, double max) {
    return Math.max(min, Math.min(max, value));
  }

  // convert mouse coordinates in the imageView to coordinates in the actual image:
  protected Point2D imageViewToImage(ImageView imageView, Point2D imageViewCoordinates) {
    double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
    double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();
    Rectangle2D viewport = imageView.getViewport();
    return new Point2D(
        viewport.getMinX() + xProportion * viewport.getWidth(),
        viewport.getMinY() + yProportion * viewport.getHeight());
  }

  public void zoomToPoint(
      double centerX, double centerY, double width, double height, double padding) {
    width += padding * 2;
    height += padding * 2;
    if (height > width) {
      width = height * fullImgWidth / fullImgHeight;
    } else {
      height = width * fullImgHeight / fullImgWidth;
    }

    double newMinX = centerX - width / 2;
    double newMinY = centerY - height / 2;

    Point2D newDimensions = imageViewToImage(mapImage, new Point2D(width, height));
    Point2D newPos = imageViewToImage(mapImage, new Point2D(newMinX, newMinY));

    viewRect = makeRectangle(newMinX, newMinY, width, height, Color.RED);
    mapImage.setViewport(
        new Rectangle2D(newPos.getX(), newPos.getY(), newDimensions.getX(), newDimensions.getY()));
    imgWidth = mapImage.getViewport().getWidth();
    imgHeight = mapImage.getViewport().getHeight();
    xOffset = mapImage.getViewport().getMinX();
    yOffset = mapImage.getViewport().getMinY();
    update();

    // System.out.println("mapImage  " + mapImage + " Viewport  " + mapImage.getViewport());
  }

  public void zoomToFitNodes(HospitalMapNode a, HospitalMapNode b, double padding) {
    double centerX = transformX(DirectionStep.calcCenterPointX(a, b));
    double centerY = transformY(DirectionStep.calcCenterPointY(a, b));

    centerPoint = makeCircle(centerX, centerY, 10 / scale * fullImgHeight / imgHeight, Color.RED);

    double width = transformX(DirectionStep.calcWidth(a, b));
    double height = transformY(DirectionStep.calcHeight(a, b));

    //    double delta = (200 - fullImgWidth) * ((fullImgWidth - width - padding * 2) /
    // fullImgWidth);
    //    double fartherX = fullImgWidth / 2 + (centerX - (fullImgWidth / 2)) * 1.15;
    //    double fartherY = fullImgHeight / 2 + (centerY - (fullImgHeight / 2)) * 1.15;
    //    updateScale(fartherX, fartherY, delta);
    zoomToPoint(centerX, centerY, width, height, padding);
  }

  private void updateScale(double centerX, double centerY, double delta) {
    Rectangle2D viewport = mapImage.getViewport();
    double scale =
        clamp(
            Math.pow(1.001, delta),
            // don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
            Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),
            // don't scale so that we're bigger than image dimensions:
            Math.max(fullImgWidth / viewport.getWidth(), fullImgHeight / viewport.getHeight()));
    Point2D zoomCenter = imageViewToImage(mapImage, new Point2D(centerX, centerY));
    System.out.println("SCALE: " + scale);
    double newWidth = viewport.getWidth() * scale;
    double newHeight = viewport.getHeight() * scale;
    double newMinX =
        clamp(
            zoomCenter.getX() - (zoomCenter.getX() - viewport.getMinX()) * scale,
            0,
            fullImgWidth - newWidth);
    double newMinY =
        clamp(
            zoomCenter.getY() - (zoomCenter.getY() - viewport.getMinY()) * scale,
            0,
            fullImgHeight - newHeight);

    mapImage.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
    imgWidth = mapImage.getViewport().getWidth();
    imgHeight = mapImage.getViewport().getHeight();
    xOffset = mapImage.getViewport().getMinX();
    yOffset = mapImage.getViewport().getMinY();
    update();

    System.out.println("Viewport:  " + mapImage.getViewport());
  }
}
