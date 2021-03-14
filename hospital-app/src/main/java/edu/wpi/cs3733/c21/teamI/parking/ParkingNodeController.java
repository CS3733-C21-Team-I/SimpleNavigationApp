package edu.wpi.cs3733.c21.teamI.parking;

import edu.wpi.cs3733.c21.teamI.database.ParkingPeripheralServerManager;
import edu.wpi.cs3733.c21.teamI.hospitalMap.ParkingNode;
import java.util.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class ParkingNodeController {

  private static ParkingNodeController ourInstance;

  public static ParkingNodeController getInstance() {
    if (ourInstance == null) {
      ourInstance = new ParkingNodeController();
    }

    return ourInstance;
  }

  private Map<Integer, ParkingNode> linkedNodes;

  private ParkingNodeController() {
    this.linkedNodes = new HashMap<>();

    pollDB();

    Timeline fiveSecondsWonder =
        new Timeline(
            new KeyFrame(
                Duration.seconds(5),
                new EventHandler<ActionEvent>() {

                  @Override
                  public void handle(ActionEvent event) {
                    //                    lotView.setItems(null);
                    //                    lots =
                    //                        FXCollections.observableArrayList(
                    //
                    // ParkingPeripheralServerManager.getInstance().loadLots().values());
                    //                    lotView.setItems(lots);
                    pollDB();
                    System.out.println("this is called every 5 seconds on UI thread");
                  }
                }));
    fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
    fiveSecondsWonder.play();
  }

  public void registerNode(ParkingNode node) {
    // TODO get linked slotId from DB
    int slotId = -1;

    node.setParkingID(slotId);
    linkedNodes.put(slotId, node);
  }

  public void pollDB() {
    // TODO get list of occupied nodes from DB
    Collection<Integer> occupied = ParkingPeripheralServerManager.getInstance().getOccupiedSlots();

    for (Integer i : linkedNodes.keySet()) {
      if (occupied.contains(i)) linkedNodes.get(i).setOccupied(true);
      else linkedNodes.get(i).setOccupied(false);
    }
  }
}
