package edu.wpi.cs3733.c21.teamI.parking.view;

import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import edu.wpi.cs3733.c21.teamI.parking.Block;
import javafx.util.Callback;

public class BlockListItemFactory implements Callback<JFXListView<Block>, JFXListCell<Block>> {

  @Override
  public JFXListCell<Block> call(JFXListView<Block> param) {
    return new BlockListItem();
  }
}
