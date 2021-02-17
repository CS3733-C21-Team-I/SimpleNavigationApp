package edu.wpi.ithorian.projectCTable;

import edu.wpi.ithorian.hospitalMap.tableEditing.FullLocationNode;
import edu.wpi.ithorian.hospitalMap.tableEditing.FullMapEditManager;
import edu.wpi.ithorian.ReadCSV;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import edu.wpi.ithorian.hospitalMap.HospitalMapNode;
import edu.wpi.ithorian.hospitalMap.LocationNode;
import edu.wpi.ithorian.hospitalMap.mapEditing.MapEditManager;
import java.io.*;
import java.util.HashSet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class TableController {
  @FXML TextField Node_ID, Xcod, Ycod, Floor, Building, Type, Long, Short, Team;
  @FXML Button Add;

  private static FullMapEditManager ourManager;
  private FullMapEditManager mapManager;

  public TableController(FullMapEditManager fullMapEditManager) {
    this.mapManager = fullMapEditManager;
  }

  public TableController() {
    this.mapManager = ourManager;
  }

  public static void saveManager() {
    ourManager = new FullMapEditManager().getInstance();
  }

  @FXML
  public void onAddAction() throws IOException {
    System.out.println(mapManager.getEntityNodes());
    System.out.println("Map manager: " + mapManager);
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
            new HashSet<FullLocationNode>());
    mapManager.addNode(newNode);
    System.out.println(mapManager.getEntityNodes());
  }

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
    xCoordCol.setCellFactory(TextFieldTableCell.forTableColumn());
    xCoordCol.setCellValueFactory(new PropertyValueFactory<NodeRow, String>("xCoordCol"));
    xCoordCol.setOnEditCommit(
        new EventHandler<CellEditEvent>() {
          @Override
          public void handle(CellEditEvent event) {
            NodeRow row =
                (NodeRow) event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setXCoordCol((String) event.getNewValue());
          }
        });
    tableView.getColumns().add(xCoordCol);

    TableColumn yCoordCol = new TableColumn("y coord");
    yCoordCol.setCellFactory(TextFieldTableCell.forTableColumn());
    yCoordCol.setOnEditCommit(
        new EventHandler<CellEditEvent>() {
          @Override
          public void handle(CellEditEvent event) {
            NodeRow row =
                (NodeRow) event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setYCoordCol((String) event.getNewValue());
          }
        });
    yCoordCol.setCellValueFactory(new PropertyValueFactory<NodeRow, String>("yCoordCol"));
    tableView.getColumns().add(yCoordCol);

    TableColumn floorCol = new TableColumn("floor");
    floorCol.setCellFactory(TextFieldTableCell.forTableColumn());
    floorCol.setOnEditCommit(
        new EventHandler<CellEditEvent>() {
          @Override
          public void handle(CellEditEvent event) {
            NodeRow row =
                (NodeRow) event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setFloorCol((String) event.getNewValue());
          }
        });
    floorCol.setCellValueFactory(new PropertyValueFactory<NodeRow, String>("floorCol"));
    tableView.getColumns().add(floorCol);

    TableColumn buildingCol = new TableColumn("building");
    buildingCol.setCellFactory(TextFieldTableCell.forTableColumn());
    buildingCol.setOnEditCommit(
        new EventHandler<CellEditEvent>() {
          @Override
          public void handle(CellEditEvent event) {
            NodeRow row =
                (NodeRow) event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setBuildingCol((String) event.getNewValue());
          }
        });
    buildingCol.setCellValueFactory(new PropertyValueFactory<NodeRow, String>("buildingCol"));
    tableView.getColumns().add(buildingCol);

    TableColumn nodeTypeCol = new TableColumn("node type");
    nodeTypeCol.setCellFactory(TextFieldTableCell.forTableColumn());
    nodeTypeCol.setOnEditCommit(
        new EventHandler<CellEditEvent>() {
          @Override
          public void handle(CellEditEvent event) {
            NodeRow row =
                (NodeRow) event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setNodeTypeCol((String) event.getNewValue());
          }
        });
    nodeTypeCol.setCellValueFactory(new PropertyValueFactory<NodeRow, String>("nodeTypeCol"));
    tableView.getColumns().add(nodeTypeCol);

    TableColumn longNameCol = new TableColumn("long name");
    longNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
    longNameCol.setOnEditCommit(
        new EventHandler<CellEditEvent>() {
          @Override
          public void handle(CellEditEvent event) {
            NodeRow row =
                (NodeRow) event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setLongNameCol((String) event.getNewValue());
          }
        });
    longNameCol.setCellValueFactory(new PropertyValueFactory<NodeRow, String>("longNameCol"));
    tableView.getColumns().add(longNameCol);

    TableColumn shortNameCol = new TableColumn("short name");
    shortNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
    shortNameCol.setOnEditCommit(
        new EventHandler<CellEditEvent>() {
          @Override
          public void handle(CellEditEvent event) {
            NodeRow row =
                (NodeRow) event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setShortNameCol((String) event.getNewValue());
          }
        });
    shortNameCol.setCellValueFactory(new PropertyValueFactory<NodeRow, String>("shortNameCol"));
    tableView.getColumns().add(shortNameCol);

    TableColumn teamAssignedCol = new TableColumn("team assigned");
    teamAssignedCol.setCellFactory(TextFieldTableCell.forTableColumn());
    teamAssignedCol.setOnEditCommit(
        new EventHandler<CellEditEvent>() {
          @Override
          public void handle(CellEditEvent event) {
            NodeRow row =
                (NodeRow) event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setTeamAssignedCol((String) event.getNewValue());
          }
        });
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

    public void setXCoordCol(String xCoordCol) {
      this.xCoordCol = xCoordCol;
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

    public void setYCoordCol(String yCoordCol) {
      this.yCoordCol = yCoordCol;
    }

    public void setFloorCol(String floorCol) {
      this.floorCol = floorCol;
    }

    public void setBuildingCol(String buildingCol) {
      this.buildingCol = buildingCol;
    }

    public void setNodeTypeCol(String nodeTypeCol) {
      this.nodeTypeCol = nodeTypeCol;
    }

    public void setLongNameCol(String longNameCol) {
      this.longNameCol = longNameCol;
    }

    public void setShortNameCol(String shortNameCol) {
      this.shortNameCol = shortNameCol;
    }

    public void setTeamAssignedCol(String teamAssignedCol) {
      this.teamAssignedCol = teamAssignedCol;
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
