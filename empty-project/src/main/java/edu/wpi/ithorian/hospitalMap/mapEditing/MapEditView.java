package edu.wpi.ithorian.hospitalMap.mapEditing;

import edu.wpi.ithorian.hospitalMap.HospitalMapNode;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

public class MapEditView {

  private Group root;
  private final int scale;
  private final MapEditManager mapManager;
  private ImageView imageView;

  public MapEditView(MapEditManager mapManager) {
    this.mapManager = mapManager;
    this.scale = mapManager.getScale();
    init();
    update();
  }

  public void init() {
    // creating the image object
    InputStream stream = null;
    try {
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
    this.root = new Group();
    this.imageView = imageView;
    Scene scene = new Scene(root, image.getWidth() / scale, image.getHeight() / scale);
    Stage stage = new Stage();
    stage.setTitle("Floor Map");
    stage.setScene(scene);
    stage.show();
  }

  public void update() {
    root.getChildren().clear();
    root.getChildren().add(imageView);
    for (HospitalMapNode node : mapManager.getEntityNodes()) {
      drawEdges(node);
    }
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

  private Circle makeNodeCircle(HospitalMapNode node) {
    Circle circle = new Circle();
    circle.setFill(Color.RED);
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
    return circle;
  }
}
