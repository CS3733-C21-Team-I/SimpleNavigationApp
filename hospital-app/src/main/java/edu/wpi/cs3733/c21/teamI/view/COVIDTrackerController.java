package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.CovidTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class COVIDTrackerController {

  @FXML private Label soreThroat, breath, taste, fever, congestion, cough, nausea;
  @FXML private Label diarrhea, headache, positiveContact, sympContact, testResult;
  @FXML private Button risk, notRisk, arrivedBtn, markDenied, markCleared;
  CovidTicket covidTicket;
  @FXML Label finish;
  @FXML HBox form;
  @FXML Label noForms;
  @FXML JFXTreeTableView<User> treeView;

  ObservableList<User> userList;

  private void color(boolean test, Label label) {
    if (test) {
      label.setStyle("-fx-background-color: #e34b4b");
    } else {
      label.setStyle("-fx-background-color: #4ccf70");
    }
  }

  @FXML
  public void initialize() {
    covidTicket =
        (CovidTicket)
            ServiceTicketDatabaseManager.getInstance().getServiceTicketDB().stream()
                .filter(
                    t ->
                        t.getTicketType() == ServiceTicket.TicketType.COVID
                            && t.getAssignedUserID()
                                .contains(
                                    ApplicationDataController.getInstance()
                                        .getLoggedInUser()
                                        .getUserId())
                            && !t.getCompleted().getValue())
                .findFirst()
                .orElse(null);
    form.setVisible(covidTicket != null);
    noForms.setVisible(covidTicket == null);
    if (covidTicket != null) {
      color(covidTicket.isSoreThroat(), soreThroat);
      color(covidTicket.isBreathing(), breath);
      color(covidTicket.isTasteSmellLoss(), taste);
      color(covidTicket.isFever(), fever);
      color(covidTicket.isCongestion(), congestion);
      color(covidTicket.isCough(), cough);
      color(covidTicket.isNausea(), nausea);
      color(covidTicket.isDiarrhea(), diarrhea);
      color(covidTicket.isHeadache(), headache);
      color(covidTicket.isContactCovidConfirmed(), positiveContact);
      color(covidTicket.isContactCovidSymptoms(), sympContact);
      color(covidTicket.isCovidTest(), testResult);
    }

    form.setVisible(true); // TODO remove this line

    Callback<User, javafx.beans.Observable[]> extractor =
        new Callback<User, javafx.beans.Observable[]>() {
          @Override
          public javafx.beans.Observable[] call(User u) {
            return new Observable[] {u.getRiskString()};
          }
        };

    ArrayList<JFXTreeTableColumn> columns = new ArrayList();
    JFXTreeTableColumn<User, String> nameCol = new JFXTreeTableColumn<>("Name");
    nameCol.setPrefWidth(200);
    nameCol.setEditable(true);
    nameCol.setCellValueFactory(
        param -> {
          return new SimpleStringProperty(param.getValue().getValue().getName());
        });

    JFXTreeTableColumn<User, String> riskCol = new JFXTreeTableColumn<>("COVID Status");
    riskCol.setPrefWidth(200);
    riskCol.setEditable(true);
    riskCol.setCellValueFactory(
        param -> {
          switch (param.getValue().getValue().getCovidRisk()) {
            case COVID_RISK:
              return new SimpleStringProperty("RISK");
            case NO_COVID_RISK:
              return new SimpleStringProperty("CLEARED");
            default:
              return new SimpleStringProperty("PENDING");
          }
        });

    JFXTreeTableColumn<User, String> entranceCol = new JFXTreeTableColumn<>("Permitted Entrance");
    entranceCol.setPrefWidth(200);
    entranceCol.setEditable(true);
    entranceCol.setCellValueFactory(
        param -> {
          switch (param.getValue().getValue().getCovidRisk()) {
            case COVID_RISK:
              return new SimpleStringProperty("Emergency Entrance");
            case NO_COVID_RISK:
              return new SimpleStringProperty("Atrium Main Lobby");
            default:
              return new SimpleStringProperty("Undetermined");
          }
        });
    JFXTreeTableColumn<User, String> arrivedCol = new JFXTreeTableColumn<>("Arrived");
    arrivedCol.setPrefWidth(200);

    userList = FXCollections.observableArrayList(extractor);
    updateTable();

    final TreeItem<User> root =
        new RecursiveTreeItem<User>(userList, RecursiveTreeObject::getChildren);

    treeView.setRoot(root);
    treeView.setShowRoot(false);

    // add cols to table
    treeView.getColumns().setAll(nameCol, riskCol, entranceCol, arrivedCol);
  }

  public void submit(ActionEvent actionEvent) {
    UserDatabaseManager.getInstance()
        .updateCovidRiskForUser(
            covidTicket.getRequestingUserID(),
            (actionEvent.getSource() == risk)
                ? User.CovidRisk.COVID_RISK
                : User.CovidRisk.NO_COVID_RISK);
    System.out.println(
        UserDatabaseManager.getInstance().getCovidRiskForUser(covidTicket.getRequestingUserID()));
    ServiceTicketDatabaseManager.getInstance().updateTicket(covidTicket.getTicketId());
    risk.setDisable(true);
    notRisk.setDisable(true);
    finish.setVisible(true);
  }

  public void markArrived() {
    // TODO System.out.println("this is selected: " +
    // treeView.getSelectionModel().getSelectedItem().getValue().----setArrived function?---);
  }

  public void markDenied() {
    User user = treeView.getSelectionModel().getSelectedItem().getValue();
    if (user != null) {
      int id = user.getUserId();
      UserDatabaseManager.getInstance().updateCovidRiskForUser(id, User.CovidRisk.COVID_RISK);
      updateTable();
    } else {
      System.out.println("no user selected");
    }
  }

  public void markCleared() {
    User user = treeView.getSelectionModel().getSelectedItem().getValue();
    if (user != null) {
      int id = user.getUserId();
      UserDatabaseManager.getInstance().updateCovidRiskForUser(id, User.CovidRisk.NO_COVID_RISK);
      updateTable();
    } else {
      System.out.println("no user selected");
    }
  }

  public void updateTable() {
    userList.clear();
    List<String> userNames =
        UserDatabaseManager.getInstance()
            .getUsernamesWithPermission(User.Permission.SUBMIT_COVD_TICKET);

    for (String name : userNames) {
      userList.add(UserDatabaseManager.getInstance().getUserForScreenname(name));
    }
  }
}
