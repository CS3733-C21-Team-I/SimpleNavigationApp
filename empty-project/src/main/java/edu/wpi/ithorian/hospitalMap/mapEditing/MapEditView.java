package edu.wpi.ithorian.hospitalMap.mapEditing;

import edu.wpi.ithorian.hospitalMap.HospitalMapNode;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.stage.Stage;

public class MapEditView extends Application {

  private Group root;
  private int scale = 2;
  private MapEditManager mapManager;
  private ImageView imageView;
  private Scene scene;
  private static MapEditManager ourManager;

  public MapEditView() {
    this.mapManager = ourManager;
    System.out.println("Manager? " + mapManager);
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
    InputStream stream = null;

    try {
      System.out.println(new File(".").getAbsolutePath());

      stream = new FileInputStream(mapManager.getImagePath());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    Image image = new Image(stream);
    // Creating the image view
    ImageView imageView = new ImageView();
    // Setting image to the image view
    imageView.setImage(image);
    // Setting the image view parameters
    imageView.setX(0);
    imageView.setY(0);
    imageView.setFitWidth(image.getWidth() / scale);
    imageView.setPreserveRatio(true);
    // Setting the Scene object
    this.root = new Group(imageView);
    this.imageView = imageView;
    this.scene = new Scene(this.root, image.getWidth() / scale, image.getHeight() / scale);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Floor Map");
    primaryStage.setScene(scene);
    primaryStage.show();
    update();
  }

  public void update() {
    this.root.getChildren().clear();
    this.root.getChildren().add(imageView);
    for (HospitalMapNode node : mapManager.getEntityNodes()) {
      drawEdges(node);
      makeNodeCircle(node);
    }
    System.out.println(root.getChildren());
  }

  private void onRightClick(MouseEvent e, HospitalMapNode node) {
    if (e.getButton() == MouseButton.SECONDARY) {
      mapManager.deleteNode(node.getID());
      for (HospitalMapNode newNode : this.mapManager.getEntityNodes()) {
        drawEdges(newNode);
      }
    }
  }

  private void drawEdges(HospitalMapNode parent) {
    for (HospitalMapNode child : parent.getConnections()) {
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
          Circle newCircle = (Circle) root.getChildren().get(root.getChildren().indexOf(circle));
          newCircle.setFill(Color.PINK);
        });

    circle.setOnMouseExited(
        t -> {
          Circle newCircle = (Circle) root.getChildren().get(root.getChildren().indexOf(circle));
          newCircle.setFill(Color.RED);
        });
    root.getChildren().add(circle);
  }
}
