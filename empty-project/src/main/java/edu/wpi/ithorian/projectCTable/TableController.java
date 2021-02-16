package edu.wpi.ithorian.projectCTable;

import edu.wpi.ithorian.ReadCSV;
import java.util.List;
import javafx.scene.Group;
import javafx.stage.Stage;

public class TableController {

  private static TableController ourInstance;
  private Group root = null;
  private Stage stage = null;
  private TableAppView tableAppView = null;

  public static void init() {
    ourInstance = new TableController();
  }

  public static TableController getInstance() {
    return ourInstance;
  }

  public void startTableAppView() {
    this.root = new Group();
    TableAppView tableAppView = new TableAppView();
  }

  public TableController() {}

  public void setRoot(Group root) {
    this.root = root;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void addNodesToTable() {
    String path =
        System.getProperty("user.dir")
            + "/empty-project/src/main/java/edu/wpi/ithorian/hospitalMap/mapEditing/";
    List<List<String>> tableNodes = ReadCSV.readFromFile(path + "MapINodes.csv");
    for (List<String> tableNode : tableNodes) {
      NodeRow nodeRow =
          new NodeRow(
              tableNode.get(0),
              tableNode.get(1),
              tableNode.get(2),
              tableNode.get(3),
              tableNode.get(4),
              tableNode.get(5),
              tableNode.get(6),
              tableNode.get(7),
              tableNode.get(8));
      tableAppView.addNodeRow(nodeRow);
    }
  }

  public static class NodeRow {
    public String nodeIdCol,
        xCoordCol,
        yCoordCol,
        floorCol,
        buildingCol,
        nodeTypeCol,
        longNameCol,
        shortNameCol,
        teamAssigned;

    public NodeRow(
        String nodeIdCol,
        String xCoordCol,
        String yCoordCol,
        String floorCol,
        String buildingCol,
        String nodeTypeCol,
        String longNameCol,
        String shortNameCol,
        String teamAssigned) {
      this.nodeIdCol = nodeIdCol;
      this.xCoordCol = xCoordCol;
      this.yCoordCol = yCoordCol;
      this.floorCol = floorCol;
      this.buildingCol = buildingCol;
      this.nodeTypeCol = nodeTypeCol;
      this.longNameCol = longNameCol;
      this.shortNameCol = shortNameCol;
      this.teamAssigned = teamAssigned;
    }
  }
}
