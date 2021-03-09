package edu.wpi.cs3733.c21.teamI.view.maps;

import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.hospitalMap.*;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.LocationCategory;
import edu.wpi.cs3733.c21.teamI.hospitalMap.LocationNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.MapDataEntity;
import edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing.MapEditDataController;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;

public class MapEditingController extends MapController {
  @FXML StackPane rootPane;
  @FXML Button save, discard, undoButton, redoButton, navigateButton;
  @FXML Button nodeDeleteButton, saveButton;
  @FXML TextField nodePath, edgePath;
  protected HospitalMapNode movingNode;
  private static final MapEditDataController dataCont = new MapEditDataController();
  private boolean shift;

  // setup stuff
  @FXML
  public void initialize() throws IOException {
    System.out.println("Initializing editing controller...");
    navigateButton.setVisible(true);
    currentMapID = "Faulkner Lot";
    currentTab = null;
    campusTab(new ActionEvent());
  }

  @FXML
  public void toggleEditMap(ActionEvent e) throws IOException {
    StackPane replacePane = (StackPane) rootPane.getParent();
    replacePane.getChildren().clear();
    replacePane
        .getChildren()
        .add(FXMLLoader.load(getClass().getResource("/fxml/Pathfinding.fxml")));
  }

  // viewport stuff

  public void updateView() {
    if (isFirstLoad) {
      isFirstLoad = false;
      setAddNodeHandler();
      undoButton.setOnAction(
          e -> {
            if (dataCont.isUndoAvailable()) {
              dataCont.undo();
            }
            update();
          });
      redoButton.setOnAction(
          e -> {
            if (dataCont.isRedoAvailable()) {
              dataCont.redo();
            }
            update();
          });
    }
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
    nodeMenu.setVisible(selectedInActiveMap() && selectedNode.size() != 0 && adminMap);
    update();
  }

  protected void update() {
    // set datacont to the correct map ID if null or outdated
    if (dataCont.getActiveMap() == null || !dataCont.getActiveMap().getId().equals(currentMapID)) {
      System.out.println("Loading fresh map rom memory");
      dataCont.setActiveMap(MapDataEntity.getMap().get(currentMapID));
      dataCont.discardChanges();
    }
    mapPane.getChildren().clear();
    drawSelectedNode();
    for (HospitalMapNode node : dataCont.getActiveMap().getNodes()) { // same
      drawEdges(node);
    }
    for (HospitalMapNode node : dataCont.getActiveMap().getNodes()) { // same
      makeNodeCircle(node);
    }
    if (dataCont.isUndoAvailable()) {
      undoButton.setOpacity(1);
    } else {
      undoButton.setOpacity(0.2);
    }

    if (dataCont.isRedoAvailable()) {
      redoButton.setOpacity(1);
    } else {
      redoButton.setOpacity(0.2);
    }
  }

  // drawing new nodes stuff

  // TODO NOT THIS ANYTHING BUT THIS
  protected static AtomicInteger idGen = new AtomicInteger();

  protected String randomGenerate() {
    return "BadSol" + idGen.incrementAndGet();
  }

  protected void setAddNodeHandler() {
    EventHandler<? super MouseEvent> eventHandler =
        (EventHandler<MouseEvent>)
            e -> {
              if (e.getButton() == MouseButton.SECONDARY) {
                // definitely need a better way of making an ID
                Point2D mousePress = imageViewToImage(mapImage, new Point2D(e.getX(), e.getY()));
                dataCont.addNode(
                    new HospitalMapNode(
                        randomGenerate(),
                        currentMapID,
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
      dataCont.editNode(node.getID(), newNode);
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
      dataCont.editNode(node.getID(), newNode);
    }
  }

  public void toggleNode(HospitalMapNode node) {
    if (selectedNode.size() == 0) {
      selectedNode.add(node);
      nodeMenu.setVisible(true);

    } else if (selectedNode.size() == 1) {
      nodeMenu.setVisible(false);
      if (selectedNode.get(0).equals(node)) {
        selectedNode.clear();

      } else {
        dataCont.addEdge(node, selectedNode.get(0));
        selectedNode.clear();
      }
    } else {
      nodeMenu.setVisible(true);
      selectedNode.clear();
      selectedNode.add(node);
    }
  }

  protected void drawEdges(HospitalMapNode parent) {
    Image xIconImg = new Image("/fxml/fxmlResources/redxicon.png");
    for (HospitalMapNode child : parent.getConnections()) {
      HospitalMapNode startNode =
          dataCont.getActiveMap().getNodes().contains(parent) ? parent : child;
      HospitalMapNode endNode = child;
      Color color = Color.ORANGE;
      if (!dataCont.getActiveMap().getNodes().contains(child)
          || !dataCont.getActiveMap().getNodes().contains(parent)) {
        endNode =
            new HospitalMapNode(
                null,
                null,
                (int) (startNode.getxCoord() - mapPane.getPrefWidth()),
                (int) (startNode.getyCoord() - mapPane.getPrefWidth()),
                null);
        color = Color.GREEN;
      }
      ImageView xMarker = new ImageView();
      xMarker.setImage(xIconImg);
      xMarker.setFitHeight(12);
      xMarker.setFitWidth(12);
      xMarker.setVisible(false);
      xMarker.setStyle("-fx-cursor: hand");
      mapPane.getChildren().add(xMarker);
      Line line =
          LineBuilder.create()
              .startX(transformX(startNode.getxCoord()))
              .startY(transformY(startNode.getyCoord()))
              .endX(transformX(endNode.getxCoord()))
              .endY(transformY(endNode.getyCoord()))
              .stroke(color)
              .strokeWidth(10 / scale)
              .build();
      line.setStyle("-fx-cursor: hand");
      mapPane.getChildren().add(line);
      HospitalMapNode finalEndNode = endNode;
      line.setOnMouseEntered(
          t -> {
            xMarker.setVisible(true);
            xMarker.toFront();
            xMarker.setX(
                transformX((startNode.getxCoord() + finalEndNode.getxCoord()) / 2)
                    - xMarker.getFitWidth() / 2);
            xMarker.setY(
                transformY((startNode.getyCoord() + finalEndNode.getyCoord()) / 2)
                    - xMarker.getFitHeight() / 2);
          });
      line.setOnMouseExited(
          t -> {
            xMarker.setVisible(false);
          });
      line.setOnMouseClicked(
          t -> {
            mapPane.getChildren().remove(line);
            dataCont.deleteEdge(parent, child);
            update();
          });
      xMarker.setOnMouseEntered(
          t -> {
            xMarker.setVisible(true);
          });
      xMarker.setOnMouseClicked(
          t -> {
            mapPane.getChildren().remove(line);
            dataCont.deleteEdge(parent, child);
            update();
          });
      xMarker.setOnMouseExited(
          t -> {
            xMarker.setVisible(false);
          });
    }
  }

  @FXML
  protected void straightenSelected(ActionEvent e) {
    int minX = selectedNode.stream().mapToInt(HospitalMapNode::getxCoord).min().getAsInt();
    int minY = selectedNode.stream().mapToInt(HospitalMapNode::getyCoord).min().getAsInt();
    int maxX = selectedNode.stream().mapToInt(HospitalMapNode::getxCoord).max().getAsInt();
    int maxY = selectedNode.stream().mapToInt(HospitalMapNode::getyCoord).max().getAsInt();
    double xAvg =
        selectedNode.stream().mapToInt(HospitalMapNode::getxCoord).average().getAsDouble();
    double yAvg =
        selectedNode.stream().mapToInt(HospitalMapNode::getyCoord).average().getAsDouble();

    for (HospitalMapNode selected : selectedNode) {
      if (maxX - minX > maxY - minY) {
        selected.setyCoord((int) yAvg);
        dataCont.editNode(selected.getID(), selected);
      } else {
        selected.setxCoord((int) xAvg);
        dataCont.editNode(selected.getID(), selected);
      }
    }
    update();
  }

  protected Node setMouseActions(Node circle, HospitalMapNode node) {
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
              if (t.isShiftDown() && node.getMapID().equals(selectedNode.get(0).getMapID())) {
                shift = true;
                if (selectedNode.contains(node)) {
                  selectedNode.remove(node);
                } else {
                  selectedNode.add(node);
                }
                nodeMenu.setVisible(selectedNode.size() == 1);
              } else {
                shift = false;
                toggleNode(node);
              }
            } else {
              panAllowed = true;
              isDrag = false;
              Point2D mousePress = imageViewToImage(mapImage, new Point2D(t.getX(), t.getY()));
              int currentX = (int) (mousePress.getX() / fullImgWidth * 100000);
              int currentY = (int) (mousePress.getY() / fullImgHeight * 100000);

              if (shift) {
                int x = 0, y = 0;
                for (HospitalMapNode n : this.selectedNode) {
                  if (n.getID().equals(movingNode.getID())) {
                    x = n.getxCoord();
                    y = n.getyCoord();
                  }
                }
                int xOffset = currentX - x;
                int yOffset = currentY - y;
                this.selectedNode.removeIf(n -> n.getID().equals(movingNode.getID()));
                for (HospitalMapNode n : this.selectedNode) {
                  n.setxCoord(n.getxCoord() + xOffset);
                  n.setyCoord(n.getyCoord() + yOffset);
                  dataCont.editNode(n.getID(), n);
                }
              }
              movingNode.setxCoord(currentX);
              movingNode.setyCoord(currentY);
              dataCont.editNode(movingNode.getID(), movingNode);

              this.selectedNode.clear();
            }
            nodeDeleteButton.setOnAction(
                e -> {
                  toggleNode(node);
                  dataCont.deleteNode(node.getID());
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
          if (node instanceof LocationNode) {
            newCircle.setFill(Color.DARKRED);
          } else {
            newCircle.setFill(Color.RED);
          }
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
          HospitalMapNode newNode;
          if (node instanceof LocationNode) {
            newNode =
                new LocationNode(
                    node.getID(),
                    node.getMapID(),
                    0,
                    0,
                    ((LocationNode) node).getShortName(),
                    ((LocationNode) node).getLongName(),
                    ((LocationNode) node).getLocationCategory(),
                    ((LocationNode) node).getTeamAssigned(),
                    node.getConnections());
          } else {
            newNode =
                new HospitalMapNode(node.getID(), node.getMapID(), 0, 0, node.getConnections());
          }
          movingNode = newNode;
          isDrag = true;
        });
    return circle;
  }

  // save/discard/data management
  @FXML
  public void saveChanges() {
    dataCont.saveChanges();
    update();
  }

  @FXML
  public void discardChanges() {
    dataCont.discardChanges();
    try {
      toggleEditMap(new ActionEvent());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // saving CSV

  @FXML
  public void save(ActionEvent actionEvent) {
    HospitalMapCSVBuilder.saveCSV(
        NavDatabaseManager.getInstance().loadMapsFromMemory().values(),
        nodePath.getText(),
        edgePath.getText());
  }

  @FXML
  public void load(ActionEvent actionEvent) {
    Map<String, HospitalMap> maps =
        HospitalMapCSVBuilder.loadCSV(nodePath.getText(), edgePath.getText());
    NavDatabaseManager.getInstance().saveMapsIntoMemory(maps.values());
  }
}
