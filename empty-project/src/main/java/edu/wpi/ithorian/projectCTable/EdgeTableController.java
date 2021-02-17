package edu.wpi.ithorian.projectCTable;

import edu.wpi.ithorian.ReadCSV;
import edu.wpi.ithorian.hospitalMap.tableEditing.FullLocationNode;
import edu.wpi.ithorian.hospitalMap.tableEditing.FullMapEditManager;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class EdgeTableController {
  @FXML TextField ID, aID, bID;
  @FXML Button Add;
  @FXML TableView tableView;

  ArrayList<String> headers =
      new ArrayList<>(
          Arrays.asList("id", "startNode", "endNode"));

  private static FullMapEditManager ourManager;
  private FullMapEditManager mapManager;

  public EdgeTableController() {
    this.mapManager = ourManager;
  }

  public EdgeTableController(FullMapEditManager fullMapEditManager) {
    this.mapManager = fullMapEditManager;
  }

  public static void saveManager() {
    ourManager = new FullMapEditManager().getInstance();
  }

  @FXML
  public void onAddAction() throws IOException {
    String IDString = ID.getText();
    String aIDString = aID.getText();
    String bIDString = bID.getText();

    System.out.println(mapManager);
    mapManager.addConnection(aIDString, bIDString);
    addEdgesToTable();
  }

  public static void init() {};

  private ObservableList<EdgeRow> eRows = FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    addEdgesToTable();
    setup();
  }

  @FXML
  public void addEdgesToTable() {
    String path =
            System.getProperty("user.dir")
                    + "/empty-project/src/main/java/edu/wpi/ithorian/hospitalMap/mapEditing/";
    List<List<String>> tableEdges = ReadCSV.readFromFile(path + "MapIEdges.csv");
    for (List<String> tableEdge : tableEdges) {
      EdgeRow edgeRow =
              new EdgeRow(
                      tableEdge.get(0),
                      tableEdge.get(1),
                      tableEdge.get(2)
                      );
      eRows.add(edgeRow);
    }
    tableView.getItems().addAll(eRows);
  }

  @FXML
  public void setup() {
    tableView.setEditable(true);

    TableColumn edgeIdCol = new TableColumn("Edge ID");
    edgeIdCol.setCellValueFactory(new PropertyValueFactory<EdgeRow, String>("edgeIdCol"));
    tableView.getColumns().add(edgeIdCol);

    ArrayList<String> columnIDs =
        new ArrayList<>(
            Arrays.asList(
                "edgeIdCol",
                "startNodeCol",
                "endNodeCol"
                ));
    ArrayList<TableColumn> columns = new ArrayList<TableColumn>();

    for (int i = 0; i < headers.size(); i++) {
      columns.add(new TableColumn(headers.get(i)));
      columns.get(i).setCellFactory(TextFieldTableCell.forTableColumn());
      columns
          .get(i)
          .setCellValueFactory(new PropertyValueFactory<EdgeRow, String>(columnIDs.get(i)));
      columns
          .get(i)
          .setOnEditCommit(
              (EventHandler<CellEditEvent>)
                  event -> {
                    EdgeRow row = (EdgeRow) event.getRowValue();
                    String startNodeId = row.startNodeCol;
                    String endNodeId = row.endNodeCol;
                    int headerIndex = headers.indexOf(event.getTableColumn().getText());
                    String value = (String) event.getNewValue();
                    mapManager.removeConnection(startNodeId, endNodeId);
                    if (headerIndex == 1) {
                      startNodeId = value;
                    }
                    if (headerIndex == 2) {
                      endNodeId = value;
                    }
                    mapManager.addConnection(startNodeId, endNodeId);
                    addEdgesToTable();
                  });
      tableView.getColumns().add(columns.get(i));
    }
  }

  public static class EdgeRow {
    public String edgeIdCol;
    public String startNodeCol;
    public String endNodeCol;

    public String getEdgeIdCol() {
      return edgeIdCol;
    }

    public void setEdgeIdCol(String edgeIdCol) {
      this.edgeIdCol = edgeIdCol;
    }

    public String getStartNodeCol() {
      return startNodeCol;
    }

    public void setStartNodeCol(String startNodeCol) {
      this.startNodeCol = startNodeCol;
    }

    public String getEndNodeCol() {
      return endNodeCol;
    }

    public void setEndNodeCol(String endNodeCol) {
      this.endNodeCol = endNodeCol;
    }

    public EdgeRow(
            String edgeIdCol,
            String startNodeCol,
            String endNodeCol
            ) {
      this.edgeIdCol = edgeIdCol;
      this.startNodeCol = startNodeCol;
      this.endNodeCol = endNodeCol;
    }
  }
}
