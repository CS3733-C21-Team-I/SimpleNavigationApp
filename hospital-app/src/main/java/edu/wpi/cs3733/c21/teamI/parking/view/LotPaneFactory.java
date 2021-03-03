package edu.wpi.cs3733.c21.teamI.parking.view;

import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import edu.wpi.cs3733.c21.teamI.parking.Lot;
import javafx.util.Callback;

public class LotPaneFactory implements Callback<JFXListView<Lot>, JFXListCell<Lot>> {

  @Override
  public JFXListCell<Lot> call(JFXListView<Lot> param) {
    LotPane pane = new LotPane();

    return pane;
  }
}
