package edu.wpi.cs3733.c21.teamI.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

public class TabController extends Application implements Initializable {
  @FXML Tab campus;
  @FXML Tab floor1;
  @FXML Tab floor2;
  @FXML Tab floor3;
  @FXML Tab floor4;
  @FXML Tab floor6;
  private Tab currentTab = null;

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  @Override
  public void start(Stage primaryStage) throws Exception {}

  public void campusTab(Event event) {
    if (campus != currentTab) {
      System.out.println("Tab 1");
      ViewManager.setActiveMap("Faulkner 0");
      ViewManager.refreshMap();
      currentTab = campus;
    }
  }

  public void floor1Tab(Event event) {
    if (floor1 != currentTab) {
      System.out.println("Tab 2");
      ViewManager.setActiveMap("Faulkner 1");
      ViewManager.refreshMap();
      currentTab = floor1;
    }
  }

  public void floor2Tab(Event event) {
    if (floor2 != currentTab) {
      System.out.println("Tab 3");
      ViewManager.setActiveMap("Faulkner 2");
      ViewManager.refreshMap();
      currentTab = floor2;
    }
  }

  public void floor3Tab(Event event) {
    if (floor3 != currentTab) {

      System.out.println("Tab 4");
      ViewManager.setActiveMap("Faulkner 3");
      ViewManager.refreshMap();
      currentTab = floor3;
    }
  }

  public void floor4Tab(Event event) {
    if (floor4 != currentTab) {
      System.out.println("Tab 5");
      ViewManager.setActiveMap("Faulkner 4");
      ViewManager.refreshMap();
      currentTab = floor4;
    }
  }

  public void floor5Tab(Event event) {
    if (floor6 != currentTab) {
      System.out.println("Tab 6");
      ViewManager.setActiveMap("Faulkner 5");
      ViewManager.refreshMap();
      currentTab = floor6;
    }
  }
}
