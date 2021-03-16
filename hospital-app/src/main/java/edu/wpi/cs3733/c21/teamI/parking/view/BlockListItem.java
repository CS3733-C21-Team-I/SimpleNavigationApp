package edu.wpi.cs3733.c21.teamI.parking.view;

import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import edu.wpi.cs3733.c21.teamI.parking.Block;
import edu.wpi.cs3733.c21.teamI.parking.Floor;
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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class BlockListItem extends JFXListCell<Block> {

  @FXML private Pane pane;

  @FXML private Label blockLabel;

  @FXML private JFXListView floorList;

  @FXML private Label occupancyLabel;

  private ObservableList<Floor> floors;

  private FXMLLoader mLLoader;

  @FXML
  @Override
  protected void updateItem(Block item, boolean empty) {
    super.updateItem(item, empty);

    System.out.println("Hello block update");

    if (empty) {
      setText(null);
      setGraphic(null);
      setContentDisplay(ContentDisplay.TEXT_ONLY);
    } else {

      if (mLLoader == null) {
        mLLoader = new FXMLLoader(getClass().getResource("/fxml/parking/BlockListItem.fxml"));
        mLLoader.setController(this);
        try {
          mLLoader.load();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      System.out.println(blockLabel);
      blockLabel.setText("Name: " + item.getBlockCode());

      int unocupied = item.getUnocupied().get();
      occupancyLabel.setText("Slots: " + unocupied + "/" + item.getCapacity());

      if (unocupied == 0)
        setBackground(
            new Background(
                new BackgroundFill(Color.web("#FFCCCC"), CornerRadii.EMPTY, Insets.EMPTY)));
      else
        setBackground(
            new Background(
                new BackgroundFill(Color.web("#CCFFCC"), CornerRadii.EMPTY, Insets.EMPTY)));

      if (floors.isEmpty()) {
        Callback<Floor, Observable[]> extractor =
            new Callback<Floor, Observable[]>() {

              @Override
              public Observable[] call(Floor p) {
                return new Observable[] {p.getUnocupied()};
              }
            };
        floors = FXCollections.observableArrayList(extractor);
        floors.setAll(item.getFloors());
        floorList.setItems(floors);
        floorList.prefHeightProperty().setValue(floors.size() * 30);
        pane.prefHeightProperty().setValue(70 + floors.size() * 30);
      }

      setGraphic(pane);
      setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }
  }

  @FXML
  public void initialize() {
    System.out.println("Hello Block Init");
    setGraphic(pane);

    floorList.setCellFactory(new FloorListItemFactory());

    floors = FXCollections.emptyObservableList();

    setPadding(new Insets(0, 0, 0, 0));
    floorList.setPrefWidth(450);
  }
}
