package edu.wpi.ithorian.projectCTable;

import edu.wpi.ithorian.hospitalMap.tableEditing.FullLocationNode;
import edu.wpi.ithorian.hospitalMap.tableEditing.FullMapEditManager;
import java.io.*;
import java.util.HashSet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class TableController {
  @FXML TextField Node_ID, Xcod, Ycod, Floor, Building, Type, Long, Short, Team;
  @FXML Button Add;

  private static FullMapEditManager ourManager;
  private FullMapEditManager mapManager;

  public TableController(FullMapEditManager fullMapEditManager) {
    this.mapManager = fullMapEditManager;
  }

  public TableController() {
    this.mapManager = ourManager;
  }

  public static void saveManager() {
    ourManager = new FullMapEditManager().getInstance();
  }

  @FXML
  public void onAddAction() throws IOException {
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

    FullLocationNode newNode =
        new FullLocationNode(
            nID,
            Integer.parseInt(xCord),
            Integer.parseInt(yCord),
            fName,
            bName,
            type,
            sName,
            lName,
            tName,
            new HashSet<FullLocationNode>());
    mapManager.addNode(newNode);
    System.out.println(mapManager.getEntityNodes());
  }

  private String getCurrentDirectory() {
    File file = new File(".");
    return file.getAbsolutePath();
  }
}
