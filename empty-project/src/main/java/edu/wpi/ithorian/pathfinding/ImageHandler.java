package edu.wpi.ithorian.pathfinding;

import edu.wpi.ithorian.ReadCSV;
import edu.wpi.ithorian.hospitalMap.EuclidianDistCalc;
import edu.wpi.ithorian.hospitalMap.HospitalMap;
import edu.wpi.ithorian.hospitalMap.HospitalMapNode;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;

public class ImageHandler {

  private Group root;
  private int scale;
  private ImageView baseImage;
  private HashMap<HospitalMapNode, Circle> nodes = new HashMap<>();
  private HospitalMap view;
  private EuclidianDistCalc scorer;

  public ImageHandler(Set<HospitalMapNode> mapNodes, ImageView baseImage, int scale, Group root) {
    this.root = root;
    this.baseImage = baseImage;
    this.scale = scale;
    root.getChildren().add(baseImage);
    for (HospitalMapNode node : mapNodes) {
      drawEdges(node);
      Circle circle = new Circle(node.getxCoord() / scale, node.getyCoord() / scale, 13 / scale);
      circle.setFill(Color.RED);
      circle.setOnMousePressed(
          e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
              root.getChildren().remove(nodes.get(node));
              for (HospitalMapNode neighbor : node.getConnections()) {
                neighbor.removeConnection(node);
              }
              nodes.remove(node);
              root.getChildren().removeIf(element -> element.getClass() == Line.class);
              for (HospitalMapNode newNode : this.nodes.keySet()) {
                drawEdges(newNode);
              }
            }
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
      this.nodes.put(node, circle);
    }
    root.getChildren().addAll(nodes.values());
  }

  @FXML
  public void initialize() {
    String path =
        System.getProperty("user.dir")
            + "\\src\\main\\java\\edu\\wpi\\ithorian\\hospitalMap\\mapEditing\\";
    Set<HospitalMapNode> nodes =
        HospitalMap.generateElementFromData(
            ReadCSV.readFromFile(path + "MapINodes.csv"),
            ReadCSV.readFromFile(path + "MapIEdges.csv"));
    this.view =
        new HospitalMap("Test_Map", "Test Map", "Building1", 1, path + "FaulknerCampus.png", nodes);

    this.scorer = new EuclidianDistCalc();
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

  public void drawPath(List<HospitalMapNode> path) {

    for (int i = 0; i < path.size() - 1; i++) {
      HospitalMapNode currNode = path.get(i);
      HospitalMapNode nextNode = path.get(i + 1);
      Line line =
          LineBuilder.create()
              .startX(currNode.getxCoord() / scale)
              .startY(currNode.getyCoord() / scale)
              .endX(nextNode.getxCoord() / scale)
              .endY(nextNode.getyCoord() / scale)
              .stroke(Color.BLUE)
              .strokeWidth(14 / scale)
              .build();
      root.getChildren().add(line);
    }
  }
}
