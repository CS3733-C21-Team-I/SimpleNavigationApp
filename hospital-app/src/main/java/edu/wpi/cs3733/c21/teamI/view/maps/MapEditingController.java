package edu.wpi.cs3733.c21.teamI.view.maps;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.LocationCategory;
import edu.wpi.cs3733.c21.teamI.hospitalMap.LocationNode;
import edu.wpi.cs3733.c21.teamI.pathfinding.BreadthFirstSearch;
import edu.wpi.cs3733.c21.teamI.pathfinding.DepthFirstSearch;
import edu.wpi.cs3733.c21.teamI.pathfinding.PathFinder;
import edu.wpi.cs3733.c21.teamI.view.ViewManager;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MapEditingController extends MapController {
  @FXML JFXComboBox algorithmPick;
  @FXML Button save, discard, undoButton, redoButton, adminMapToggle;
  protected HospitalMapNode movingNode;

  @FXML
  public void initialize() throws IOException {
    System.out.println("Initializing editing controller...");
    floor1Tab(new ActionEvent());
    campusTab(new ActionEvent());
    boolean isAdmin = true;
    adminMapToggle.setVisible(true);
    algorithmPick.setVisible(true);
    algorithmPick.getItems().addAll("A*", "Depth First", "Breadth First");
    ViewManager.setMapController(this);
  }

  @FXML
  public void toggleEditMap(ActionEvent e) throws IOException {
    Group root = new Group();
    Scene scene = ((Button) e.getSource()).getScene();
    root.getChildren()
        .add(FXMLLoader.load(ViewManager.class.getResource("/fxml/Pathfinding.fxml")));
  }

  public void updateView() throws IOException {
    if (isFirstLoad) {
      isFirstLoad = false;
      setAddNodeHandler();
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
    nodeMenu.setVisible(
        ViewManager.selectedInActiveMap() && ViewManager.getSelectedNode() != null && adminMap);
    undoButton.setVisible(false);
    redoButton.setVisible(false);
    update();
  }

  // TODO NOT THIS ANYTHING BUT THIS
  protected static AtomicInteger idGen = new AtomicInteger();

  protected String randomGenerate() {
    return "BadSol" + idGen.incrementAndGet();
  }

  protected void update() {
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

  protected void setAddNodeHandler() {
    EventHandler<? super MouseEvent> eventHandler =
        (EventHandler<MouseEvent>)
            e -> {
              if (e.getButton() == MouseButton.SECONDARY) {
                // definitely need a better way of making an ID
                Point2D mousePress = imageViewToImage(mapImage, new Point2D(e.getX(), e.getY()));
                ViewManager.getDataCont()
                    .addNode(
                        new HospitalMapNode(
                            randomGenerate(),
                            ViewManager.getMapID(),
                            (int) (mousePress.getX() / fullImgWidth * 100000),
                            (int) (mousePress.getY() / fullImgHeight * 100000),
                            new ArrayList<>()));
                update();
              }
            };
    mapPane.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
  }

  protected void populateEditNodeMenu(HospitalMapNode node) {
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

  protected void editNodeName(HospitalMapNode node) {
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

  @FXML
  public void saveChanges() {
    ViewManager.getDataCont().saveChanges();
    try {
      toggleEditMap(new ActionEvent());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void discardChanges() {
    ViewManager.getDataCont().discardChanges();
    try {
      toggleEditMap(new ActionEvent());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected Circle setMouseActions(Circle circle, HospitalMapNode node) {
    circle.setOnMouseEntered(
        t -> {
          Circle newCircle =
              (Circle) mapPane.getChildren().get(mapPane.getChildren().indexOf(circle));
          newCircle.setFill(Color.YELLOW);
          circle.setStyle("-fx-cursor: hand");
        });

    circle.setOnMouseClicked(
        t -> {
          if (t.getButton() == MouseButton.PRIMARY) {
            if (!isDrag) {
              nodeMenu.setVisible(ViewManager.toggleNode(node));
            } else {
              panAllowed = true;
              ViewManager.setSelectedNode(null);
              isDrag = false;
              Point2D mousePress = imageViewToImage(mapImage, new Point2D(t.getX(), t.getY()));
              movingNode.setxCoord((int) (mousePress.getX() / fullImgWidth * 100000));
              movingNode.setyCoord((int) (mousePress.getY() / fullImgHeight * 100000));
              ViewManager.getDataCont().editNode(movingNode.getID(), movingNode);
            }
            nodeDeleteButton.setOnAction(
                e -> {
                  nodeMenu.setVisible(ViewManager.toggleNode(node));
                  ViewManager.getDataCont().deleteNode(node.getID());
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
          panAllowed = false;
          Circle newCircle =
              (Circle) mapPane.getChildren().get(mapPane.getChildren().indexOf(circle));
          newCircle.setFill(Color.YELLOW);
          newCircle.setCenterX(t.getX());
          newCircle.setCenterY(t.getY());
          HospitalMapNode newNode =
              new HospitalMapNode(node.getID(), node.getMapID(), 0, 0, node.getConnections());
          movingNode = newNode;
          isDrag = true;
        });
    return circle;
  }
}
