package edu.wpi.cs3733.c21.teamI.parking.view;

import com.jfoenix.controls.JFXListCell;
import edu.wpi.cs3733.c21.teamI.parking.Floor;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class FloorListItem extends JFXListCell<Floor> {

  @FXML private GridPane gridPane;

  @FXML private Label floorNumLabel;

  @FXML private Label occupancyLabel;

  @FXML private Label accessibilityLabel;

  @FXML private Label coveredLabel;

  @FXML private Label staffOnlyLabel;

  private FXMLLoader mLLoader;

  @FXML
  @Override
  protected void updateItem(Floor item, boolean empty) {
    super.updateItem(item, empty);

    System.out.println("Hello block update");

    if (empty) {
      setText(null);
      setContentDisplay(ContentDisplay.TEXT_ONLY);
    } else {

      if (mLLoader == null) {
        mLLoader = new FXMLLoader(getClass().getResource("/fxml/parking/FloorListItem.fxml"));
        mLLoader.setController(this);

        try {
          mLLoader.load();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      floorNumLabel.setText(item.getFloorNum() + "");
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

      accessibilityLabel.setVisible(item.isDisabledAcessable());
      coveredLabel.setVisible(item.isCovered());
      staffOnlyLabel.setVisible(item.isStaffOnly());

      gridPane.setStyle(
          "    -fx-border-radius: 10px;\n"
              + "    -fx-border-width: 2px;\n"
              + "    -fx-border-color: black;\n");

      setGraphic(gridPane);
      setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }
  }

  @FXML
  public void initialize() {
    System.out.println("Hello Block Init");
    setGraphic(gridPane);

    setPadding(new Insets(0, 0, 0, 0));
  }
}
