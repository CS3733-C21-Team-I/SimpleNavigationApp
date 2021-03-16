package edu.wpi.cs3733.c21.teamI.parking;

import edu.wpi.cs3733.c21.teamI.database.ParkingPeripheralServerManager;
import edu.wpi.cs3733.c21.teamI.hospitalMap.ParkingNode;
import edu.wpi.cs3733.c21.teamI.view.maps.MapPathfindingController;
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
                    System.out.println("Polling DB for parking Nodes");
                  }
                }));
    fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
    fiveSecondsWonder.play();
  }

  public void registerNode(ParkingNode node) {
    // TODO get linked slotId from DB
    System.out.println("Registering parking Node");
    int slotId = ParkingPeripheralServerManager.getInstance().getLinkedSlotId(node.getID());

    System.out.println(slotId);
    if (slotId != -1) {
      node.setParkingID(slotId);
      linkedNodes.put(slotId, node);
    }
  }

  public void pollDB() {

    Collection<Integer> occupied = ParkingPeripheralServerManager.getInstance().getOccupiedSlots();
    System.out.println(occupied);
    for (Integer i : linkedNodes.keySet()) {
      // System.out.println("Updating slotId: " + i);
      if (occupied.contains(i)) {
        // System.out.println("Setting " + linkedNodes.get(i).getShortName() + " to occupied");
        linkedNodes.get(i).setOccupied(true);
      } else {
        // System.out.println("Setting " + linkedNodes.get(i).getShortName() + " to unoccupied");
        linkedNodes.get(i).setOccupied(false);
      }
    }

    if (MapPathfindingController.lastInitialized != null)
      MapPathfindingController.lastInitialized.update();
  }
}
