package edu.wpi.cs3733.c21.teamI.parking;

import java.util.ArrayList;
import java.util.List;

public class Lot {

  private int id;
  private String name;
  private boolean isReentyAllowed;
  private boolean isValetAvailable;

  private List<Block> blocks;

  public Lot(int id, String name, boolean isReentyAllowed, boolean isValetAvailable) {
    this.id = id;
    this.name = name;
    this.isReentyAllowed = isReentyAllowed;
    this.isValetAvailable = isValetAvailable;
    this.blocks = new ArrayList<>();
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
