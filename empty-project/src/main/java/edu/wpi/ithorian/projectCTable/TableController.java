package edu.wpi.ithorian.projectCTable;

import edu.wpi.ithorian.ReadCSV;
import edu.wpi.ithorian.hospitalMap.tableEditing.FullLocationNode;
import edu.wpi.ithorian.hospitalMap.tableEditing.FullMapEditManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class TableController {
  @FXML TextField Node_ID, Xcod, Ycod, Floor, Building, Type, Long, Short, Team;
  @FXML Button Add;
  @FXML TableView tableView;

  private static FullMapEditManager ourManager;
  private FullMapEditManager mapManager;

  public TableController() {
    this.mapManager = ourManager;
  }

  public TableController(FullMapEditManager fullMapEditManager) {
    this.mapManager = fullMapEditManager;
  }

  public static void saveManager() {
    ourManager = new FullMapEditManager().getInstance();
  }

  @FXML
  public void onAddAction() throws IOException {
    String nID = Node_ID.getText();
    String lName = Long.getText();
    String sName = Short.getText();
    String tName = Team.getText();
    String xCord = Xcod.getText();
    String yCord = Ycod.getText();
    String fName = Floor.getText();
    String bName = Building.getText();
    String type = Type.getText();

    FullLocationNode newNode =
        new FullLocationNode(
            nID,
            Integer.parseInt(xCord),
            Integer.parseInt(yCord),
            fName,
            bName,
            type,
            sName,
            lName,
            tName,
            new HashSet<>());
    mapManager.addNode(newNode);
    addNodesToTable();
  }

  public static void init() {};

  private ObservableList<NodeRow> rows = FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    addNodesToTable();
    setup();
  }

  @FXML
  public void addNodesToTable() {
    List<List<String>> tableNodes = ReadCSV.readFromFile("node.csv");
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
    tableView.getItems().addAll(rows);
  }

  @FXML
  public void setup() {
    tableView.setEditable(true);

    TableColumn nodeIdCol = new TableColumn("Node ID");
    nodeIdCol.setCellValueFactory(new PropertyValueFactory<NodeRow, String>("nodeIdCol"));
    tableView.getColumns().add(nodeIdCol);

    ArrayList<String> headers =
        new ArrayList<>(
            Arrays.asList(
                "X Coord",
                "Y Coord",
                "Floor",
                "Building",
                "Node Type",
                "Long Name",
                "Short Name",
                "Team Assigned"));
    ArrayList<String> columnIDs =
        new ArrayList<>(
            Arrays.asList(
                "xCoordCol",
                "yCoordCol",
                "floorCol",
                "buildingCol",
                "nodeTypeCol",
                "longNameCol",
                "shortNameCol",
                "teamAssignedCol"));
    ArrayList<TableColumn> columns = new ArrayList<TableColumn>();

    for (int i = 0; i < headers.size(); i++) {
      columns.add(new TableColumn(headers.get(i)));
      columns.get(i).setCellFactory(TextFieldTableCell.forTableColumn());
      columns
          .get(i)
          .setCellValueFactory(new PropertyValueFactory<NodeRow, String>(columnIDs.get(i)));
      columns
          .get(i)
          .setOnEditCommit(
              (EventHandler<CellEditEvent>)
                  event -> {
                    System.out.println(event.getNewValue());
                  });
      tableView.getColumns().add(columns.get(i));
    }
  }

  public static class NodeRow {
    public String nodeIdCol;
    public String xCoordCol;
    public String yCoordCol;
    public String floorCol;
    public String buildingCol;
    public String nodeTypeCol;
    public String longNameCol;
    public String shortNameCol;
    public String teamAssignedCol;

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
