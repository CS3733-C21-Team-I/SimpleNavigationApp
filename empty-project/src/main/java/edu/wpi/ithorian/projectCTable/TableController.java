package edu.wpi.ithorian.projectCTable;

import edu.wpi.ithorian.hospitalMap.HospitalMapNode;
import edu.wpi.ithorian.hospitalMap.LocationNode;
import edu.wpi.ithorian.hospitalMap.mapEditing.MapEditManager;
import java.io.*;
import java.util.HashSet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class TableController {
  @FXML TextField Node_ID, Xcod, Ycod, Floor, Building, Type, Long, Short, Team;
  @FXML Button Add;

  private static MapEditManager ourManager;
  private MapEditManager mapManager;

  public TableController(MapEditManager mapEditManager) {
    this.mapManager = mapEditManager;
  }

  public TableController() {
    this.mapManager = ourManager;
  }

  public static void saveManager() {
    ourManager = new MapEditManager().getInstance();
  }

  @FXML
  public void onAddAction() {
    System.out.println(mapManager.getEntityNodes());
    System.out.println("Map manager: " + mapManager);
    String nID = Node_ID.getText();
    String lName = Long.getText();
    String sName = Short.getText();
    String tName = Team.getText();
    String xCord = Xcod.getText();
    String yCord = Ycod.getText();
    String fName = Floor.getText();
    String bName = Building.getText();
    String type = Type.getText();

    LocationNode newNode =
        new LocationNode(
            nID,
            Integer.parseInt(xCord),
            Integer.parseInt(yCord),
            sName,
            lName,
            tName,
            new HashSet<HospitalMapNode>());
    mapManager.addNode(newNode);
    System.out.println(mapManager.getEntityNodes());
  }

  private String getCurrentDirectory() {
    File file = new File(".");
    return file.getAbsolutePath();
  }
}
