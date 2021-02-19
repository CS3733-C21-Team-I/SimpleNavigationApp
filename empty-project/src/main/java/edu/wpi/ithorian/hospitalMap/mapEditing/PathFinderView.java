// package edu.wpi.ithorian.projectCTable;
//
// import edu.wpi.ithorian.hospitalMap.EuclidianDistCalc;
// import edu.wpi.ithorian.hospitalMap.HospitalMap;
// import edu.wpi.ithorian.hospitalMap.HospitalMapNode;
// import edu.wpi.ithorian.pathfinding.PathFinder;
// import java.util.*;
// import java.util.stream.Collectors;
// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.scene.Group;
// import javafx.scene.control.Button;
// import javafx.scene.control.TextField;
// import javafx.scene.input.KeyEvent;
// import javafx.scene.input.MouseEvent;
// import javafx.scene.paint.Color;
// import javafx.scene.shape.Circle;
// import javafx.scene.shape.Line;
// import javafx.scene.shape.LineBuilder;
//
// public class PathFinderView {
//
//  private HospitalMap map;
//  private double scale;
//  private EuclidianDistCalc scorer;
//
//  @FXML Group rootGroup;
//  @FXML Button start1, start2, start3, start4, start5, dest1, dest2, dest3, dest4, dest5;
//  @FXML TextField start, destination;
//
//  public PathFinderView() {}
//
//  @FXML
//  public void initialize() {
//    String path =
//        System.getProperty("user.dir") +
// "/src/main/java/edu/wpi/ithorian/hospitalMap/mapEditing/";
//    Set<HospitalMapNode> nodes =
//        HospitalMap.generateElementFromData(
//            ReadCSV.readFromFile(path + "MapINodes.csv"),
//            ReadCSV.readFromFile(path + "MapIEdges.csv"));
//    HospitalMap map =
//        new HospitalMap(
//            "Test_Map",
//            "Test Map",
//            "Building1",
//            1,
//            "build/resources/main/fxml/fxmlResources/FaulknerCampus.png",
//            nodes);
//    this.map = map;
//    this.scale = 3.05;
//    redrawMap();
//  }
//
//  public void redrawMap() {
//    rootGroup
//        .getChildren()
//        .removeIf(n -> (n.getClass() == Line.class) || (n.getClass() == Circle.class));
//    for (HospitalMapNode node : map.getNodes()) {
//      drawEdges(node);
//      drawNode(node, Color.RED);
//    }
//  }
//
//  @FXML
//  public void drawPathBetweenNodes(String aID, String bID) {
//    redrawMap();
//    this.scorer = new EuclidianDistCalc();
//    List<HospitalMapNode> aStarPath =
//        PathFinder.findPath(map.getNode(aID), map.getNode(bID), scorer);
//    drawPath(aStarPath);
//  }
//
//  private void drawNode(HospitalMapNode node, Color color) {
//    Circle circle =
//        new Circle((node.getxCoord() / scale) - 3, (node.getyCoord() / scale), 13 / scale);
//    circle.setFill(color);
//    rootGroup.getChildren().add(circle);
//  }
//
//  private void drawEdges(HospitalMapNode parent) {
//    for (HospitalMapNode child : parent.getConnections()) {
//      drawEdge(child, parent, Color.RED);
//    }
//  }
//
//  private void drawEdge(HospitalMapNode start, HospitalMapNode end, Color color) {
//    Line line =
//        LineBuilder.create()
//            .startX((start.getxCoord() / scale) - 3)
//            .startY((start.getyCoord() / scale))
//            .endX((end.getxCoord() / scale) - 3)
//            .endY((end.getyCoord() / scale))
//            .stroke(color)
//            .strokeWidth(14 / scale)
//            .build();
//    rootGroup.getChildren().add(line);
//  }
//
//  public void drawPath(List<HospitalMapNode> path) {
//    HospitalMapNode currNode;
//    HospitalMapNode nextNode = null;
//    for (int i = 0; i < path.size() - 1; i++) {
//      currNode = path.get(i);
//      nextNode = path.get(i + 1);
//      drawEdge(currNode, nextNode, Color.BLUE);
//      drawNode(currNode, Color.BLUE);
//    }
//    drawNode(nextNode, Color.BLUE);
//  }
//
//  public void lookup(KeyEvent e) {
//    String matchString =
//        (((TextField) e.getSource()).getText()
//            + (!e.getCharacter().equals(Character.toString((char) 8)) ? e.getCharacter() : "")
//                .toLowerCase());
//    ArrayList<String> nodeIDs =
//        new ArrayList<String>(
//            map.getNodes().stream().map(n -> n.getID()).collect(Collectors.toList()));
//    ArrayList<String> matches = new ArrayList<>();
//    for (String location : nodeIDs) {
//      if (location.toLowerCase().contains(matchString)) {
//        matches.add(location);
//      }
//    }
//
//    clearMenus();
//    for (int i = 0; i < matches.size(); i++) {
//      if (i < 5) {
//        getMenuButton(i, e.getSource() == start).setVisible(true);
//        getMenuButton(i, e.getSource() == start).setText(matches.get(i));
//      } else {
//        break;
//      }
//    }
//  }
//
//  @FXML
//  private Button getMenuButton(int index, boolean isStart) {
//    switch (index) {
//      case 0:
//        return isStart ? start1 : dest1;
//      case 1:
//        return isStart ? start2 : dest2;
//      case 2:
//        return isStart ? start3 : dest3;
//      case 3:
//        return isStart ? start4 : dest4;
//      default:
//        return isStart ? start5 : dest5;
//    }
//  }
//
//  @FXML
//  private void clearMenus() {
//    for (int i = 0; i < 5; i++) {
//      getMenuButton(i, true).setVisible(false);
//      getMenuButton(i, false).setVisible(false);
//    }
//  }
//
//  public void calculatePath(ActionEvent e) {
//    drawPathBetweenNodes(start.getText(), destination.getText());
//  }
//
//  @FXML
//  public void autoFillStart(MouseEvent e) {
//    start.setText(((Button) e.getSource()).getText());
//  }
//
//  @FXML
//  public void autoFillDest(MouseEvent e) {
//    destination.setText(((Button) e.getSource()).getText());
//  }
// }
