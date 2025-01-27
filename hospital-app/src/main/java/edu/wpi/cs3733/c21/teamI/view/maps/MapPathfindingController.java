package edu.wpi.cs3733.c21.teamI.view.maps;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.hospitalMap.*;
import edu.wpi.cs3733.c21.teamI.pathfinding.*;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicketDataController;
import edu.wpi.cs3733.c21.teamI.user.User;
import edu.wpi.cs3733.c21.teamI.util.ImageLoader;
import edu.wpi.cs3733.c21.teamI.util.InputChecking;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import lombok.SneakyThrows;

public class MapPathfindingController extends MapController {
  @FXML ImageView mapImage;
  @FXML Button adminMapToggle;
  @FXML JFXComboBox algorithmPick;
  @FXML TextField start, destination;
  @FXML ListView startList, destList;
  @FXML VBox directionsField;
  @FXML VBox viewDisplay;

  private ArrayList<StepType> floorChangeTypes =
      new ArrayList<>(Arrays.asList(StepType.ELEVATOR, StepType.STAIR, StepType.EXIT));

  private EuclidianDistCalc scorer = new EuclidianDistCalc();
  private AlgorithmSelectionStrategyPattern pathFinderAlgorithm =
      new AlgorithmSelectionStrategyPattern(new A_Star<>());

  private List<HospitalMapNode> foundPath;
  private ArrayList<String> foundPathDescription;
  public static MapPathfindingController lastInitialized = null;

  double overviewpadding = 100;
  double stepPadding = 50;

  public MapPathfindingController() {}

  // setup stuff
  @FXML
  public void initialize() {
    System.out.println("Initializing pathfinding controller");
    lastInitialized = this;
    boolean isAdmin =
        ApplicationDataController.getInstance()
            .getLoggedInUser()
            .hasPermission(User.Permission.EDIT_MAP);
    adminMapToggle.setVisible(isAdmin);
    adminMapToggle.setManaged(isAdmin);
    algorithmPick.setVisible(isAdmin);
    algorithmPick.setManaged(isAdmin);
    algorithmPick.getItems().addAll("A*", "Depth First", "Breadth First", "Dijkstra");
    reflectCovidStatus(
        ApplicationDataController.getInstance().getLoggedInUser().getCovidRisk()
            == User.CovidRisk.COVID_RISK);
    setupMapViewHandlers();
    currentMapID = "Faulkner Lot";
    campusTab(new ActionEvent());
  }

  @FXML
  public void toggleEditMap(ActionEvent e) throws IOException {
    StackPane replacePane = (StackPane) rootPane.getParent();
    replacePane.getChildren().clear();
    replacePane
        .getChildren()
        .add(FXMLLoader.load(getClass().getResource("/fxml/map/Pathediting.fxml")));
  }

  @FXML
  public void toGoogleMaps(ActionEvent e) throws IOException {
    StackPane replacePane = (StackPane) rootPane.getParent();
    replacePane.getChildren().clear();
    replacePane
        .getChildren()
        .add(FXMLLoader.load(getClass().getResource("/fxml/map/GoogleMapsMain.fxml")));
  }

  // viewport stuff
  public void updateView() {

    Image background =
        ImageLoader.loadImage("/fxml/map/mapImages/" + currentMapID.replace(" ", "") + ".png");
    mapImage.setImage(background);
    fullImgWidth = background.getWidth();
    fullImgHeight = background.getHeight();
    imgWidth = background.getWidth();
    imgHeight = background.getHeight();

    update();
  }

  public void update() {
    mapPane.getChildren().clear();
    drawLocationNodes();
    if (foundPathExists()) {
      drawCalculatedPath(getFoundPath());
    }
  }

  private void populateDirections(List<DirectionStep> directionSteps) {
    directionsField.getChildren().clear();
    Label floorLabel = new Label(directionSteps.get(0).getPointA().getMapID());
    floorLabel.setAlignment(Pos.BASELINE_CENTER);
    floorLabel.setMaxWidth(directionsField.getWidth());
    floorLabel.setStyle("-fx-font-weight: bold; -fx-background-color: white");
    floorLabel.setPadding(new Insets(10, 0, 0, 0));
    directionsField.getChildren().add(floorLabel);
    for (DirectionStep step : directionSteps) {
      JFXButton button = new JFXButton(step.stepDetails);
      button.wrapTextProperty().set(true);
      button.setMaxWidth(directionsField.getWidth());
      button.setRipplerFill(Color.valueOf("#0067b1"));
      String styleString = "-fx-alignment: center-left; -fx-cursor:hand; ";
      button
          .styleProperty()
          .bind(
              Bindings.when(button.hoverProperty())
                  .then(new SimpleStringProperty(styleString + "-fx-background-color: #cdeaff"))
                  .otherwise(
                      new SimpleStringProperty(styleString + "-fx-background-color: white")));
      Image icon = new Image(step.getIconPath());
      ImageView imgView = new ImageView(icon);
      imgView.setFitHeight(20);
      imgView.setFitWidth(20);
      button.setGraphic(imgView);
      button.setPadding(new Insets(10, 10, 10, 10));
      button.setOnAction((event) -> zoomToStep(step, stepPadding));
      directionsField.getChildren().add(button);
      if (floorChangeTypes.contains(step.getStepType())) {
        floorLabel = new Label(step.getPointB().getMapID());
        floorLabel.setAlignment(Pos.CENTER);
        floorLabel.setMaxWidth(directionsField.getWidth());
        floorLabel.setStyle("-fx-font-weight: bold; -fx-background-color: white");
        floorLabel.setPadding(new Insets(10, 0, 0, 0));
        directionsField.getChildren().add(floorLabel);
      }
    }
  }

  @FXML
  private EventHandler<ActionEvent> zoomToStep(DirectionStep step, double padding) {
    // System.out.println("A: " + step.getPointA() + " B: " + step.getPointB());
    if (!step.getPointA().getMapID().equals(currentMapID)) {
      goToTab(step.getPointA().getMapID());
    }
    zoomToFitNodes(step.getPointA(), step.getPointB(), padding);
    return null;
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
    List<String> locations = NavDatabaseManager.getInstance().getLocationNodeLongNames();

    start.setOnMouseClicked(
        (MouseEvent e) -> {
          ObservableList<String> items = FXCollections.observableArrayList(locations);
          startList.setItems(items);
          startList.setVisible(true);
        });
    destination.setOnMouseClicked(
        (MouseEvent e) -> {
          ObservableList<String> items = FXCollections.observableArrayList(locations);
          destList.setItems(items);
          destList.setVisible(true);
        });

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
    viewDisplay.setOnMouseClicked(
        (MouseEvent e) -> {
          startList.setVisible(false);
          destList.setVisible(false);
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

    // Zooms to fit entire path
    if (foundPath.size() >= 2) {
      goToTab(foundPath.get(0).getMapID());
      zoomToFitNodes(foundPath.get(0), lastNodeOnSameFloor(foundPath), overviewpadding);
    }
    return foundPath;
  }

  private HospitalMapNode lastNodeOnSameFloor(List<HospitalMapNode> path) {
    for (int i = path.size() - 1; i > 1; i--) {
      if (path.get(i).getMapID().equals(currentMapID)) {
        System.out.println("Last Node on Floor: " + path.get(i).getID());
        return path.get(i);
      }
    }
    return null;
  }

  private HospitalMapNode firstNodeOnSameFloor(List<HospitalMapNode> path) {
    for (int i = 0; i < path.size() - 2; i++) {
      if (path.get(i).getMapID().equals(currentMapID)) {
        System.out.println("First Node on Floor: " + path.get(i).getID());
        return path.get(i);
      }
    }
    return null;
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
  public void getDirections(ActionEvent e) {
    String begin = start.getText();
    String end = destination.getText();
    if (InputChecking.validLocationName(begin)) {
      if (InputChecking.validLocationName(end)) {
        System.out.println(begin + " " + end);

        HospitalMapNode nodeA = MapDataEntity.getNodeByLongName(begin);
        HospitalMapNode nodeB = MapDataEntity.getNodeByLongName(end);
        getFoundPath(nodeA, nodeB);
        if (foundPathExists()) goToTab(foundPath.get(0).getMapID());
        update();
      } else {
        System.out.println("Invalid destination entered");
      }

    } else {
      System.out.println("Invalid starting point");
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
        drawLocationNodes();
        if (nodeA.getMapID().equals(currentMapID)) drawStartPoint(foundPath);
        if (nodeB.getMapID().equals(currentMapID)) drawEndPoint(foundPath);
      } catch (IOException e) {
        drawLocationNodes();
        e.printStackTrace();
      }

      showButtonToNextMapOnPath(foundPath);
      populateDirections(TextDirections.getDirectionSteps());
    }
  }

  // algorithm stuff

  @FXML
  private void switchAlgorithm() {
    switch (algorithmPick.getValue().toString()) {
      case "Depth First":
        // System.out.println("Making new Depth first search");
        pathFinderAlgorithm.setPlanning(new DepthFirstSearch());
        break;
      case "Breadth First":
        // System.out.println("Making new Breadth first search");
        pathFinderAlgorithm.setPlanning(new BreadthFirstSearch());
        break;
      case "Dijkstra":
        // System.out.println("Making new Dijkstra");
        pathFinderAlgorithm.setPlanning(new Dijkstra());
        break;
      default:
        // System.out.println("Making new A*");
        pathFinderAlgorithm.setPlanning(new A_Star());
        break;
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
    clearFoundPath();
    ObservableList<String> items = FXCollections.observableArrayList(new ArrayList<String>());
    directionsField.getChildren().clear();
    update();
  }

  @FXML
  public void toggleAccessible(ActionEvent e) {
    if (scorer.nodeTypesToAvoid.contains(NodeRestrictions.WHEELCHAIR_INACCESSIBLE)) {
      scorer.nodeTypesToAvoid.remove(NodeRestrictions.WHEELCHAIR_INACCESSIBLE);
    } else {
      scorer.nodeTypesToAvoid.add(NodeRestrictions.WHEELCHAIR_INACCESSIBLE);
    }
    System.out.print("NodeRestrictions:" + scorer.nodeTypesToAvoid);
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
  public void toAboutPage(ActionEvent e) throws IOException {
    rootPane.getChildren().clear();
    rootPane.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/AboutPage.fxml")));
    JFXButton backBtn = (JFXButton) rootPane.lookup("#backBtn");
    backBtn.setOnAction(
        new EventHandler<ActionEvent>() {
          @SneakyThrows
          @Override
          public void handle(ActionEvent event) {
            rootPane.getChildren().clear();
            rootPane
                .getChildren()
                .add(FXMLLoader.load(getClass().getResource("/fxml/map/Pathfinding.fxml")));
          }
        });
  }

  protected Node setMouseActions(Node circle, HospitalMapNode node) {
    circle.setOnMouseClicked(
        t -> {
          circle.setStyle("-fx-cursor: hand");
          setStartAndEndOnClick(((LocationNode) node).getLongName());
          iconAnimation(circle);
        });
    circle.setStyle("-fx-cursor: hand");
    return circle;
  }

  public void iconAnimation(Node circle) {
    Timeline bouncer = new Timeline();
    bouncer
        .getKeyFrames()
        .addAll(
            new KeyFrame(
                new javafx.util.Duration(0), new KeyValue(circle.translateYProperty(), 0.0)),
            new KeyFrame(
                new javafx.util.Duration(100), new KeyValue(circle.translateYProperty(), -20.0)),
            new KeyFrame(
                new javafx.util.Duration(600), new KeyValue(circle.translateYProperty(), 0)));
    bouncer.play();
  }

  boolean fillStart = true;
  boolean fillDestination = false;

  public void setStartAndEndOnClick(String nodeName) {

    if (start.isFocused()) {
      start.setText(nodeName);
      destination.requestFocus();
    } else if (destination.isFocused()) {
      destination.setText(nodeName);
      start.requestFocus();
    } else if (start.getText().equals("")) {
      start.setText(nodeName);
    } else if (destination.getText().equals("")) {
      destination.setText(nodeName);
    } else if (fillStart) {
      start.setText(nodeName);
      fillStart = false;
      fillDestination = true;
    } else if (fillDestination) {
      destination.setText(nodeName);
      fillDestination = false;
      fillStart = true;
    }
  }

  public void drawLocationNodes() {
    for (HospitalMapNode node : MapDataEntity.getNodesSet()) {
      if (node instanceof LocationNode
          && node.getMapID().equals(currentMapID)) { // draw all location nodes on this level

        // draw parking spaces
        if (node instanceof ParkingNode) {
          Color strokeColor;
          Color fillColor;
          String describe;
          if (!((ParkingNode) node).isOccupied()) {
            strokeColor = Color.GREEN;
            fillColor = Color.WHITE;
            describe = "available";
          } else {
            strokeColor = Color.RED;
            fillColor = Color.RED;
            describe = "full";
          }
          Circle park = new Circle(transformX(node.getxCoord()), transformY(node.getyCoord()), 6);
          park = (Circle) setMouseActions(park, node);
          park.setFill(fillColor);
          park.setStroke(strokeColor);
          park.setStrokeWidth(4);
          Tooltip t = new Tooltip(((LocationNode) node).getLongName() + " (" + describe + ")");
          bindTooltip(park, t);
          mapPane.getChildren().add(park);
        } else {
          switch (((LocationNode) node).getLocationCategory()) { // switch case for special types
            case ELEV:
              displayIcon("/fxml/map/mapImages/mapIcons/elevator.png", node);
              break;
            case REST:
              displayIcon("/fxml/map/mapImages/mapIcons/bathroom.png", node);
              break;
            case STAI:
              displayIcon("/fxml/map/mapImages/mapIcons/stairs.png", node);
              break;
            case KIOS:
              displayIcon("/fxml/map/mapImages/mapIcons/info.png", node);
              break;
              //          case FOOD:
              //            displayIcon("/fxml/mapImages/mapIcons/dining.png", node);
              //            break;
            case PARK:
              //  displayIcon("/fxml/mapImages/mapIcons/parking.png", node);
              break;
            default:
              switch (((LocationNode) node).getLongName()) { // even specialer cases
                case "Northern Parking Icon":
                case "Western Parking Icon":
                  displayIcon("/fxml/map/mapImages/mapIcons/parking.png", node);
                  break;
                case "Cafeteria":
                case "Food Services":
                  displayIcon("/fxml/map/mapImages/mapIcons/dining.png", node);
                  break;
                case "Starbucks":
                  displayIcon("/fxml/map/mapImages/mapIcons/starbucks.png", node);
                  break;
                case "Pharmacy":
                  displayIcon("/fxml/map/mapImages/mapIcons/pharmacy.png", node);
                  break;
                case "Emergency Department":
                  displayIcon("/fxml/map/mapImages/mapIcons/emergencyRoom.png", node);
                  break;
                case "Valet Parking Icon":
                  displayIcon("/fxml/map/mapImages/mapIcons/valet.png", node);
                  break;
                default:
                  Circle circle =
                      makeCircle(
                          transformX(node.getxCoord()),
                          transformY(node.getyCoord()),
                          25 / scale,
                          Color.valueOf("#00B5E2"));
                  circle = (Circle) setMouseActions(circle, node);
                  Tooltip t = new Tooltip(((LocationNode) node).getLongName());
                  bindTooltip(circle, t);
                  mapPane.getChildren().add(circle);
                  break;
              }
          }
        }
      }
    }
  }

  public static void bindTooltip(final Node node, final Tooltip tooltip) {
    node.setOnMouseMoved(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            // +15 moves the tooltip 15 pixels below the mouse cursor to avoid flicker
            tooltip.show(node, event.getScreenX(), event.getScreenY() + 15);
          }
        });

    node.setOnMouseExited(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            tooltip.hide();
            node.setScaleX(1.0);
            node.setScaleY(1.0);
          }
        });
    node.setOnMouseEntered(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {

            node.setScaleX(1.2);
            node.setScaleY(1.2);
          }
        });
  }

  public void displayIcon(String imagePath, HospitalMapNode node) {
    double imgScale = 100 / scale;
    try {
      Image icon = new Image(getClass().getResource(imagePath).toURI().toString());
      double x = transformX(Double.valueOf(node.getxCoord())) - imgScale / 2;
      double y = transformY(Double.valueOf(node.getyCoord())) - imgScale / 2;
      // displayImage(icon, x, y, imgScale);
      ImageView imageView = new ImageView();
      // /        ImageView bouncingImageView = new ImageView();
      // Setting image to the image view
      imageView.setImage(icon);

      // Setting the image view parameters
      imageView.setX(x);
      imageView.setY(y);
      // bouncingImageView.setX(x);
      // bouncingImageView.setY(y);

      // imageView.setEffect(new Reflection());

      imageView.setFitHeight(imgScale);
      // bouncingImageView.setFitHeight(imgScale);
      imageView.setPreserveRatio(true);
      // bouncingImageView.setPreserveRatio(true);
      imageView = (ImageView) setMouseActions(imageView, node);
      Tooltip t = new Tooltip(((LocationNode) node).getLongName());
      bindTooltip(imageView, t);
      // final ImageView newImageView = imageView;

      mapPane.getChildren().add(imageView);
      // mapPane.getChildren().add(bouncingImageView);

    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  protected void drawStartPoint(List<HospitalMapNode> path) throws IOException {
    double imgScale = 256 / scale;
    Image startIcon = null;
    try {
      startIcon =
          new Image(
              (getClass().getResource("/fxml/map/mapImages/symbolIcons/startIcon.png"))
                  .toURI()
                  .toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    double startIconX = transformX(path.get(0).getxCoord()) - imgScale / 2;
    double startIconY = transformY(path.get(0).getyCoord()) - imgScale;
    // drawNode(path.get(0), blue);
    displayImage(startIcon, startIconX, startIconY, imgScale);
  }

  protected void drawEndPoint(List<HospitalMapNode> path) throws IOException {
    double imgScale = 256 / scale;
    Image finishIcon = null;
    try {
      finishIcon =
          new Image(
              (getClass().getResource("/fxml/map/mapImages/symbolIcons/finishIcon.png"))
                  .toURI()
                  .toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    double finishIconX = transformX(path.get(path.size() - 1).getxCoord()) - imgScale / 2;
    double finishIconY = transformY(path.get(path.size() - 1).getyCoord()) - imgScale;
    // drawNode(path.get(path.size() - 1), red);
    displayImage(finishIcon, finishIconX, finishIconY, imgScale);
  }

  public void pathfindingButton(String imagePath, String nextMapID, int xCoord, int yCoord) {
    Image Icon = null;

    try {
      Icon = new Image((getClass().getResource(imagePath)).toURI().toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }

    ImageView imageView = new ImageView(Icon);

    double imageViewScale = 80 / scale;
    double imgScale = 236 / scale;

    imageView.setFitHeight(imageViewScale);
    imageView.setPreserveRatio(true);
    JFXButton bttn = new JFXButton("", imageView);
    bttn.setStyle("-fx-cursor: hand;");
    bttn.setOnAction(
        new EventHandler<ActionEvent>() {

          /** Implement what you want to be returned on click here */
          @Override
          public void handle(ActionEvent event) {
            goToTab(nextMapID);
            zoomToFitNodes(
                firstNodeOnSameFloor(foundPath), lastNodeOnSameFloor(foundPath), overviewpadding);
          }
        });

    bttn.setLayoutX(transformX(xCoord) - imgScale / 2);
    bttn.setLayoutY(transformY(yCoord) - imgScale / 2);
    mapPane.getChildren().add(bttn);
  }

  protected void showButtonToNextMapOnPath(List<HospitalMapNode> path) {
    if (!path.get(path.size() - 1).getMapID().equals(currentMapID)) {
      for (int i = 0; i < path.size(); i++) {

        if (path.get(i) != path.get(path.size() - 1)
            && !path.get(i + 1).getMapID().equals(path.get(i).getMapID())
            && currentMapID.equals(path.get(i).getMapID())) {
          String Id = path.get(i + 1).getMapID();
          if (!Id.equals("Faulkner Lot")
              && !path.get(i).getMapID().equals(("Faulkner Lot"))
              && Integer.parseInt(Id.substring(Id.length() - 1))
                  < Integer.parseInt(
                      path.get(i)
                          .getMapID()
                          .substring(
                              path.get(i).getMapID().length()
                                  - 1))) { // "/fxml/map/mapImages/mapIcons/downArr.png"
            pathfindingButton(
                "/fxml/map/mapImages/mapIcons/downArr.png",
                Id,
                path.get(i).getxCoord(),
                path.get(i).getyCoord());
          }
          if (!Id.equals("Faulkner Lot")
              && !path.get(i).getMapID().equals(("Faulkner Lot"))
              && Integer.parseInt(Id.substring(Id.length() - 1))
                  > Integer.parseInt(
                      path.get(i)
                          .getMapID()
                          .substring(
                              path.get(i).getMapID().length()
                                  - 1))) { // "/fxml/map/mapImages/mapIcons/upArr.png"
            pathfindingButton(
                "/fxml/map/mapImages/mapIcons/upArr.png",
                Id,
                path.get(i).getxCoord(),
                path.get(i).getyCoord());
          }
          if (path.get(i).getMapID().equals(("Faulkner Lot"))) {
            pathfindingButton(
                "/fxml/map/mapImages/mapIcons/mapDown.png",
                Id,
                path.get(i).getxCoord(),
                path.get(i).getyCoord());
          }
          if (Id.equals(("Faulkner Lot"))) {
            pathfindingButton(
                "/fxml/map/mapImages/mapIcons/mapUp.png",
                Id,
                path.get(i).getxCoord(),
                path.get(i).getyCoord());
          }
        }
      }
    }
  }
}
