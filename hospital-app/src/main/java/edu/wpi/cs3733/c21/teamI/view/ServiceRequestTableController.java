package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.util.Callback;

public class ServiceRequestTableController implements Initializable {

  @FXML private JFXTreeTableView<ServiceTicket> treeView;

  @FXML private JFXTextField input;

  @FXML private JFXTextField IDTextField;

  ServiceTableIntermediateController interController;

  ObservableList<ServiceTicket> ticketList;
  FilteredList<ServiceTicket> filteredList;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    interController = new ServiceTableIntermediateController();

    Callback<ServiceTicket, Observable[]> extractor =
        new Callback<ServiceTicket, Observable[]>() {

          @Override
          public Observable[] call(ServiceTicket p) {
            return new Observable[] {p.getCompleted(), p.assignedUserIDProperty()};
          }
        };

    ticketList = FXCollections.observableArrayList(extractor);
    ticketList.addAll(ServiceTicketDatabaseManager.getInstance().getServiceTicketDB());
    filteredList = new FilteredList<>(ticketList);

    input
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              System.out.println(newValue + ": " + newValue.length());
              treeView.setPredicate(
                  ServiceTicketTreeItem -> {
                    Boolean flag =
                        ServiceTicketTreeItem.getValue()
                            .getTicketType()
                            .toString()
                            .contains(newValue.toUpperCase());

                    return flag;
                  });
              filteredList.setPredicate(
                  ServiceTicket -> {
                    Boolean flag =
                        ServiceTicket.getTicketType().toString().contains(newValue.toUpperCase());

                    return flag;
                  });
              update();
            });

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
          StringBuilder listString = new StringBuilder();
          for (Integer s : param.getValue().getValue().getAssignedUserID()) {
            listString.append(s).append(", ");
          }
          return new SimpleStringProperty(
              listString.toString().substring(0, listString.toString().length() - 2));
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

    final TreeItem<ServiceTicket> root =
        new RecursiveTreeItem<ServiceTicket>(ticketList, RecursiveTreeObject::getChildren);
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

    treeView.setRoot(root);
    treeView.setShowRoot(false);

    treeView
        .getRoot()
        .getChildren()
        .addListener(
            new ListChangeListener<TreeItem<ServiceTicket>>() {
              @Override
              public void onChanged(Change<? extends TreeItem<ServiceTicket>> c) {
                System.out.println("Ticket list changed");
              }
            });

    update();
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
    ticketList.clear();
    ticketList.addAll(ServiceTicketDatabaseManager.getInstance().getServiceTicketDB());

    System.out.println(treeView.getRoot().getChildren().size());

    if (treeView.getRoot() != null) {
      boolean allMatch =
          filteredList.stream().map(ServiceTicket::getTicketType).distinct().count() == 1;

      for (TreeItem<ServiceTicket> t : treeView.getRoot().getChildren()) {
        System.out.println(t.getValue().getTicketType());
      }

      System.out.println(allMatch);

      if (allMatch) {
        if (treeView.getColumns().size() == 7) {
          treeView
              .getColumns()
              .addAll(UniqueColumnFactory.getColumns(treeView, filteredList.get(0)));
        }
      } else {
        if (treeView.getColumns().size() != 7) {
          System.out.println("Trimming back columns");
          treeView.getColumns().remove(7, treeView.getColumns().size());
        }
      }
    }
  }

  @FXML
  public void onFilter() {
    // update();
  }
}
