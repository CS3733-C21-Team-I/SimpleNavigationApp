package edu.wpi.cs3733.c21.teamI.user.view;

import static edu.wpi.cs3733.c21.teamI.user.User.*;

import com.jfoenix.controls.JFXListView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EmployeeTableController extends Application {
  @FXML private JFXListView roleList;

  @FXML
  private void initialize() {
    ObservableList<Role> roles = FXCollections.observableArrayList(Role.values());

    roleList.setItems(roles);
    roleList.setCellFactory(new RoleTreeFactory());
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    AnchorPane root = FXMLLoader.load(getClass().getResource("/fxml/menuFiles/EmployeeTable.fxml"));
    primaryStage.setTitle("Hospital App");
    Scene applicationScene = new Scene(root, 973, 800);
    primaryStage.setScene(applicationScene);
    primaryStage.show();
  }

  //  @Override
  //  public void start(Stage primaryStage) throws Exception {
  //    Parent root = FXMLLoader.load(getClass().getResource("/fxml/EmployeeTable.fxml"));
  //    TreeTableView<Employee> employeeTable = (TreeTableView<Employee>)
  // root.lookup("#employeeTable");
  //    TextField idField = (TextField) root.lookup("#employeeID");
  //    TextField fNameField = (TextField) root.lookup("#firstName");
  //    TextField lNameField = (TextField) root.lookup("#lastName");
  //    TextField genderField = (TextField) root.lookup("#gender");
  //    TextField posField = (TextField) root.lookup("#position");
  //    Button addBtn = (Button) root.lookup("#addBtn");
  //    Button delBtn = (Button) root.lookup("#delBtn");
  //
  //    idField.setPromptText("Employee ID");
  //    fNameField.setPromptText("First Name");
  //    lNameField.setPromptText("Last Name");
  //    genderField.setPromptText("Gender");
  //    posField.setPromptText("Pos1,Pos2,Pos3");
  //
  //    employeeTable.setEditable(true);
  //    positions.forEach(pos -> pos.setExpanded(true));
  //    dummyList.forEach(
  //        (employee -> {
  //          for (TreeItem pos : positions) {
  //            for (String empPos : employee.position) {
  //              if (((Employee) pos.getValue()).getEmployeeID().equals(empPos)) {
  //                pos.getChildren().add(new TreeItem<>(employee));
  //              }
  //            }
  //          }
  //        }));
  //
  //    addBtn.setOnAction(
  //        new EventHandler<ActionEvent>() {
  //          @Override
  //          public void handle(ActionEvent event) {
  //            Employee newEmp =
  //                new Employee(
  //                    idField.getText(),
  //                    lNameField.getText(),
  //                    fNameField.getText(),
  //                    genderField.getText(),
  //                    positionToList(posField.getText()));
  //            dummyList.add(newEmp);
  //            for (TreeItem pos : positions) {
  //              for (String empPos : newEmp.position) {
  //                if (((Employee) pos.getValue()).getEmployeeID().equals(empPos)) {
  //                  pos.getChildren().add(new TreeItem<>(newEmp));
  //                }
  //              }
  //            }
  //            idField.clear();
  //            lNameField.clear();
  //            fNameField.clear();
  //            genderField.clear();
  //            posField.clear();
  //          }
  //        });
  //
  //    TreeTableColumn<Employee, String> idCol = new TreeTableColumn<Employee, String>("Employee
  // ID");
  //    idCol.setPrefWidth(160);
  //    idCol.setCellValueFactory(
  //        new Callback<
  //            TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
  //          @Override
  //          public ObservableValue<String> call(
  //              TreeTableColumn.CellDataFeatures<Employee, String> p) {
  //            Employee f = p.getValue().getValue();
  //            String text = f.getEmployeeID() == null ? "/" : f.getEmployeeID();
  //            return new ReadOnlyObjectWrapper<String>(text);
  //          }
  //        });
  //    idCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
  //    idCol.setOnEditCommit(
  //        new EventHandler<TreeTableColumn.CellEditEvent<Employee, String>>() {
  //          @Override
  //          public void handle(TreeTableColumn.CellEditEvent<Employee, String> event) {
  //            Employee selectedEmp = event.getRowValue().getValue();
  //            for (Employee emp : dummyList) {
  //              if (emp.employeeID.equals(selectedEmp.getEmployeeID())) {
  //                event.getRowValue().getValue().setEmployeeID(event.getNewValue());
  //                emp.setEmployeeID(event.getNewValue());
  //              }
  //            }
  //            refresh();
  //          }
  //        });
  //
  //    TreeTableColumn<Employee, String> fNameCol =
  //        new TreeTableColumn<Employee, String>("First Name");
  //    fNameCol.setPrefWidth(160);
  //    fNameCol.setCellValueFactory(
  //        new Callback<
  //            TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
  //          @Override
  //          public ObservableValue<String> call(
  //              TreeTableColumn.CellDataFeatures<Employee, String> p) {
  //            Employee f = p.getValue().getValue();
  //            String text = f.getFirstName() == null ? "/" : f.getFirstName();
  //            return new ReadOnlyObjectWrapper<String>(text);
  //          }
  //        });
  //    fNameCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
  //    fNameCol.setOnEditCommit(
  //        new EventHandler<TreeTableColumn.CellEditEvent<Employee, String>>() {
  //          @Override
  //          public void handle(TreeTableColumn.CellEditEvent<Employee, String> event) {
  //            Employee selectedEmp = event.getRowValue().getValue();
  //            for (Employee emp : dummyList) {
  //              if (emp.employeeID.equals(selectedEmp.getEmployeeID())) {
  //                event.getRowValue().getValue().setFirstName(event.getNewValue());
  //                emp.setFirstName(event.getNewValue());
  //              }
  //            }
  //            refresh();
  //          }
  //        });
  //
  //    TreeTableColumn<Employee, String> lNameCol = new TreeTableColumn<Employee, String>("Last
  // Name");
  //    lNameCol.setPrefWidth(160);
  //    lNameCol.setCellValueFactory(
  //        new Callback<
  //            TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
  //          @Override
  //          public ObservableValue<String> call(
  //              TreeTableColumn.CellDataFeatures<Employee, String> p) {
  //            Employee f = p.getValue().getValue();
  //            String text = f.getLastName() == null ? "/" : f.getLastName();
  //            return new ReadOnlyObjectWrapper<String>(text);
  //          }
  //        });
  //    lNameCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
  //    lNameCol.setOnEditCommit(
  //        new EventHandler<TreeTableColumn.CellEditEvent<Employee, String>>() {
  //          @Override
  //          public void handle(TreeTableColumn.CellEditEvent<Employee, String> event) {
  //            Employee selectedEmp = event.getRowValue().getValue();
  //            for (Employee emp : dummyList) {
  //              if (emp.employeeID.equals(selectedEmp.getEmployeeID())) {
  //                event.getRowValue().getValue().setLastName(event.getNewValue());
  //                emp.setLastName(event.getNewValue());
  //              }
  //            }
  //            refresh();
  //          }
  //        });
  //
  //    TreeTableColumn<Employee, String> genderCol = new TreeTableColumn<Employee,
  // String>("Gender");
  //    genderCol.setPrefWidth(160);
  //    genderCol.setCellValueFactory(
  //        new Callback<
  //            TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
  //          @Override
  //          public ObservableValue<String> call(
  //              TreeTableColumn.CellDataFeatures<Employee, String> p) {
  //            Employee f = p.getValue().getValue();
  //            String text = f.getGender() == null ? "/" : f.getGender();
  //            return new ReadOnlyObjectWrapper<String>(text);
  //          }
  //        });
  //    genderCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
  //    genderCol.setOnEditCommit(
  //        new EventHandler<TreeTableColumn.CellEditEvent<Employee, String>>() {
  //          @Override
  //          public void handle(TreeTableColumn.CellEditEvent<Employee, String> event) {
  //            Employee selectedEmp = event.getRowValue().getValue();
  //            for (Employee emp : dummyList) {
  //              if (emp.employeeID.equals(selectedEmp.getEmployeeID())) {
  //                event.getRowValue().getValue().setGender(event.getNewValue());
  //                emp.setGender(event.getNewValue());
  //              }
  //            }
  //            refresh();
  //          }
  //        });
  //
  //    TreeTableColumn<Employee, String> posCol = new TreeTableColumn<Employee,
  // String>("Position");
  //    posCol.setPrefWidth(160);
  //    posCol.setCellValueFactory(
  //        new Callback<
  //            TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
  //          @Override
  //          public ObservableValue<String> call(
  //              TreeTableColumn.CellDataFeatures<Employee, String> p) {
  //            Employee f = p.getValue().getValue();
  //            String text = f.getPosition() == null ? "/" : f.getPosition();
  //            return new ReadOnlyObjectWrapper<String>(text);
  //          }
  //        });
  //    posCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
  //    posCol.setOnEditCommit(
  //        new EventHandler<TreeTableColumn.CellEditEvent<Employee, String>>() {
  //          @Override
  //          public void handle(TreeTableColumn.CellEditEvent<Employee, String> event) {
  //            Employee selectedEmp = event.getRowValue().getValue();
  //            for (Employee emp : dummyList) {
  //              if (emp.employeeID.equals(selectedEmp.getEmployeeID())) {
  //                event.getRowValue().getValue().setPosition(positionToList(event.getNewValue()));
  //                emp.setPosition(positionToList(event.getNewValue()));
  //              }
  //            }
  //            refresh();
  //          }
  //        });
  //
  //    employeeTable.setRoot(new TreeItem<>());
  //    employeeTable.setShowRoot(false);
  //    for (TreeItem pos : positions) {
  //      pos.setExpanded(true);
  //      employeeTable.getRoot().getChildren().add(pos);
  //    }
  //    employeeTable.getColumns().addAll(idCol, fNameCol, lNameCol, genderCol, posCol);
  //
  //    employeeTable.addEventHandler(
  //        MouseEvent.MOUSE_CLICKED,
  //        new EventHandler<MouseEvent>() {
  //          @Override
  //          public void handle(MouseEvent event) {
  //            delBtn.setOnAction(
  //                e -> {
  //                  Employee selectedEmp =
  //                      employeeTable.getSelectionModel().getSelectedItem().getValue();
  //                  dummyList.removeIf(
  //                      employee -> employee.getEmployeeID().equals(selectedEmp.getEmployeeID()));
  //                  for (TreeItem pos : positions) {
  //                    pos.getChildren()
  //                        .removeIf(
  //                            child ->
  //                                ((TreeItem<Employee>) child)
  //                                    .getValue()
  //                                    .getEmployeeID()
  //                                    .equals(selectedEmp.getEmployeeID()));
  //                  }
  //                });
  //          }
  //        });
  //
  //    primaryStage.setTitle("Employee Table");
  //    primaryStage.setScene(new Scene(root, 800, 600));
  //    primaryStage.show();
  //  }

  //  public class Employee {
  //    private String employeeID, lastName, firstName, gender;
  //    private List<String> position;
  //
  //    public String getEmployeeID() {
  //      return employeeID;
  //    }
  //
  //    public void setEmployeeID(String employeeID) {
  //      this.employeeID = employeeID;
  //    }
  //
  //    public String getLastName() {
  //      return lastName;
  //    }
  //
  //    public void setLastName(String lastName) {
  //      this.lastName = lastName;
  //    }
  //
  //    public String getFirstName() {
  //      return firstName;
  //    }
  //
  //    public void setFirstName(String firstName) {
  //      this.firstName = firstName;
  //    }
  //
  //    public String getGender() {
  //      return gender;
  //    }
  //
  //    public void setGender(String gender) {
  //      this.gender = gender;
  //    }
  //
  //    public String getPosition() {
  //      String ret = "";
  //      for (String s : position) {
  //        ret += s;
  //        ret += ", ";
  //      }
  //      return ret;
  //    }
  //
  //    public void setPosition(List<String> position) {
  //      this.position = position;
  //    }
  //
  //    private Employee(String id, String lName, String fName, String gender, List<String>
  // position) {
  //      this.employeeID = id;
  //      this.lastName = lName;
  //      this.firstName = fName;
  //      this.gender = gender;
  //      this.position = position;
  //    }
  //  }
  //
  //  public List<String> positionToList(String pos) {
  //    List list = new ArrayList();
  //    for (String str : pos.trim().split(",")) {
  //      list.add(str);
  //    }
  //    return list;
  //  }
}
