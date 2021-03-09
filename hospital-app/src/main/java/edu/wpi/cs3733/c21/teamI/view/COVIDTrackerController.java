package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.JFXTreeView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;

import java.util.Arrays;

public class COVIDTrackerController {
  @FXML
  JFXTreeTableView symptomList;

  @FXML
  public void initialize() {
    JFXTreeTableColumn<String, String> symptoms = new JFXTreeTableColumn<>("Symptoms");
    symptoms.setPrefWidth(150);
    symptoms.setEditable(true);
    symptoms.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue()));

    final TreeItem<String> root =
            new RecursiveTreeItem<~>(FXCollections.observableList(
                    Arrays.asList(
                            "Sore throat",
                            "Shortness of breath",
                            "New loss of taste or smell",
                            "Fever or chills",
                            "Congestion or runny nose",
                            "Cough",
                            "Nausea or vomiting",
                            "Diarrhea",
                            "Headache",
                            "Has been in contact with someone COVID-19 positive",
                            "Has been in contact with someone COVID-19 symptomatic",
                            "Is currently waiting on a COVID-19 test result")), RecursiveTreeObject::getChildren);
    symptomList.getColumns().add(symptoms);
    symptomList.setRoot(root);
    symptomList.setShowRoot(false);
  }
}
