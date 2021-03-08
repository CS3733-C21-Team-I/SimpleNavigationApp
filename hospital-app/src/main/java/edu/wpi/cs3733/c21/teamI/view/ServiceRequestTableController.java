package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;

public class ServiceRequestTableController implements Initializable {

  @FXML private JFXTreeTableView<ServiceTicket> treeView;

  @FXML private JFXTextField input;

  @FXML private JFXTextField IDTextField;

  ServiceTableIntermediateController interController;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    interController = new ServiceTableIntermediateController();
    update();
    input
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) ->
                treeView.setPredicate(
                    ServiceTicketTreeItem -> {
                      Boolean flag =
                          ServiceTicketTreeItem.getValue()
                              .getTicketType()
                              .toString()
                              .contains(newValue.toUpperCase());
                      return flag;
                    }));
  }

  public void markComplete() {
    System.out.println(treeView.getSelectionModel().getSelectedItem().getValue().getTicketId());
    //
    interController.markCompleted(
        treeView.getSelectionModel().getSelectedItem().getValue().getTicketId());
    update();
  }

  public void addAssignedID() {
    interController.addAssignedID(
        treeView.getSelectionModel().getSelectedItem().getValue().getTicketId(),
        Integer.parseInt(IDTextField.getText()));
    update();
  }

  public void removeAssignedID() {
    interController.removeAssignedID(
        treeView.getSelectionModel().getSelectedItem().getValue().getTicketId(),
        Integer.parseInt(IDTextField.getText()));
    update();
  }

  public void update() {
    // treeView.setEditable(true);

    ArrayList<JFXTreeTableColumn> columns = new ArrayList();
    JFXTreeTableColumn<ServiceTicket, String> ticketIDCol = new JFXTreeTableColumn<>("Ticket ID");
    ticketIDCol.setPrefWidth(150);
    ticketIDCol.setEditable(true);
    ticketIDCol.setCellValueFactory(
        param ->
            new SimpleStringProperty(Integer.toString(param.getValue().getValue().getTicketId())));

    JFXTreeTableColumn<ServiceTicket, String> requestTypeCol =
        new JFXTreeTableColumn<>("Request Type");
    requestTypeCol.setPrefWidth(150);
    requestTypeCol.setEditable(true);
    requestTypeCol.setCellValueFactory(
        param -> new SimpleStringProperty(param.getValue().getValue().getTicketType().toString()));

    JFXTreeTableColumn<ServiceTicket, String> requestIDCol =
        new JFXTreeTableColumn<>("Requester ID");
    requestIDCol.setPrefWidth(150);
    requestIDCol.setEditable(true);
    requestIDCol.setCellValueFactory(
        param ->
            new SimpleStringProperty(
                Integer.toString(param.getValue().getValue().getRequestingUserID())));

    JFXTreeTableColumn<ServiceTicket, String> assignIDCol = new JFXTreeTableColumn<>("Assigned ID");
    assignIDCol.setPrefWidth(150);
    assignIDCol.setEditable(true);
    assignIDCol.setCellValueFactory(
        param -> {
          String listString = param.getValue().getValue().getAssignedUserID().toString();
          return new SimpleStringProperty(listString.substring(1, listString.length() - 1));
        });

    JFXTreeTableColumn<ServiceTicket, String> locationCol = new JFXTreeTableColumn<>("Location");
    locationCol.setPrefWidth(150);
    locationCol.setEditable(true);
    locationCol.setCellValueFactory(
        param -> new SimpleStringProperty(param.getValue().getValue().getLocation()));

    JFXTreeTableColumn<ServiceTicket, String> detailsCol = new JFXTreeTableColumn<>("Details");
    detailsCol.setPrefWidth(150);
    detailsCol.setEditable(true);
    detailsCol.setCellValueFactory(
        param -> new SimpleStringProperty(param.getValue().getValue().getDescription()));

    JFXTreeTableColumn<ServiceTicket, String> completeCol = new JFXTreeTableColumn<>("Complete");
    completeCol.setPrefWidth(150);
    completeCol.setEditable(true);
    completeCol.setCellValueFactory(
        param ->
            new SimpleStringProperty(Boolean.toString(param.getValue().getValue().isCompleted())));

    ObservableList<ServiceTicket> serviceTickets = FXCollections.observableArrayList();
    serviceTickets.addAll(ServiceTicketDatabaseManager.getInstance().getServiceTicketDB());

    final TreeItem<ServiceTicket> root =
        new RecursiveTreeItem<ServiceTicket>(serviceTickets, RecursiveTreeObject::getChildren);
    treeView
        .getColumns()
        .setAll(
            requestTypeCol,
            ticketIDCol,
            requestIDCol,
            assignIDCol,
            locationCol,
            detailsCol,
            completeCol);
    if (treeView.getRoot() != null) {
      boolean allMatch =
          treeView.getRoot().getChildren().stream()
                  .map(c -> c.getValue().getTicketType())
                  .distinct()
                  .count()
              <= 1;

      if (allMatch) {
        treeView
            .getColumns()
            .addAll(
                UniqueColumnFactory.getColumns(
                    treeView, treeView.getRoot().getChildren().get(0).getValue()));
      }
    }
    treeView.setRoot(root);
    treeView.setShowRoot(false);
  }

  @FXML
  public void onFilter() {
    update();
  }
}
