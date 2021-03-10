package edu.wpi.cs3733.c21.teamI.view.mobile;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.hospitalMap.EuclidianDistCalc;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.MapDataEntity;
import edu.wpi.cs3733.c21.teamI.hospitalMap.NodeRestrictions;
import edu.wpi.cs3733.c21.teamI.pathfinding.*;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicketDataController;
import edu.wpi.cs3733.c21.teamI.user.User;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class MapPathfindingControllerMobile extends MobileMapController {
  @FXML ImageView mapImage;
  @FXML TextField start, destination;
  @FXML ListView startList, destList, directionsField;
  @FXML VBox extraMenu;
  @FXML JFXButton clear;
  @FXML JFXCheckBox accessible;
  @FXML StackPane allDirections;
  @FXML JFXHamburger ham;
  @FXML Label singleInstruction;

  private EuclidianDistCalc scorer = new EuclidianDistCalc();
  private AlgorithmSelectionStrategyPattern pathFinderAlgorithm =
      new AlgorithmSelectionStrategyPattern(new A_Star<>());

  private List<HospitalMapNode> foundPath;
  private ArrayList<String> foundPathDescription;

  // setup stuff
  @FXML
  public void initialize() throws IOException {
    System.out.println("Initializing pathfinding controller");
    extraMenu.managedProperty().bind(extraMenu.visibleProperty());
    allDirections.managedProperty().bind(allDirections.visibleProperty());
    HamburgerSlideCloseTransition hamburgerTransition = new HamburgerSlideCloseTransition(ham);
    hamburgerTransition.setRate(-1);
    ham.addEventHandler(
        MouseEvent.MOUSE_CLICKED,
        (e) -> {
          hamburgerTransition.setRate(hamburgerTransition.getRate() * -1);
          hamburgerTransition.play();
          extraMenu.setVisible(!extraMenu.isVisible());
        });
    setupMapViewHandlers();
    currentMapID = "Faulkner Lot";
    campusTab(new ActionEvent());

    // Gets covid risk data from UserDatabaseManager and submits it to the scorer to consider
    boolean isHighCovidRisk =
        UserDatabaseManager.getInstance()
                .getCovidRiskForUser(
                    ApplicationDataController.getInstance().getLoggedInUser().getUserId())
            == User.CovidRisk.COVID_RISK;

    reflectCovidStatus(isHighCovidRisk);
  }

  public void reflectCovidStatus(boolean isHighCovidRisk) {
    if (isHighCovidRisk) {
      scorer.nodeTypesToAvoid.add(NodeRestrictions.NON_COVID_RISK_VISITORS);
      scorer.nodeTypesToAvoid.remove(NodeRestrictions.COVID_RISK_VISITORS);
    } else {
      scorer.nodeTypesToAvoid.remove(NodeRestrictions.NON_COVID_RISK_VISITORS);
      scorer.nodeTypesToAvoid.add(NodeRestrictions.COVID_RISK_VISITORS);
    }
    System.out.print("NodeRestrictions:" + scorer.nodeTypesToAvoid);
  }

  @FXML
  public void toggleDirections(ActionEvent e) {
    allDirections.setVisible(!allDirections.isVisible());
  }

  @FXML
  public void toggleEditMap(ActionEvent e) throws IOException {
    StackPane replacePane = (StackPane) rootPane.getParent();
    replacePane.getChildren().clear();
    replacePane
        .getChildren()
        .add(FXMLLoader.load(getClass().getResource("/fxml/Pathediting.fxml")));
  }

  // viewport stuff
  public void updateView() {
    try {
      Image background =
          new Image(
              (getClass().getResource("/fxml/mapImages/" + currentMapID.replace(" ", "") + ".png"))
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
    if (foundPathExists()) {
      ObservableList<String> items = FXCollections.observableArrayList(new ArrayList<String>());
      directionsField.setItems(items);
      drawCalculatedPath(getFoundPath());
    }
  }

  // start & end dialogue boxes stuff
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

  // pathfinding functions
  public void clearFoundPath() {
    foundPath = null;
  }

  public boolean foundPathExists() {
    return foundPath != null && !foundPath.isEmpty();
  }

  public List<HospitalMapNode> getFoundPath() {
    if (foundPathExists()) {
      return foundPath;
    } else {
      return new ArrayList<HospitalMapNode>();
    }
  }

  public List<HospitalMapNode> getFoundPath(HospitalMapNode nodeA, HospitalMapNode nodeB) {
    this.foundPath = pathFinderAlgorithm.findPath(nodeA, nodeB, scorer);
    this.foundPathDescription = TextDirections.getDirections(scorer, foundPath);
    return foundPath;
  }

  public ArrayList<String> getFoundPathDescription(List<HospitalMapNode> path) {
    if (!path.equals(foundPath)) {
      foundPathDescription = TextDirections.getDirections(scorer, path);
    }
    return foundPathDescription;
  }

  public ArrayList<String> getFoundPathDescription() {
    return foundPathDescription;
  }

  @FXML
  public void getDirections(ActionEvent e) throws IOException {
    String begin = start.getText();
    String end = destination.getText();
    if (begin.length() > 0 && end.length() > 0) {
      System.out.println(begin + " " + end);

      HospitalMapNode nodeA = MapDataEntity.getNodeByLongName(begin);
      HospitalMapNode nodeB = MapDataEntity.getNodeByLongName(end);
      getFoundPath(nodeA, nodeB);
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
        if (nodeA.getMapID().equals(currentMapID)) drawStartPoint(foundPath);
        if (nodeB.getMapID().equals(currentMapID)) drawEndPoint(foundPath);
      } catch (IOException e) {
        e.printStackTrace();
      }
      displayDirections(getFoundPathDescription());
    }
  }

  // algorithm stuff
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
    clearFoundPath();
    ObservableList<String> items = FXCollections.observableArrayList(new ArrayList<String>());
    directionsField.setItems(items);
    clearMap();
  }

  @FXML
  public void toggleAccessible(ActionEvent e) {
    if (scorer.nodeTypesToAvoid.size() > 0) {
      scorer.nodeTypesToAvoid.clear();
    } else {
      scorer.nodeTypesToAvoid.add(NodeRestrictions.WHEELCHAIR_INACCESSIBLE);
    }
    System.out.print("NodeRestrictions:" + scorer.nodeTypesToAvoid);
  }

  protected void displayDirections(ArrayList<String> directions) {
    ObservableList<String> items = FXCollections.observableArrayList(directions);
    directionsField.setItems(items);
    singleInstruction.setText(directionsField.getItems().get(0).toString());
  }

  protected Circle setMouseActions(Circle circle, HospitalMapNode node) {
    return circle;
  }

  @FXML
  public void exit(MouseEvent e) {
    Stage stage = (Stage) ((Circle) e.getSource()).getScene().getWindow();
    stage.close();
  }

  @FXML
  public void exitButton(ActionEvent e) {
    Stage stage = (Stage) ((JFXButton) e.getSource()).getScene().getWindow();
    stage.close();
  }

  public void fillCar(ActionEvent actionEvent) {
    destination.setText(
        UserDatabaseManager.getInstance()
            .getLocationForUser(
                ApplicationDataController.getInstance().getLoggedInUser().getUserId()));
  }
}
