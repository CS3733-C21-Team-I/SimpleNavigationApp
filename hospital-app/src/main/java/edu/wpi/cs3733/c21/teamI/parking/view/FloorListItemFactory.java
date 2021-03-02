package edu.wpi.cs3733.c21.teamI.parking.view;

import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import edu.wpi.cs3733.c21.teamI.parking.Floor;
import javafx.util.Callback;

public class FloorListItemFactory implements Callback<JFXListView<Floor>, JFXListCell<Floor>> {

  @Override
  public JFXListCell<Floor> call(JFXListView<Floor> param) {
    return new FloorListItem();
  }
}
