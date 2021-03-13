package edu.wpi.cs3733.c21.teamI.view.mobile;

import com.jfoenix.controls.JFXButton;
import java.awt.*;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javax.swing.*;

public class PayParkingTicket {

  @FXML JFXButton submitBtn1, completeTransaction;
  @FXML StackPane root;
  @FXML AnchorPane background;
  @FXML ImageView phone;
  @FXML AnchorPane screen;
  @FXML TextField parkingIDInput;

  public void initialize() {
    completeTransaction.setDisable(true);
    setUpParkingDropdown();
  }

  public void goToPathfinding(ActionEvent e) throws IOException {
    root.getChildren().clear();
    root.getChildren()
        .add(FXMLLoader.load(getClass().getResource("/fxml/mobilePages/PathfindingMobile.fxml")));
  }

  private void setUpParkingDropdown() {

    //        parkingList
    //                .getSelectionModel()
    //                .selectedItemProperty()
    //                .addListener(
    //                        (ChangeListener<String>)
    //                                (ov, oldVal, newVal) -> {
    //                                    parkingIDInput.setText(newVal);
    //                                    parkingList.setVisible(false);
    //                                });
    //        background.setOnMouseClicked(
    //                t -> {
    //                    parkingList.setVisible(false);
    //                });
  }

  @FXML
  public void lookupParking(javafx.scene.input.KeyEvent e) {
    //        String matchString =
    //                (parkingIDInput.getText()
    //                        + (!e.getCharacter().equals(Character.toString((char) 8)) ?
    // e.getCharacter() : ""))
    //                        .toLowerCase();
    //        List<String> nodeNames = NavDatabaseManager.getInstance().getParkingLongNames();
    //
    //        List<String> matches = new ArrayList<>();
    //        System.out.println(nodeNames);
    //        System.out.println(matchString);
    //        for (String location : nodeNames) {
    //            if (location.toLowerCase().contains(matchString)) {
    //                matches.add(location);
    //            }
    //        }
    //
    //        // Add elements to ListView
    //        ObservableList<String> items = FXCollections.observableArrayList(matches);
    //     //   parkingList.setItems(items);
    //     //   parkingList.setVisible(true);
  }

  public void submit() throws IOException {}

  public void clear() {}

  @FXML
  public void exit(javafx.scene.input.MouseEvent e) {
    Stage stage = (Stage) ((Circle) e.getSource()).getScene().getWindow();
    stage.close();
  }
}
