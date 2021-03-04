package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;

public class ServiceRequestTableController implements Initializable {

  @FXML private FlowPane main;
  @FXML private JFXTreeTableView<ServiceTicket> treeView;

  @FXML private JFXTextField input;

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    treeView.setEditable(true);

    JFXTreeTableColumn<ServiceTicket, String> requestTypeCol =
        new JFXTreeTableColumn<>("Request Type");
    requestTypeCol.setPrefWidth(150);
    requestTypeCol.setEditable(true);
    requestTypeCol.setCellValueFactory(
        new Callback<
            TreeTableColumn.CellDataFeatures<ServiceTicket, String>, ObservableValue<String>>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<ServiceTicket, String> param) {
            return new SimpleStringProperty(param.getValue().getValue().getTicketType().toString());
          }
        });

    JFXTreeTableColumn<ServiceTicket, String> requestIDCol =
        new JFXTreeTableColumn<>("Requester ID");
    requestIDCol.setPrefWidth(150);
    requestIDCol.setEditable(true);
    requestIDCol.setCellValueFactory(
        new Callback<
            TreeTableColumn.CellDataFeatures<ServiceTicket, String>, ObservableValue<String>>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<ServiceTicket, String> param) {
            return new SimpleStringProperty(
                Integer.toString(param.getValue().getValue().getRequestingUserID()));
          }
        });

    JFXTreeTableColumn<ServiceTicket, String> assignIDCol = new JFXTreeTableColumn<>("Assigned ID");
    assignIDCol.setPrefWidth(150);
    assignIDCol.setEditable(true);
    assignIDCol.setCellValueFactory(
        new Callback<
            TreeTableColumn.CellDataFeatures<ServiceTicket, String>, ObservableValue<String>>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<ServiceTicket, String> param) {
            System.out.println(param.getValue().getValue().getAssignedUserID().get(0));
            return new SimpleStringProperty(
                Integer.toString(param.getValue().getValue().getAssignedUserID().get(0)));
          }
        });

    JFXTreeTableColumn<ServiceTicket, String> locationCol = new JFXTreeTableColumn<>("Location");
    locationCol.setPrefWidth(150);
    locationCol.setEditable(true);
    locationCol.setCellValueFactory(
        new Callback<
            TreeTableColumn.CellDataFeatures<ServiceTicket, String>, ObservableValue<String>>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<ServiceTicket, String> param) {
            return new SimpleStringProperty(param.getValue().getValue().getLocation());
          }
        });

    JFXTreeTableColumn<ServiceTicket, String> detailsCol = new JFXTreeTableColumn<>("Details");
    detailsCol.setPrefWidth(150);
    detailsCol.setEditable(true);
    detailsCol.setCellValueFactory(
        new Callback<
            TreeTableColumn.CellDataFeatures<ServiceTicket, String>, ObservableValue<String>>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<ServiceTicket, String> param) {
            return new SimpleStringProperty(param.getValue().getValue().getDescription());
          }
        });

    JFXTreeTableColumn<ServiceTicket, String> completeCol = new JFXTreeTableColumn<>("Complete");
    completeCol.setPrefWidth(150);
    completeCol.setEditable(true);
    completeCol.setCellValueFactory(
        new Callback<
            TreeTableColumn.CellDataFeatures<ServiceTicket, String>, ObservableValue<String>>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<ServiceTicket, String> param) {
            return new SimpleStringProperty(
                Boolean.toString(param.getValue().getValue().isCompleted()));
          }
        });

    ObservableList<ServiceTicket> serviceTickets = FXCollections.observableArrayList();
    serviceTickets.addAll(ServiceTicketDatabaseManager.getInstance().getServiceTicketDB());
    System.out.println(serviceTickets);
    System.out.println("yolo");

    final TreeItem<ServiceTicket> root =
        new RecursiveTreeItem<ServiceTicket>(serviceTickets, RecursiveTreeObject::getChildren);
    treeView
        .getColumns()
        .setAll(requestTypeCol, requestIDCol, assignIDCol, locationCol, detailsCol, completeCol);
    treeView.setRoot(root);
    treeView.setShowRoot(false);

    input
        .textProperty()
        .addListener(
            new ChangeListener<String>() {
              @Override
              public void changed(
                  ObservableValue<? extends String> observable, String oldValue, String newValue) {
                treeView.setPredicate(
                    new Predicate<TreeItem<ServiceTicket>>() {
                      @Override
                      public boolean test(TreeItem<ServiceTicket> ServiceTicketTreeItem) {
                        Boolean flag =
                            ServiceTicketTreeItem.getValue()
                                .getTicketType()
                                .toString()
                                .contains(newValue.toUpperCase());
                        System.out.println(treeView.getSelectionModel().getSelectedItem());
                        return flag;
                      }
                    });
              }
            });
  }
}
