package edu.wpi.cs3733.c21.teamI.parking.view;

import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import edu.wpi.cs3733.c21.teamI.parking.Block;
import edu.wpi.cs3733.c21.teamI.parking.Lot;
import java.io.IOException;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class LotPane extends JFXListCell<Lot> {

  @FXML private Label nameLabel;

  @FXML private Label occupancyLabel;

  @FXML private JFXListView blockList;

  @FXML private VBox vBox;

  private FXMLLoader mLLoader;

  private ObservableList<Block> blocks;

  public LotPane() {
    // loadFXML();

  }

  void loadFXML() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/parking/LotPane.fxml"));

      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Lot lastItem;

  @FXML
  @Override
  protected void updateItem(Lot item, boolean empty) {
    super.updateItem(item, empty);

    if (empty) {
      System.out.println("Empty Lot Pane");
      System.out.println(item);
      setText(null);
      setGraphic(null);
      setContentDisplay(ContentDisplay.TEXT_ONLY);

    } else {

      if (mLLoader == null) {
        System.out.println("Loading");
        mLLoader = new FXMLLoader(getClass().getResource("/fxml/parking/LotPane.fxml"));
        mLLoader.setController(this);

        try {
          mLLoader.load();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      nameLabel.setText("Name: " + item.getName());
      int unocupied = item.getUnocupied().get();
      occupancyLabel.setText("Slots: " + unocupied + "/" + item.getCapacity());

      //      if (unocupied == 0)
      //        setBackground(
      //            new Background(
      //                new BackgroundFill(Color.web("#FFCCCC"), CornerRadii.EMPTY, Insets.EMPTY)));
      //      else
      //        setBackground(
      //            new Background(
      //                new BackgroundFill(Color.web("#CCFFCC"), CornerRadii.EMPTY, Insets.EMPTY)));

      setBackground(
          new Background(
              new BackgroundFill(Color.web("#012D5A"), new CornerRadii(10.0), new Insets(1.0))));

      if (blocks.isEmpty()) {
        Callback<Block, Observable[]> extractor =
            new Callback<Block, Observable[]>() {

              @Override
              public Observable[] call(Block p) {
                return new Observable[] {p.getUnocupied()};
              }
            };
        blocks = FXCollections.observableArrayList(extractor);
        blocks.setAll(item.getBlocks());
        blockList.setItems(blocks);

        System.out.println("Set blocks" + blocks.size());
      }

      //      if (unocupied == 0)
      //        blockList.setBackground(
      //            new Background(
      //                new BackgroundFill(Color.web("#FFCCCC"), new CornerRadii(10.0), new
      // Insets(1.0))));
      //      else
      //        blockList.setBackground(
      //            new Background(
      //                new BackgroundFill(Color.web("#CCFFCC"), new CornerRadii(10.0), new
      // Insets(1.0))));

      blockList.setBackground(
          new Background(
              new BackgroundFill(Color.web("#CCFFCC"), new CornerRadii(10.0), new Insets(1.0))));
      nameLabel.setStyle("-fx-text-fill: white");
      occupancyLabel.setStyle("-fx-text-fill: white");

      setGraphic(vBox);
      setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }
  }

  @FXML
  public void initialize() {
    setGraphic(vBox);
    blockList.setCellFactory(new BlockListItemFactory());

    blocks = FXCollections.emptyObservableList();
    blockList.setPrefWidth(255);
    blockList.setPrefHeight(500);
  }
}
