package edu.wpi.ithorian.hospitalMap.mapEditing;

import edu.wpi.ithorian.hospitalMap.HospitalMapNode;
import java.io.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.stage.Stage;

public class MapEditView extends Application {

  private int scale = 2;
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
    System.out.println("Editor time");
    //        new Scene(mapManager.getRoot() /*, image.getWidth() / scale, image.getHeight() /
    // scale*/);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    primaryStage.setTitle("Map Editor");
    primaryStage.show();
    mapManager.setStage(primaryStage);
    update();
  }

  public void update() {
    Group root = mapManager.getRoot();
    for (HospitalMapNode node : mapManager.getEntityNodes()) {
      drawEdges(node);
    }
    for (HospitalMapNode node : mapManager.getEntityNodes()) {
      makeNodeCircle(node);
    }
    mapManager.setRoot(root);
  }

  private void onRightClick(MouseEvent e, HospitalMapNode node) {
    System.out.println("Node " + node.getID() + " clicked");
    if (e.getButton() == MouseButton.SECONDARY) {
      mapManager.deleteNode(node.getID());
      for (HospitalMapNode newNode : this.mapManager.getEntityNodes()) {
        drawEdges(newNode);
      }
      update();
    }
  }

  private void drawEdges(HospitalMapNode parent) {
    Group root = mapManager.getRoot();
    for (HospitalMapNode child : parent.getConnections()) {
      if (mapManager.getEntityNodes().contains(child)) {
        Line line =
            LineBuilder.create()
                .startX(parent.getxCoord() / scale)
                .startY(parent.getyCoord() / scale)
                .endX(child.getxCoord() / scale)
                .endY(child.getyCoord() / scale)
                .stroke(Color.RED)
                .strokeWidth(14 / scale)
                .build();
        root.getChildren().add(line);
        mapManager.setRoot(root);
      }
    }
  }

  private void makeNodeCircle(HospitalMapNode node) {
    Circle circle = new Circle();
    circle.setFill(Color.RED);
    circle.setCenterX(node.getxCoord() / scale);
    circle.setCenterY(node.getyCoord() / scale);
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
    mapManager.setRoot(root);
  }
}
