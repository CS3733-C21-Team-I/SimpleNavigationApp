package edu.wpi.cs3733.c21.teamI.parking;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Block {

  private String blockCode;

  private int id;
  private List<Floor> floors;
  private Lot lot;
  private IntegerProperty unocupied = new SimpleIntegerProperty();

  public Block(int id, String blockCode) {
    this.id = id;
    this.blockCode = blockCode;
    this.floors = new ArrayList<>();
    this.lot = null;
  }

  public Block(String blockCode) {
    this.id = -1;
    this.blockCode = blockCode;
    this.floors = new ArrayList<>();
    this.lot = null;
  }

  public void refreshUnocupied() {
    int u = 0;
    for (Floor floor : floors) {

      floor.refreshUnocupied();

      u += floor.getUnocupied().get();
    }
    unocupied.setValue(u);
  }

  public IntegerProperty getUnocupied() {
    return unocupied;
  }

  public void addFloor(Floor f) {
    floors.add(f);
    f.setBlock(this);
  }

  public void setLot(Lot lot) {
    this.lot = lot;
  }

  public List<Floor> getFloors() {
    return floors;
  }

  public String getBlockCode() {
    return blockCode;
  }

  public int getCapacity() {
    int count = 0;
    for (Floor floor : floors) {
      count += floor.getSlotCodes().size();
    }
    return count;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
