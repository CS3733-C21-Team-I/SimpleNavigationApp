package edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing;

import edu.wpi.cs3733.c21.teamI.hospitalMap.EuclidianDistCalc;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.pathfinding.PathFinder;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ApplicationView extends Application {
  @FXML
  Button map,
      serviceRequests,
      mapReturn,
      requestsReturn,
      sanitationReturn,
      maintenanceReturn,
      loginReturn,
      maintenance,
      start1,
      start2,
      start3,
      start4,
      start5,
      dest1,
      dest2,
      dest3,
      dest4,
      dest5,
      login;
  @FXML ImageView mapImage, adminPath;
  @FXML HBox destinationPoint;
  @FXML TextField start, destination;
  @FXML Label dateTime;
  @FXML AnchorPane mapPane;
  @FXML Button loginButton;
  @FXML VBox requestContainer;
  @FXML TextField username;
  @FXML PasswordField password;
  @FXML Label headerLabel;

  private final double scale = 3.05;
  private EuclidianDistCalc scorer;

  public String uName;
  public static String pass;

  boolean adminMap = false;
  private static MapEditManager ourManager;
  private final MapEditManager mapManager;
  @FXML ScrollPane requestScrollPane;

  public ApplicationView() {
    this.mapManager = ourManager;
  }

  public ApplicationView(MapEditManager mapManager) {
    this.mapManager = mapManager;
  }

  public static void saveManager() {
    ourManager = new MapEditManager().getInstance();
  }

  @Override
  public void init() {}

  @Override
  public void start(Stage primaryStage) throws IOException {
    Group root = mapManager.getRoot();
    root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Home.fxml")));
    primaryStage.setTitle("Home");
    mapManager.setRoot(root);
    Scene applicationScene = new Scene(root, 973, 800);
    primaryStage.setScene(applicationScene);
    mapManager.setStage(primaryStage);
    primaryStage.show();
  }

  @FXML
  public void navigate(ActionEvent e) throws IOException {
    Group root = mapManager.getRoot();
    Scene scene = ((Button) e.getSource()).getScene();
    root.getChildren().clear();
    if (e.getSource() == mapReturn
        || e.getSource() == requestsReturn
        || e.getSource() == loginReturn) {
      root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Home.fxml")));
    } else if (e.getSource() == sanitationReturn || e.getSource() == maintenanceReturn) {
      root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Requests.fxml")));
    } else if (e.getSource() == map) {
      adminMap = false;
      root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Map.fxml")));
    } else if (e.getSource() == serviceRequests) {
      root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Requests.fxml")));
    } else if (e.getSource() == maintenance) {
      root.getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/MaintenanceRequest.fxml")));
    } else if (e.getSource() == login) {
      root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/Login.fxml")));
    } else {
      root.getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/SanitationRequest.fxml")));
    }
    mapManager.setRoot(root);
    scene.setRoot(root);
  }

  @FXML
  public void initialize() {
    initClock();
  }

  @FXML
  public void initClock() {
    Timeline clock =
        new Timeline(
            new KeyFrame(
                Duration.ZERO,
                e -> {
                  DateTimeFormatter formatter =
                      DateTimeFormatter.RFC_1123_DATE_TIME; // "yyyy-MM-dd HH:mm:ss"
                  if (dateTime != null) {
                    String dateTimeString = ZonedDateTime.now().format(formatter);
                    dateTime.setText(dateTimeString.substring(0, dateTimeString.length() - 9));
                  }
                }),
            new KeyFrame(Duration.seconds(1)));
    clock.setCycleCount(Animation.INDEFINITE);
    clock.play();
  }

  @FXML
  private Button getMenuButton(int index, boolean isStart) {
    switch (index) {
      case 0:
        return isStart ? start1 : dest1;
      case 1:
        return isStart ? start2 : dest2;
      case 2:
        return isStart ? start3 : dest3;
      case 3:
        return isStart ? start4 : dest4;
      default:
        return isStart ? start5 : dest5;
    }
  }

  @FXML
  private void clearMenus() {
    for (int i = 0; i < 5; i++) {
      getMenuButton(i, true).setVisible(false);
      getMenuButton(i, false).setVisible(false);
    }
  }

  @FXML
  public void autoFillStart(MouseEvent e) {
    start.setText(((Button) e.getSource()).getText());
  }

  @FXML
  public void autoFillDest(MouseEvent e) {
    destination.setText(((Button) e.getSource()).getText());
  }

  @FXML
  public void toggleEditMap(ActionEvent e) {
    mapManager.startEditorView(mapPane);
    adminMap = !adminMap;
  }

  @FXML
  public void exit() {
    mapManager.getStage().close();
  }

  @FXML
  public void login() {
    uName = username.getText();
    pass = password.getText();
    headerLabel.setText("You successfully logged in.");
    System.out.println(uName + ' ' + pass);
    generateRequestList();
  }

  public void enterServiceTicketPage(ServiceTicket st) throws IOException {
    mapManager.startRequestView(st);
  }

  private void generateRequestList() {
    ServiceTicket ticket1 =
        new ServiceTicket(
            11, 12, 13, ServiceTicket.TicketType.MAINTENANCE, "somewhere", "info", false);
    ServiceTicket ticket2 =
        new ServiceTicket(
            21, 22, 23, ServiceTicket.TicketType.LAUNDRY, "somewhere else", "more info", false);
    ServiceTicket ticket3 =
        new ServiceTicket(
            31, 32, 33, ServiceTicket.TicketType.FOOD, "elsewhere", "other info", false);
    List<ServiceTicket> requests =
        new ArrayList<ServiceTicket>(Arrays.asList(ticket1, ticket2, ticket3));
    List<String> requestNames =
        requests.stream()
            .map(st -> st.getTicketType() + " #" + st.getTicketId())
            .collect(Collectors.toList());
    requestScrollPane.getStylesheets().add("/fxml/fxmlResources/main.css");
    for (int i = 0; i < requestNames.size(); i++) {
      Button requestButton = new Button(requestNames.get(i));
      int finalI = i;
      requestButton.setOnAction(
          event -> {
            try {
              mapManager.startRequestView(requests.get(finalI));
            } catch (IOException e) {
              e.printStackTrace();
            }
          });
      requestButton.getStyleClass().add("requestButton");
      requestButton.setMinHeight(50);
      requestButton.setMaxWidth(requestContainer.getWidth());
      requestContainer.getChildren().add(requestButton);
    }
    requestScrollPane.setVisible(true);
  }

  public void redrawMap() {
    mapManager
        .getRoot()
        .getChildren()
        .removeIf(n -> (n.getClass() == Line.class) || (n.getClass() == Circle.class));
    for (HospitalMapNode node : mapManager.getDataCont().getActiveMap().getNodes()) {
      drawEdges(node);
      drawNode(node, Color.RED);
    }
  }

  @FXML
  public void getDirections(ActionEvent e) {
    String begin = start.getText();
    String end = destination.getText();
    drawPathBetweenNodes(begin, end);
  }

  @FXML
  public void deletePath() {
    mapPane.getChildren().clear();
  }

  @FXML
  public void drawPathBetweenNodes(String aID, String bID) {
    // redrawMap();
    mapManager
        .getRoot()
        .getChildren()
        .removeIf(n -> (n.getClass() == Line.class) || (n.getClass() == Circle.class));

    this.scorer = new EuclidianDistCalc();
    HospitalMapNode nodeA = mapManager.getDataCont().getActiveMap().getNode(aID);
    HospitalMapNode nodeB = mapManager.getDataCont().getActiveMap().getNode(bID);
    List<HospitalMapNode> aStarPath = PathFinder.findPath(nodeA, nodeB, scorer);
    drawPath(aStarPath);
    drawNode(nodeA, Color.BLUE);
    drawNode(nodeB, Color.BLUE);
  }

  private void drawNode(HospitalMapNode node, Color color) {
    Circle circle =
        new Circle((node.getxCoord() / scale) - 3, (node.getyCoord() / scale), 13 / scale);
    circle.setFill(color);
    mapPane.getChildren().add(circle);
  }

  private void drawEdges(HospitalMapNode parent) {
    for (HospitalMapNode child : parent.getConnections()) {
      drawEdge(child, parent, Color.RED);
    }
  }

  private void drawEdge(HospitalMapNode start, HospitalMapNode end, Color color) {
    Line line =
        LineBuilder.create()
            .startX((start.getxCoord() / scale) - 3)
            .startY((start.getyCoord() / scale))
            .endX((end.getxCoord() / scale) - 3)
            .endY((end.getyCoord() / scale))
            .stroke(color)
            .strokeWidth(14 / scale)
            .build();
    mapPane.getChildren().add(line);
  }

  public void drawPath(List<HospitalMapNode> path) {
    HospitalMapNode currNode;
    HospitalMapNode nextNode = null;
    for (int i = 0; i < path.size() - 1; i++) {
      currNode = path.get(i);
      nextNode = path.get(i + 1);
      drawEdge(currNode, nextNode, Color.BLUE);
    }
  }

  public void lookup(KeyEvent e) {
    String matchString =
        (((TextField) e.getSource()).getText()
            + (!e.getCharacter().equals(Character.toString((char) 8)) ? e.getCharacter() : "")
                .toLowerCase());
    ArrayList<String> nodeIDs =
        new ArrayList<String>(
            mapManager.getDataCont().getActiveMap().getNodes().stream()
                .map(n -> n.getID())
                .collect(Collectors.toList()));
    ArrayList<String> matches = new ArrayList<>();
    for (String location : nodeIDs) {
      if (location.toLowerCase().contains(matchString)) {
        matches.add(location);
      }
    }

    clearMenus();
    for (int i = 0; i < matches.size(); i++) {
      if (i < 5) {
        getMenuButton(i, e.getSource() == start).setVisible(true);
        getMenuButton(i, e.getSource() == start).setText(matches.get(i));
      } else {
        break;
      }
    }
  }
}
