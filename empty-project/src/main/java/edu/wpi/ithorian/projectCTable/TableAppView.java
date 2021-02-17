package edu.wpi.ithorian.projectCTable;

import edu.wpi.ithorian.ReadCSV;
import edu.wpi.ithorian.hospitalMap.EuclidianDistCalc;
import edu.wpi.ithorian.hospitalMap.HospitalMap;
import edu.wpi.ithorian.hospitalMap.HospitalMapNode;
import edu.wpi.ithorian.pathfinding.ImageHandler;
import edu.wpi.ithorian.pathfinding.PathFinder;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class TableAppView extends Application {

  private TableController tableController;

  @Override
  public void init() {
    System.out.println("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    /*
    System.out.println("Table manager: " + tableController);
    Group root = new Group();
    root.getChildren().add(FXMLLoader.load(getClass().getResource("/fxml/NodeTable.fxml")));
    primaryStage.setTitle("Table View");
    Scene tableScene = new Scene(root, 1000, 400);
    primaryStage.setScene(tableScene);
    System.out.println("Table scene: " + tableScene);
    primaryStage.show();
    */

    String path =
        System.getProperty("user.dir") + "/src/main/java/edu/wpi/ithorian/hospitalMap/mapEditing/";
    Set<HospitalMapNode> nodes =
        HospitalMap.generateElementFromData(
            ReadCSV.readFromFile(path + "MapINodes.csv"),
            ReadCSV.readFromFile(path + "MapIEdges.csv"));
    HospitalMap map =
        new HospitalMap("Test_Map", "Test Map", "Building1", 1, path + "FaulknerCampus.png", nodes);
    int scale = 2;

    System.out.println(map.getNodes());

    EuclidianDistCalc scorer = new EuclidianDistCalc();
    List<HospitalMapNode> aStar =
        PathFinder.findPath(map.getNode("IPARK00101"), map.getNode("IPARK02401"), scorer);
    System.out.println(aStar);

    // creating the image object
    InputStream stream =
        new FileInputStream("build/resources/main/fxml/fxmlResources/FaulknerFloor1.png");
    Image image = new Image(stream);
    // Creating the image view
    ImageView imageView = new ImageView();
    // Setting image to the image view
    imageView.setImage(image);
    // Setting the image view parameters
    imageView.setX(0);
    imageView.setX(0);
    imageView.setY(0);
    imageView.setFitWidth(image.getWidth() / scale);
    imageView.setPreserveRatio(true);
    // Setting the Scene object
    Group root = new Group();
    ImageHandler handler = new ImageHandler(map.getNodes(), imageView, scale, root);
    // draw a*
    handler.drawPath(aStar);

    Scene scene = new Scene(root, image.getWidth() / scale, image.getHeight() / scale);
    primaryStage.setTitle("Floor Map");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  @Override
  public void stop() {
    System.out.println("Shutting Down");
  }
}
