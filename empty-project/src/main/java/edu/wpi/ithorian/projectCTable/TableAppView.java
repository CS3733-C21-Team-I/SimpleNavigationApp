package edu.wpi.ithorian.projectCTable;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class TableAppView extends Application {
  @FXML TableView<TableController.NodeRow> tableView;

  @FXML
  TableColumn<TableController.NodeRow, String> nodeIdCol,
      xCoordCol,
      yCoordCol,
      floorCol,
      buildingCol,
      nodeTypeCol,
      longNameCol,
      shortNameCol,
      teamAssigned;

  @Override
  public void init() {
    System.out.println("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/fxml/NodeTable.fxml"));
    tableView.setEditable(true);
    primaryStage.setTitle("Table View");
    primaryStage.setScene(new Scene(root, 973, 800));
    primaryStage.show();

    nodeIdCol.setCellValueFactory(
        new PropertyValueFactory<TableController.NodeRow, String>("nodeIdCol"));
    xCoordCol.setCellValueFactory(
        new PropertyValueFactory<TableController.NodeRow, String>("xCoordCol"));
    yCoordCol.setCellValueFactory(
        new PropertyValueFactory<TableController.NodeRow, String>("yCoordCol"));
    floorCol.setCellValueFactory(
        new PropertyValueFactory<TableController.NodeRow, String>("floorCol"));
    buildingCol.setCellValueFactory(
        new PropertyValueFactory<TableController.NodeRow, String>("buildingCol"));
    nodeTypeCol.setCellValueFactory(
        new PropertyValueFactory<TableController.NodeRow, String>("nodeTypeCol"));
    longNameCol.setCellValueFactory(
        new PropertyValueFactory<TableController.NodeRow, String>("longNameCol"));
    shortNameCol.setCellValueFactory(
        new PropertyValueFactory<TableController.NodeRow, String>("shortNameCol"));
    teamAssigned.setCellValueFactory(
        new PropertyValueFactory<TableController.NodeRow, String>("teamAssigned"));
  }

  @Override
  public void stop() {
    System.out.println("Shutting Down");
  }

  public void addNodeRow(TableController.NodeRow nodeRow) {
    tableView.getItems().add(nodeRow);
  }
}
