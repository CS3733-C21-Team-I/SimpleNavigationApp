package edu.wpi.ithorian.projectCTable;

import edu.wpi.ithorian.ReadCSV;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableController {

  @FXML TableView tableView;

  //  @FXML
  //  TableColumn nodeIdCol,
  //      xCoordCol,
  //      yCoordCol,
  //      floorCol,
  //      buildingCol,
  //      nodeTypeCol,
  //      longNameCol,
  //      shortNameCol,
  //      teamAssignedCol;

  public static void init() {};

  public TableController() {}

  private ObservableList<NodeRow> rows = FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    System.out.println("table controller initialize()");
    addNodesToTable();
    setup();
  }

  //  public void addNodeRow(TableController.NodeRow nodeRow) {
  //    tableView.getItems().add(nodeRow);
  //  }

  @FXML
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
      rows.add(nodeRow);
    }
    System.out.println("added rows to list: " + rows);
    System.out.println("row 1 x: " + rows.get(0).getXCoordCol());
    tableView.getItems().addAll(rows);
  }

  @FXML
  public void setup() {
    tableView.setEditable(true);

    TableColumn nodeIdCol = new TableColumn("Node ID");
    nodeIdCol.setCellValueFactory(new PropertyValueFactory<NodeRow, String>("nodeIdCol"));
    tableView.getColumns().add(nodeIdCol);

    TableColumn xCoordCol = new TableColumn("x coord");
    xCoordCol.setCellValueFactory(new PropertyValueFactory<NodeRow, String>("xCoordCol"));
    tableView.getColumns().add(xCoordCol);

    TableColumn yCoordCol = new TableColumn("y coord");
    yCoordCol.setCellValueFactory(new PropertyValueFactory<NodeRow, String>("yCoordCol"));
    tableView.getColumns().add(yCoordCol);

    TableColumn floorCol = new TableColumn("floor");
    floorCol.setCellValueFactory(new PropertyValueFactory<NodeRow, String>("floorCol"));
    tableView.getColumns().add(floorCol);

    TableColumn buildingCol = new TableColumn("building");
    buildingCol.setCellValueFactory(new PropertyValueFactory<NodeRow, String>("buildingCol"));
    tableView.getColumns().add(buildingCol);

    TableColumn nodeTypeCol = new TableColumn("node type");
    nodeTypeCol.setCellValueFactory(new PropertyValueFactory<NodeRow, String>("nodeTypeCol"));
    tableView.getColumns().add(nodeTypeCol);

    TableColumn longNameCol = new TableColumn("long name");
    longNameCol.setCellValueFactory(new PropertyValueFactory<NodeRow, String>("longNameCol"));
    tableView.getColumns().add(longNameCol);

    TableColumn shortNameCol = new TableColumn("short name");
    shortNameCol.setCellValueFactory(new PropertyValueFactory<NodeRow, String>("shortNameCol"));
    tableView.getColumns().add(shortNameCol);

    TableColumn teamAssignedCol = new TableColumn("team assigned");
    teamAssignedCol.setCellValueFactory(
        new PropertyValueFactory<NodeRow, String>("teamAssignedCol"));
    tableView.getColumns().add(teamAssignedCol);

    //    yCoordCol.setCellValueFactory(new PropertyValueFactory<NodeRow, String>("yCoordCol"));
    //    floorCol.setCellValueFactory(new PropertyValueFactory<NodeRow, String>("floorCol"));
    //    buildingCol.setCellValueFactory(new PropertyValueFactory<NodeRow, String>("buildingCol"));
    //    nodeTypeCol.setCellValueFactory(new PropertyValueFactory<NodeRow, String>("nodeTypeCol"));
    //    longNameCol.setCellValueFactory(new PropertyValueFactory<NodeRow, String>("longNameCol"));
    //    shortNameCol.setCellValueFactory(new PropertyValueFactory<NodeRow,
    // String>("shortNameCol"));
    //    teamAssignedCol.setCellValueFactory(new PropertyValueFactory<NodeRow,
    // String>("teamAssigned"));
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
        teamAssignedCol;

    public String getNodeIdCol() {
      return nodeIdCol;
    }

    public String getXCoordCol() {
      return xCoordCol;
    }

    public String getYCoordCol() {
      return yCoordCol;
    }

    public String getFloorCol() {
      return floorCol;
    }

    public String getBuildingCol() {
      return buildingCol;
    }

    public String getNodeTypeCol() {
      return nodeTypeCol;
    }

    public String getLongNameCol() {
      return longNameCol;
    }

    public String getShortNameCol() {
      return shortNameCol;
    }

    public String getTeamAssignedCol() {
      return teamAssignedCol;
    }

    public NodeRow(
        String nodeIdCol,
        String xCoordCol,
        String yCoordCol,
        String floorCol,
        String buildingCol,
        String nodeTypeCol,
        String longNameCol,
        String shortNameCol,
        String teamAssignedCol) {
      this.nodeIdCol = nodeIdCol;
      this.xCoordCol = xCoordCol;
      this.yCoordCol = yCoordCol;
      this.floorCol = floorCol;
      this.buildingCol = buildingCol;
      this.nodeTypeCol = nodeTypeCol;
      this.longNameCol = longNameCol;
      this.shortNameCol = shortNameCol;
      this.teamAssignedCol = teamAssignedCol;
    }
  }
}
