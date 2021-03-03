package edu.wpi.cs3733.c21.teamI.view.maps;

import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.NodeRestrictions;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicketDataController;
import edu.wpi.cs3733.c21.teamI.user.User;
import edu.wpi.cs3733.c21.teamI.view.ViewManager;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class MapPathfindingController extends MapController {
  @FXML TextField start, destination;
  @FXML ListView startList, destList, directionsField;

  @FXML
  public void initialize() throws IOException {
    System.out.println("Initializing pathfinding controller...");
    floor1Tab(new ActionEvent());
    campusTab(new ActionEvent());
    boolean isAdmin =
        ApplicationDataController.getInstance()
            .getLoggedInUser()
            .hasPermission(User.Permission.EDIT_MAP);
    adminMapToggle.setVisible(isAdmin);
    ViewManager.setMapController(this);
    setupMapViewHandlers();
  }

  @FXML
  public void toggleEditMap(ActionEvent e) throws IOException {
    Group root = new Group();
    Scene scene = ((Button) e.getSource()).getScene();
    root.getChildren()
        .add(FXMLLoader.load(ViewManager.class.getResource("/fxml/Pathediting.fxml")));
  }

  public void updateView() throws IOException {
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
    update();
  }

  protected void update() {
    mapPane.getChildren().clear();
    if (data.foundPathExists()) {
      ObservableList<String> items = FXCollections.observableArrayList(new ArrayList<String>());
      directionsField.setItems(items);
      drawCalculatedPath(data.getFoundPath());
    }
  }

  public void lookup(KeyEvent e) {
    if (e.getSource() == start) {
      ServiceTicketDataController.lookupNodes(e, startList, start);
    } else {
      ServiceTicketDataController.lookupNodes(e, destList, destination);
    }
  }

  protected void setupMapViewHandlers() {
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

  @FXML
  public void drawCalculatedPath(List<HospitalMapNode> foundPath) {
    clearMap();
    if (foundPath.size() >= 2) {
      HospitalMapNode nodeA = foundPath.get(0);
      HospitalMapNode nodeB = foundPath.get(foundPath.toArray().length - 1);
      mapPane
          .getChildren()
          .removeIf(n -> (n.getClass() == Line.class) || (n.getClass() == Circle.class));
      try {
        drawPath(foundPath);
        if (nodeA.getMapID().equals(ViewManager.getMapID())) drawStartPoint(foundPath);
        if (nodeB.getMapID().equals(ViewManager.getMapID())) drawEndPoint(foundPath);
      } catch (IOException e) {
        e.printStackTrace();
      }
      displayDirections(data.getFoundPathDescription());
    }
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
    clearMap();
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

  protected void displayDirections(ArrayList<String> directions) {
    ObservableList<String> items = FXCollections.observableArrayList(directions);
    // System.out.println(items);
    directionsField.setItems(items);
  }

  protected Circle setMouseActions(Circle circle, HospitalMapNode node) {
    return circle;
  }
}
