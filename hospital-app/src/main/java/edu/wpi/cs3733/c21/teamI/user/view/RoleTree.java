package edu.wpi.cs3733.c21.teamI.user.view;

import com.jfoenix.controls.JFXListCell;
import edu.wpi.cs3733.c21.teamI.user.Employee;
import edu.wpi.cs3733.c21.teamI.user.EmployeeDataController;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.io.IOException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class RoleTree extends JFXListCell<User.Role> {

  @FXML private VBox rolePane;

  @FXML private Label roleLabel;

  @FXML private TreeTableView employeeTable;

  private FXMLLoader mLLoader;

  private void init() {
    employeeTable
        .prefHeightProperty()
        .bind(employeeTable.expandedItemCountProperty().multiply(30).add(35));

    TreeTableColumn<Employee, String> idCol = new TreeTableColumn<Employee, String>("Screen Name");
    idCol.setPrefWidth(160);
    idCol.setEditable(false);
    idCol.setCellValueFactory(
        new Callback<
            TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<Employee, String> p) {
            Employee f = p.getValue().getValue();
            String text = f.getScreenName();
            return new ReadOnlyObjectWrapper<String>(text);
          }
        });
    idCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());

    TreeTableColumn<Employee, String> fNameCol =
        new TreeTableColumn<Employee, String>("First Name");
    fNameCol.setPrefWidth(160);
    fNameCol.setEditable(true);
    fNameCol.setCellValueFactory(
        new Callback<
            TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<Employee, String> p) {
            Employee f = p.getValue().getValue();
            String text = f.getFirstName();
            return new ReadOnlyObjectWrapper<String>(text);
          }
        });
    fNameCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
    fNameCol.setOnEditCommit(
        new EventHandler<TreeTableColumn.CellEditEvent<Employee, String>>() {
          @Override
          public void handle(TreeTableColumn.CellEditEvent<Employee, String> event) {
            Employee selectedEmp = event.getRowValue().getValue();
            // TODO
            EmployeeDataController.getInstance().resetEmployees();
          }
        });

    TreeTableColumn<Employee, String> lNameCol = new TreeTableColumn<Employee, String>("Last Name");
    lNameCol.setPrefWidth(160);
    lNameCol.setEditable(true);
    lNameCol.setCellValueFactory(
        new Callback<
            TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<Employee, String> p) {
            Employee f = p.getValue().getValue();
            String text = f.getLastName();
            return new ReadOnlyObjectWrapper<String>(text);
          }
        });
    lNameCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
    lNameCol.setOnEditCommit(
        new EventHandler<TreeTableColumn.CellEditEvent<Employee, String>>() {
          @Override
          public void handle(TreeTableColumn.CellEditEvent<Employee, String> event) {
            Employee selectedEmp = event.getRowValue().getValue();
            // TODO
            EmployeeDataController.getInstance().resetEmployees();
          }
        });

    TreeTableColumn<Employee, String> genderCol = new TreeTableColumn<Employee, String>("Gender");
    genderCol.setPrefWidth(160);
    genderCol.setEditable(true);
    genderCol.setCellValueFactory(
        new Callback<
            TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<Employee, String> p) {
            Employee f = p.getValue().getValue();
            String text = f.getGender().toString();
            return new ReadOnlyObjectWrapper<String>(text);
          }
        });
    genderCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
    genderCol.setOnEditCommit(
        new EventHandler<TreeTableColumn.CellEditEvent<Employee, String>>() {
          @Override
          public void handle(TreeTableColumn.CellEditEvent<Employee, String> event) {
            Employee selectedEmp = event.getRowValue().getValue();
            // TODO
            EmployeeDataController.getInstance().resetEmployees();
          }
        });

    TreeTableColumn<Employee, String> posCol = new TreeTableColumn<Employee, String>("Roles");
    posCol.setPrefWidth(160);
    posCol.setCellValueFactory(
        new Callback<
            TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<Employee, String> p) {
            Employee f = p.getValue().getValue();
            StringBuilder text = new StringBuilder();
            for (User.Role role : f.userRoles) {
              text.append(role).append(", ");
            }
            return new ReadOnlyObjectWrapper<String>(text.toString());
          }
        });
    posCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
    posCol.setOnEditCommit(
        new EventHandler<TreeTableColumn.CellEditEvent<Employee, String>>() {
          @Override
          public void handle(TreeTableColumn.CellEditEvent<Employee, String> event) {
            Employee selectedEmp = event.getRowValue().getValue();
            // TODO
            EmployeeDataController.getInstance().resetEmployees();
          }
        });

    employeeTable.getColumns().addAll(idCol, fNameCol, lNameCol, genderCol, posCol);

    employeeTable.setRoot(new TreeItem());
    employeeTable.setShowRoot(false);
  }

  @FXML
  @Override
  protected void updateItem(User.Role item, boolean empty) {
    super.updateItem(item, empty);

    if (empty) {
      setText(null);
      setGraphic(null);
      setContentDisplay(ContentDisplay.TEXT_ONLY);

    } else {

      if (mLLoader == null) {
        System.out.println("Loading");
        mLLoader = new FXMLLoader(getClass().getResource("/fxml/menuFiles/RoleTable.fxml"));
        mLLoader.setController(this);

        try {
          mLLoader.load();
        } catch (IOException e) {
          e.printStackTrace();
        }

        init();
      }

      roleLabel.setText(item.toString());

      employeeTable.getRoot().getChildren().clear();
      for (Employee employee : EmployeeDataController.getInstance().getEmployeesWithRole(item)) {
        employeeTable.getRoot().getChildren().add(new TreeItem<>(employee));
      }

      setGraphic(rolePane);
      setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }
  }
}
