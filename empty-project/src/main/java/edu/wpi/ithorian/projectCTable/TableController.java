package edu.wpi.ithorian.projectCTable;

import com.opencsv.CSVWriter;
import java.io.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class TableController {
  @FXML TextField Node_ID, Xcod, Ycod, Floor, Building, Type, Long, Short, Team;
  @FXML Button Add;

  @FXML
  public void onAddAction() {
    String nID = Node_ID.getText();
    String lName = Long.getText();
    String sName = Short.getText();
    String tName = Team.getText();
    String xCord = Xcod.getText();
    String yCord = Ycod.getText();
    String fName = Floor.getText();
    String bName = Building.getText();
    String type = Type.getText();
    String is = getCurrentDirectory();
    is =
        is.substring(0, is.length() - 1)
            + "empty-project\\src\\main\\java\\edu\\wpi\\ithorian\\hospitalMap\\mapEditing\\MapINodes.csv";
    try {
      CSVWriter writer =
          new CSVWriter(
              new FileWriter(is, true), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
      String line[] = {nID, xCord, yCord, fName, bName, type, lName, sName, tName, "", ""};
      writer.writeNext(line);
      writer.close();
    } catch (FileNotFoundException e) {
      System.out.println("File not Found");
    } catch (IOException e) {
      System.out.println("IO Exception");
    }
  }

  private String getCurrentDirectory() {
    File file = new File(".");
    return file.getAbsolutePath();
  }
}
