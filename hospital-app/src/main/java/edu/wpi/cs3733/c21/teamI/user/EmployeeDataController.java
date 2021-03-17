package edu.wpi.cs3733.c21.teamI.user;

import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.user.User.Role;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EmployeeDataController {

  public static EmployeeDataController getInstance() {
    if (ourInstance == null) ourInstance = new EmployeeDataController();

    return ourInstance;
  }

  private static EmployeeDataController ourInstance;

  private List<Employee> allEmployees;

  private Map<Role, ObservableList<Employee>> employeesWithRole;

  private EmployeeDataController() {
    this.allEmployees = new ArrayList<>();
    this.allEmployees.addAll(UserDatabaseManager.getInstance().getAllEmployees());
    this.employeesWithRole = new HashMap<>();
  }

  public List<Employee> getAllEmployees() {
    return allEmployees;
  }

  public void resetEmployees() {
    allEmployees = new ArrayList<>();
    allEmployees.addAll(UserDatabaseManager.getInstance().getAllEmployees());

    for (Role role : employeesWithRole.keySet()) {
      employeesWithRole.get(role).clear();
      for (Employee employee : allEmployees) {
        if (employee.userRoles.contains(role)) {
          employeesWithRole.get(role).add(employee);
        }
      }
    }
  }

  public ObservableList<Employee> getEmployeesWithRole(Role role) {
    if (!employeesWithRole.containsKey(role)) {
      employeesWithRole.put(role, FXCollections.observableArrayList());

      for (Employee employee : getAllEmployees()) {
        if (employee.userRoles.contains(role)) {
          employeesWithRole.get(role).add(employee);
        }
      }
    }

    return employeesWithRole.get(role);
  }
}
