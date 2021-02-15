package edu.wpi.ithorian.hospitalMap.mapEditing;

import edu.wpi.ithorian.hospitalMap.HospitalMapNode;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.stage.Stage;

public class MapEditView extends Application {

  private double scale = 1.5;
  private MapEditManager mapManager;
  private Scene scene;
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
  public void init() throws Exception {
    // creating the image object
    //    InputStream stream = null;
    //
    //    try {
    //      stream = new FileInputStream(mapManager.getImagePath());
    //    } catch (FileNotFoundException e) {
    //      e.printStackTrace();
    //    }
    //    Image image = new Image(stream);
    //    // Creating the image view
    //    ImageView imageView = new ImageView();
    //    // Setting image to the image view
    //    imageView.setImage(image);
    //    // Setting the image view parameters
    //    imageView.setX(0);
    //    imageView.setY(0);
    //    imageView.setFitWidth(image.getWidth() / scale);
    //    imageView.setPreserveRatio(true);

    // Setting the Scene object
    // this.imageView = imageView;
    //        new Scene(mapManager.getRoot() /*, image.getWidth() / scale, image.getHeight()
    // /scale*/);
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Map Editor");
    //    primaryStage.setScene(mapManager.getMapChildren().getScene());
    primaryStage.show();
    mapManager.setStage(primaryStage);
    for (HospitalMapNode node : mapManager.getEntityNodes()) {
      drawEdges(node);
    }
    for (HospitalMapNode node : mapManager.getEntityNodes()) {
      makeNodeCircle(node);
    }
    // update();
  }

  public void update() {
    Group root = new Group();
    root.getChildren().addAll((Node[]) mapManager.getMapChildren());
    mapManager.getStage().getScene().setRoot(root);
    //    mapManager.getStage().setScene(root);
    //    System.out.println(mapManager.getEntityNodes());

    //    for (HospitalMapNode node : mapManager.getEntityNodes()) {
    //      drawEdges(node);
    //    }
    //    for (HospitalMapNode node : mapManager.getEntityNodes()) {
    //      makeNodeCircle(node);
    //    }
  }

  private void onRightClick(MouseEvent e, HospitalMapNode node) {
    System.out.println("Node " + node.getID() + " clicked");
    System.out.println(
        "Before: "
            + this.mapManager.getEntityNodes().stream()
                .map(HospitalMapNode::getID)
                .collect(Collectors.joining(", ")));
    if (e.getButton() == MouseButton.SECONDARY) {
      mapManager.deleteNode(node.getID());
      System.out.println(
          "After: "
              + this.mapManager.getEntityNodes().stream()
                  .map(HospitalMapNode::getID)
                  .collect(Collectors.joining(", ")));
      //      Group root = mapManager.getRoot();
      //      System.out.println(root.getChildren());
      update();
    }
  }

  private void drawEdges(HospitalMapNode parent) {
    Group root = mapManager.getRoot();
    for (HospitalMapNode child : parent.getConnections()) {
      if (mapManager.getEntityNodes().contains(child)) {
        Line line =
            LineBuilder.create()
                .startX((parent.getxCoord() - 5) / scale)
                .startY((parent.getyCoord() - 5) / scale)
                .endX((child.getxCoord() - 5) / scale)
                .endY((child.getyCoord() - 5) / scale)
                .stroke(Color.RED)
                .strokeWidth(14 / scale)
                .build();
        root.getChildren().add(line);
      }
    }
  }

  private void makeNodeCircle(HospitalMapNode node) {
    Circle circle = new Circle();
    circle.setFill(Color.RED);
    circle.setCenterX(node.getxCoord() / scale - 5);
    circle.setCenterY(node.getyCoord() / scale - 5);
    circle.setRadius(14 / scale);
    circle.setOnMousePressed(
        e -> {
          onRightClick(e, node);
        });
    circle.setOnMouseEntered(
        t -> {
          Circle newCircle =
              (Circle)
                  mapManager
                      .getRoot()
                      .getChildren()
                      .get(mapManager.getRoot().getChildren().indexOf(circle));
          newCircle.setFill(Color.PINK);
        });

    circle.setOnMouseExited(
        t -> {
          Circle newCircle =
              (Circle)
                  mapManager
                      .getRoot()
                      .getChildren()
                      .get(mapManager.getRoot().getChildren().indexOf(circle));
          newCircle.setFill(Color.RED);
        });
    Group root = mapManager.getRoot();
    root.getChildren().add(circle);
  }
}
