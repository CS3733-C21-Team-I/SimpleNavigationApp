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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;


public class NodeTableController {
  @FXML TextField Node_ID, Xcod, Ycod, Floor, Building, Type, Long, Short, Team;
  @FXML Button Add;
  @FXML TableView tableView;

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

  private static FullMapEditManager ourManager;
  private FullMapEditManager mapManager;

  public NodeTableController() {
    this.mapManager = ourManager;
  }

  public NodeTableController(FullMapEditManager fullMapEditManager) {
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
//    setup();
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
                                NodeRow row = (NodeRow) event.getRowValue();
                                System.out.println(row.getNodeIdCol());
                                row = setValue(event);
                                FullLocationNode newNode =
                                        new FullLocationNode(
                                                row.nodeIdCol,
                                                Integer.parseInt(row.xCoordCol),
                                                Integer.parseInt(row.yCoordCol),
                                                row.floorCol,
                                                row.buildingCol,
                                                row.nodeTypeCol,
                                                row.longNameCol,
                                                row.shortNameCol,
                                                row.teamAssignedCol,
                                                new HashSet());
                                System.out.println(row.getNodeIdCol());
                                System.out.println(newNode);
                                try {
                                  mapManager.editNode(row.getNodeIdCol(), newNode);
                                } catch (IOException e) {
                                  e.printStackTrace();
                                }
                                addNodesToTable();
                              });
      tableView.getColumns().add(columns.get(i));
    }
    TableColumn deleteBtnCol = new TableColumn(" ");
    deleteBtnCol.setCellValueFactory(new PropertyValueFactory<>("deleteBtnCol"));
    Callback<TableColumn<NodeRow, String>, TableCell<NodeRow, String>> cellFactory =
            new Callback<TableColumn<NodeRow, String>, TableCell<NodeRow, String>>() {
              @Override
              public TableCell<NodeRow, String> call(TableColumn<NodeRow, String> param) {
                final TableCell<NodeRow, String> cell = new TableCell<NodeRow, String>(){
                  final Button btn = new Button("Del");
                  @Override
                  public void updateItem(String item, boolean empty){
                    super.updateItem(item, empty);
                    if (empty){
                      setGraphic(null);
                    } else {
                      btn.setOnAction(event -> {
                        NodeRow nr = getTableView().getItems().get(getIndex());
                        System.out.println("ID of the node is: " + nr.getNodeIdCol());
                        mapManager.deleteNode(nr.getNodeIdCol());
//                        setup();
                      });
                      setGraphic(btn);
                    }
                    setText(null);
                  }
                };
                return cell;
              }
            };
    deleteBtnCol.setCellFactory(cellFactory);
    tableView.getColumns().add(deleteBtnCol);
  }

  private NodeRow setValue(CellEditEvent event) {
    int headerIndex = headers.indexOf(event.getTableColumn().getText());
    String value = (String) event.getNewValue();
    NodeRow row = (NodeRow) event.getRowValue();
    if (headerIndex == 0) {
      row.xCoordCol = value;
    } else if (headerIndex == 1) {
      row.yCoordCol = value;
    } else if (headerIndex == 2) {
      row.floorCol = value;
    } else if (headerIndex == 3) {
      row.buildingCol = value;
    } else if (headerIndex == 4) {
      row.nodeTypeCol = value;
    } else if (headerIndex == 5) {
      row.longNameCol = value;
    } else if (headerIndex == 6) {
      row.shortNameCol = value;
    } else if (headerIndex == 7) {
      row.teamAssignedCol = value;
    }
    return row;
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
