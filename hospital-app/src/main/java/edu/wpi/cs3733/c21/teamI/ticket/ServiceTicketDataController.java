package edu.wpi.cs3733.c21.teamI.ticket;

import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class ServiceTicketDataController {

  private static Map<User.Permission, List<String>> ticketAssignees = new HashMap<>();

  private static List<String> hospitalMapNodes;

  public static void lookupUsernames(
      KeyEvent e, User.Permission permission, ListView listView, TextField target) {
    String matchString =
        (((TextField) e.getSource()).getText()
                + (!e.getCharacter().equals(Character.toString((char) 8)) ? e.getCharacter() : ""))
            .toLowerCase()
            .replaceAll("[^a-zA-Z]+", "");
    List<String> nameList;
    if (!ticketAssignees.containsKey(permission)) {
      nameList = UserDatabaseManager.getInstance().getUsernamesWithPermission(permission);
      ticketAssignees.put(permission, nameList);
    } else {
      nameList = ticketAssignees.get(permission);
    }
    ArrayList<String> matches = new ArrayList<>();
    for (String username : nameList) {
      if (username.toLowerCase().contains(matchString)) {
        matches.add(username);
      }
    }

    // Add elements to ListView
    ObservableList<String> items = FXCollections.observableArrayList(matches);
    listView.setItems(items);
    listView.setVisible(e.getSource() == target);
  }

  public static void lookupNodes(KeyEvent e, ListView listView, TextField target) {

    String matchString =
        (((TextField) e.getSource()).getText()
                + (!e.getCharacter().equals(Character.toString((char) 8)) ? e.getCharacter() : ""))
            .toLowerCase();
    if (hospitalMapNodes == null) {
      hospitalMapNodes = new ArrayList<>();
      hospitalMapNodes.addAll(NavDatabaseManager.getInstance().getLocationNodeLongNames());
    }
    List<String> nodeNames = hospitalMapNodes;

    List<String> matches = new ArrayList<>();
    for (String location : nodeNames) {
      if (location.toLowerCase().contains(matchString)) {
        matches.add(location);
      }
    }

    // Add elements to ListView
    ObservableList<String> items = FXCollections.observableArrayList(matches);
    listView.setItems(items);
    listView.setVisible(e.getSource() == target);
  }

  public static void setupRequestView(
      AnchorPane background,
      ListView serviceLocationList,
      ListView requestAssignedList,
      TextField requestID,
      TextField assignedID,
      TextField location) {

    List<String> locations = NavDatabaseManager.getInstance().getLocationNodeLongNames();
    location.setOnMouseClicked(
        t -> {
          ObservableList<String> items = FXCollections.observableArrayList(locations);
          serviceLocationList.setItems(items);
          serviceLocationList.setVisible(true);
        });

    serviceLocationList
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (ChangeListener<String>)
                (ov, oldVal, newVal) -> {
                  location.setText(newVal);
                  serviceLocationList.setVisible(false);
                });

    requestAssignedList
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (ChangeListener<String>)
                (ov, oldVal, newVal) -> {
                  assignedID.setText(newVal);
                  requestAssignedList.setVisible(false);
                });

    background.setOnMouseClicked(
        t -> {
          serviceLocationList.setVisible(false);
          requestAssignedList.setVisible(false);
        });

    requestID.setText(ApplicationDataController.getInstance().getLoggedInUser().getName());
  }

  public static void refreshMapNodes() {
    hospitalMapNodes = null;
  }

  public static void refreshEmployees() {
    ticketAssignees = new HashMap<>();
  }
}
