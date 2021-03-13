package edu.wpi.cs3733.c21.teamI.parking;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Lot {

  private String name;
  private IntegerProperty unocupied = new SimpleIntegerProperty();

  private List<Block> blocks;

  public Lot(int id, String name, boolean isReentyAllowed, boolean isValetAvailable) {
    this.name = name;
    this.blocks = new ArrayList<>();
  }

  public void refreshUnocupied() {
    unocupied.setValue(0);
    for (Block block : blocks) {
      block.refreshUnocupied();

      unocupied.setValue(unocupied.getValue() + block.getUnocupied().get());
    }
    System.out.println("Lot refreshed with: " + unocupied + " unoccupied");
  }

  public IntegerProperty getUnocupied() {
    return unocupied;
  }

  public void addBlock(Block newBlock) {
    blocks.add(newBlock);
    newBlock.setLot(this);
  }

  public String getName() {
    return name;
  }

  public int getCapacity() {
    int cap = 0;
    for (Block block : blocks) {
      for (Floor floor : block.getFloors()) {
        cap += floor.getSlotCodes().size();
      }
    }
    return cap;
  }

  public List<Block> getBlocks() {
    return blocks;
  }
}
